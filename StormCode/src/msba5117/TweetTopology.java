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

class TweetTopology
{
  private static int TOP_N = 10;
  public static String DELIMITER="DELIMITER";
  public static String GEODELIMITER=":";
  public static void main(String[] args) throws Exception
  {
    // create the topology
    TopologyBuilder builder = new TopologyBuilder();
    // now create the tweet spout with the credentials
    // Please Enter your twitter credentials
    TweetSpout tweetSpout = new TweetSpout(
      "XtRdpc7oEHA4igDv4G0hsAWah",
      "mocRW6U2YQeZ7soM3Zx3a8qZ5teuQbDREqiiP7jt6RmIvU5P3Y",
      "3257961611-oveWDorGNbIaa7bNwq5uASHn2R7jZ2g3BUKs0xd",
      "D038CxDzF0uiiZUl1LHbKm4teLY71GSOvbGbmsx1WGmHB"
    );

    // attach the tweet spout to the topology - parallelism of 1
    builder.setSpout("tweet-spout", tweetSpout, 1);

    // attach the parse tweet bolt using shuffle grouping
    builder.setBolt("parse-tweet-bolt", new ParseTweetBolt(), 10).shuffleGrouping("tweet-spout");

    //attach the tweet location bolt using shuffle grouping
    //builder.setBolt("tweet-location-bolt", new TwitterLocationBolt(), 10).shuffleGrouping("tweet-spout");

    // attach the count bolt using fields grouping - parallelism of 15
    //builder.setBolt("count-bolt", new CountBolt(), 15).fieldsGrouping("parse-tweet-bolt", new Fields("tweet-word"));

    // attach rolling count bolt using fields grouping - parallelism of 5
    builder.setBolt("rolling-count-bolt", new RollingCountBolt(60, 20), 2).fieldsGrouping("parse-tweet-bolt", new Fields("tweet-word"));

    // attach the report bolt using global grouping - parallelism of 1
    builder.setBolt("tweet-location-bolt", new TweetLocationBolt(), 1).fieldsGrouping("rolling-count-bolt", new Fields("word"));

    // for geolocation filtering of all tweets
    builder.setBolt("intermediate-ranker", new IntermediateRankingsBolt(TOP_N), 4).fieldsGrouping("rolling-count-bolt", new Fields("word"));

    builder.setBolt("total-ranker", new TotalRankingsBolt(TOP_N)).globalGrouping("intermediate-ranker");

    builder.setBolt("report-bolt", new ReportBolt(), 1).globalGrouping("total-ranker").globalGrouping("tweet-location-bolt");
    //builder.setBolt("report-bolt", new ReportBolt(), 1).globalGrouping("total-ranker","stream1").globalGrouping("tweet-location-bolt","stream2");
    //builder.setBolt("report-bolt", new ReportBolt(), 1).globalGrouping("count-bolt");
    // create the default config object
    Config conf = new Config();

    // set the config in debugging mode
    conf.setDebug(true);

    if (args != null && args.length > 0) {

      // run it in a live cluster

      // set the number of workers for running all spout and bolt tasks
      conf.setNumWorkers(3);

      // create the topology and submit with config
      StormSubmitter.submitTopology(args[0], conf, builder.createTopology());

    } else {

      // run it in a simulated local cluster

      // set the number of threads to run - similar to setting number of workers in live cluster
      conf.setMaxTaskParallelism(3);

      // create the local cluster instance
      LocalCluster cluster = new LocalCluster();

      // submit the topology to the local cluster
      cluster.submitTopology("tweet-word-count", conf, builder.createTopology());

      // let the topology run for 300 seconds. note topologies never terminate!
      Utils.sleep(300000);

      // now kill the topology
      cluster.killTopology("tweet-word-count");

      // we are done, so shutdown the local cluster
      cluster.shutdown();
    }
  }
}
