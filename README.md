# StormProject

For storm, we used Linux machine -  Ubuntu Linux 4.4.0-53-generic

***

**Installation Steps: **
- First step is to install java, we used java 8 and add Java to Path. We added entries for this in ~/.bashrc and load them again using source ~/.bashrc
- Next, we need to download zookeeper. We download it from zooker official site http://zookeeper.apache.org/releases.html. We configured zookeeper, by modifying conf/zoo.conf file with this information
 [ Source Tutorialspoint.com ] :

	*tickTime=2000
	dataDir=/path/to/zookeeper/data
	clientPort=2181
	initLimit=5
	syncLimit=2*
	
- Next, we need to download Storm, which we downloaded from official site http://storm.apache.org/downloads.html . We configured storm, by modifying conf/storm.yaml file with this information [ Source Tutorialspoint.com ] :
	*storm.zookeeper.servers:
	 - "localhost"
	storm.local.dir: “/path/to/storm/data”
	nimbus.host: "localhost"
	supervisor.slots.ports:
	 6700
	 6701
	 6702
	 6703*
- Next start, zookeeper, with bin/zkServer.sh start
- Add storm to path, I added the entries in bashrc file.
- Next start nimbus, with bin/storm nimbus
- Next start supervisor, with bin/storm supervisor
- Next start UI, with bin/storm ui
- Now, because we used redis, you need to install redis. To do this

	*sudo apt-get install redis-server
	start redis, redis-server
	to see key-value, use redis-cli*
	
	
- Now, installation is completed and storm is up. Next step is to run code. There are two part, one is client ( nodejs ) and other one is server ( Storm source code)
- To run node server, run the following command
- npm install → this will install all the necessary packages/libraries
- nodejs server.js :- Now, your server is up, which will display result. 
- Next compile storm code. To do this, run following commands :
	*mvn clean
	mvn package -> This will generate jar
	storm target jar-name mainClassName*
	
- Next open browser, localhost:5000, and you will see trending tweets after few seconds.



