package msba5117.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
/**
 * This class provides per-slot counts of the occurrences of objects.
 * <p/>
 * It can be used, for instance, as a building block for implementing sliding window counting of objects.
 *
 * @param <T> The type of those objects we want to count.
 */
public final class SlotBasedCounter<T> implements Serializable {

  private static final long serialVersionUID = 4858185737378394432L;

  private final Map<T, long[]> objToCounts = new HashMap<T, long[]>();
  private final Map<T, String[]> objToGeolocation = new HashMap<T, String[]>();
  private final int numSlots;

  public SlotBasedCounter(int numSlots) {
    if (numSlots <= 0) {
      throw new IllegalArgumentException("Number of slots must be greater than zero (you requested " + numSlots + ")");
    }
    this.numSlots = numSlots;
  }

  // done
  public void incrementCount(T obj, T geolocation, int slot) {
    long[] counts = objToCounts.get(obj);
    String[] geo = objToGeolocation.get(obj);
    if (counts == null) {
      counts = new long[this.numSlots];
      geo = new String[this.numSlots];
      objToCounts.put(obj, counts);
      objToGeolocation.put(obj, geo);
    }
    counts[slot]++;
    if((!geolocation.equals("0.0:0.0"))&&(geolocation!=null))
      geo[slot] = geo[slot]+","+geolocation;
    else
      geo[slot] = geo[slot]+"";
  }

  public long getCount(T obj, int slot) {
    long[] counts = objToCounts.get(obj);
    if (counts == null) {
      return 0;
    }
    else {
      return counts[slot];
    }
  }

  public String getGeolocation(T obj, int slot) {
    String[] geo = objToGeolocation.get(obj);
    if (geo == null) {
      return "";
    }
    else {
      return geo[slot];
    }
  }

  public Map<T, Long> getCounts() {
    Map<T, Long> result = new HashMap<T, Long>();
    for (T obj : objToCounts.keySet()) {
      result.put(obj, computeTotalCount(obj));
    }
    return result;
  }

  public Map<T, String> getGeolocations() {
    Map<T, String> result = new HashMap<T, String>();
    for (T obj : objToGeolocation.keySet()) {
      result.put(obj, computeTotalGeolocation(obj));
    }
    return result;
  }

  private long computeTotalCount(T obj) {
    long[] curr = objToCounts.get(obj);
    long total = 0;
    for (long l : curr) {
      total += l;
    }
    return total;
  }

  private String computeTotalGeolocation(T obj) {
    String[] curr = new HashSet<String>(Arrays.asList(objToGeolocation.get(obj))).toArray(new String[0]);
    String total = "";
    for (String l : curr) {
      if(l!=null && ! (l.isEmpty()))
        total = total +","+ l;
    }
    return total;
  }

  // done
  /**
   * Reset the slot count of any tracked objects to zero for the given slot.
   *
   * @param slot
   */
  public void wipeSlot(int slot) {
    for (T obj : objToCounts.keySet()) {
      resetSlotCountToZero(obj, slot);
    }
  }

 //done
  private void resetSlotCountToZero(T obj, int slot) {
    long[] counts = objToCounts.get(obj);
    String[] geo = objToGeolocation.get(obj);
    counts[slot] = 0;
    geo[slot]="";
  }

  //done
  private boolean shouldBeRemovedFromCounter(T obj) {
    return computeTotalCount(obj) == 0;
  }

  /**
   * Remove any object from the counter whose total count is zero (to free up memory).
   */
  // done
  public void wipeZeros() {
    Set<T> objToBeRemoved = new HashSet<T>();
    for (T obj : objToCounts.keySet()) {
      if (shouldBeRemovedFromCounter(obj)) {
        objToBeRemoved.add(obj);
      }
    }
    for (T obj : objToBeRemoved) {
      objToCounts.remove(obj);
      objToGeolocation.remove(obj);
    }
  }

}
