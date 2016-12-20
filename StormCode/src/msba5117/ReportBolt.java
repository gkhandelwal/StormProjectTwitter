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
import java.util.List;
import java.util.ArrayList;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;

import msba5117.tools.*;
import msba5117.tools.Rankings;

/**
 * A bolt that prints the word and count to redis
 */
public class ReportBolt extends BaseRichBolt
{
  // place holder to keep the connection to redis
  transient RedisConnection<String,String> redis;

  @Override
  public void prepare(
      Map                     map,
      TopologyContext         topologyContext,
      OutputCollector         outputCollector)
  {
    // instantiate a redis connection
    RedisClient client = new RedisClient("localhost",6379);
    // initiate the actual connection
    redis = client.connect();
  }

  @Override
  public void execute(Tuple tuple)
  {
      String componentId = tuple.getSourceComponent();
      if ("total-ranker".equals(componentId))
      {
          List<String> listOfTweetsAndLocation = new ArrayList<String>();
          Rankings rankableList = (Rankings) tuple.getValue(0);
          for (Rankable r: rankableList.getRankings()){
            String word = r.getObject().toString();
            Long count = r.getCount();
            String geolocation = r.getGeoLocation().toString();

            String temp = word + "|" + Long.toString(count)+"|"+ geolocation;
            listOfTweetsAndLocation.add(temp);
          }

          String tweetsResults = String.join("==>" , listOfTweetsAndLocation);
          redis.publish("WordCountTopology", tweetsResults);
       }
       else if ("tweet-location-bolt".equals(componentId))
       {
          String word = tuple.getStringByField("word");
          String location = tuple.getStringByField("location");
          if(location!=null && !location.isEmpty())
            redis.publish("TwitterLocation", word + "|" + location);
       }
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {
    // nothing to add - since it is the final bolt
  }
}
