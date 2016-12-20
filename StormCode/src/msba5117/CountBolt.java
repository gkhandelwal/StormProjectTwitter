package msba5117;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
/**
 * A bolt that counts the words that it receives
 */
public class CountBolt extends BaseRichBolt
{
  // To output tuples from this bolt to the next stage bolts, if any
  private OutputCollector collector;

  // Map to store the count of the words
  private Map<String, Long> countMap;

  // Map to store the location of the words
  private Map<String, Set<String>> locationMap;

  @Override
  public void prepare(
      Map                     map,
      TopologyContext         topologyContext,
      OutputCollector         outputCollector)
  {

    // save the collector for emitting tuples
    collector = outputCollector;

    // create and initialize the map
    countMap = new HashMap<String, Long>();
    locationMap = new HashMap<String,Set<String>>();
  }

  @Override
  public void execute(Tuple tuple)
  {
    // get the word from the 1st column of incoming tuple
    String word = tuple.getString(0).split(TweetTopology.DELIMITER)[0];

    // check if the word is present in the map
    if (countMap.get(word) == null) {
      // not present, add the word with a count of 1
      countMap.put(word, 1L);
      Set<String> temp = new HashSet<String>();
      temp.add(tuple.getString(0).split(TweetTopology.DELIMITER)[1]);
      locationMap.put(word,temp);
    } else {
      // already there, hence get the count
      Long val = countMap.get(word);

      // increment the count and save it to the map
      countMap.put(word, ++val);
      // for geolocation logic
      Set<String> temp = locationMap.get(word);
      temp.add(tuple.getString(0).split(TweetTopology.DELIMITER)[1]);
      locationMap.put(word,temp);
    }

    // emit the word and count
    collector.emit(new Values(word, countMap.get(word),String.join(",",locationMap.get(word))));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
  {
    // tell storm the schema of the output tuple for this spout
    // tuple consists of a two columns called 'word' and 'count'

    // declare the first column 'word', second column 'count', third column 'location'
    outputFieldsDeclarer.declare(new Fields("word","count", "location"));
  }
}
