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
import java.util.Arrays;

/**
 * A bolt that parses the tweet into words
 */
public class TwitterLocationBolt extends BaseRichBolt
{
  // To output tuples from this bolt to the count bolt
  private String[] skipWords = {"rt", "to", "me","la","on","that","que",
    "followers","watch","know","not","have","like","I'm","new","good","do",
    "more","es","te","followers","Followers","las","you","and","de","my","is",
    "en","una","in","for","this","go","en","all","no","don't","up","are",
    "http","http:","https","https:","http://","https://","with","just","your",
    "para","want","your","you're","really","video","it's","when","they","their","much",
    "would","what","them","todo","FOLLOW","retweet","RETWEET","even","right","like",
    "bien","Like","will","Will","pero","Pero","can't","were","Can't","Were","TWITTER",
    "make","take","This","from","about","como","esta","follows","followed"};
  OutputCollector collector;

  @Override
  public void prepare(
      Map                     map,
      TopologyContext         topologyContext,
      OutputCollector         outputCollector)
  {
    // save the output collector for emitting tuples
    collector = outputCollector;
  }

  @Override
  public void execute(Tuple tuple)
  {
    // get the 1st column 'tweet' from tuple
    String tweet = tuple.getStringByField("tweet").split("DELIMITER")[0];
    //get the GEO information from each twitter
    double latitude = Double.parseDouble(tuple.getStringByField("tweet").split("DELIMITER")[1].split(",")[0]);
    double longitude = Double.parseDouble(tuple.getStringByField("tweet").split("DELIMITER")[1].split(",")[1]);

    // provide the delimiters for splitting the tweet
    String delims = "[ .,?!]+";

    // now split the tweet into tokens
    String[] tokens = tweet.split(delims);


     // for each token/word, emit it
    for (String token : tokens) {
            //emit only words greater than length 3 and not stopword list
          if (token.length() > 3 &&!Arrays.asList(skipWords).contains(token)) {
              if(token.startsWith("#")){
                collector.emit(new Values(token,String.valueOf(latitude),String.valueOf(longitude)));
              }
          }
    }


    // for each token/word, emit it
    //for (String token: tokens) {
    //  collector.emit(new Values(token));
    //}
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer)
  {
    // tell storm the schema of the output tuple for this spout
    // tuple consists of a single column called 'tweet-word'
    declarer.declare(new Fields("tweet-word","latitude","longitude"));
  }
}
