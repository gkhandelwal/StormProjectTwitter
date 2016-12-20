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
import java.util.Map;

public class TweetLocationBolt extends BaseRichBolt
{
  // To output tuples from this bolt to the next stage bolts, if any
  private OutputCollector collector;


  @Override
  public void prepare(
      Map                     map,
      TopologyContext         topologyContext,
      OutputCollector         outputCollector)
  {
    collector = outputCollector;
  }

  @Override
  public void execute(Tuple tuple)
  {
    String location="";
    // get the word from the 1st column of incoming tuple
    String word = tuple.getString(0);
    String locationString[] = tuple.getString(2).split(",");
    for(String loc : locationString)
    {
      if(loc!=null && !loc.equals("") && !loc.equals("null") && !loc.isEmpty())
      {
        if(location.equals(""))
          location = loc;
        else
          location = location + "," + loc;
      }
    }
    // emit the word and count
    collector.emit(new Values(word, location));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
  {
    // tell storm the schema of the output tuple for this spout
    // tuple consists of a two columns called 'word' and 'count'

    // declare the first column 'word', second column 'count', third column 'location'
    outputFieldsDeclarer.declare(new Fields("word","location"));
  }
}
