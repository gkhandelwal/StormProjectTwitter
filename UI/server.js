var express = require('express');

var redis = require('redis');
var sub = redis.createClient();

app = express();
app.use(express.static('public')) // We will want this later
app.set('view engine', 'ejs') // <-- Line you are adding


var port = process.env.PORT || 5000; // For when we deploy to Heroku
var server = app.listen(port)
var io = require('socket.io').listen(server);

var tweetsArray = [];
var mapArray = [];
var tempMapArray = [];
var numOfElements = 0;
var nSize = 10;

sub.on("subscribe", function(channel, count) {
	if(channel=="TwitterLocation")
	{
    	console.log("Subscribed to " + channel + ". Now subscribed to " + count + " channel(s).");
		io.emit('geoTweets', {'messageGeo1':'##Retail|42.9097484:-85.7630885'});
	}
});

sub.on("message", function(channel, message) {
	if(channel=="TwitterLocation")
	{
		if(mapArray.length==2)
		{
		  mapArray.shift();
		}
		if(numOfElements<nSize)
		{
		  var tmpGaurav = message.toString().split("|");
		  var tmpHello = tmpGaurav[1].split(",")
		  for(var k=0;k<tmpHello.length;k++)
		  {
			var resultMessage = tmpGaurav[0] + "|" +  tmpHello[k];
			tempMapArray.push(resultMessage);
		  }
		  numOfElements++;
		  
		}
		else
		{
		    mapArray.push(tempMapArray.join('==>'));
		    var messageGeo1="",messageGeo2="",messageGeo3="";
		    if(mapArray.length>=1)
		      messageGeo1 = mapArray[0];
		    if(mapArray.length>=2)
		      messageGeo2 = mapArray[1];
		    numOfElements=0;
		    io.emit('geoTweets', {'messageGeo1': messageGeo1,'messageGeo2': messageGeo2,'messageGeo3': messageGeo3});
		    tempMapArray=[];
			nSize = 20;
		}
		console.log("#" + message);
	}
	else
	{
			if(tweetsArray.length==6)
			{
				tweetsArray.shift();
			}
			tweetsArray.push(message.toString());
			var message1="",message2="",message3="",message4="",message5="",message6="";

			if(tweetsArray.length>=1)
			  message1 = tweetsArray[0];
			if(tweetsArray.length>=2)
			  message2 = tweetsArray[1];
			if(tweetsArray.length>=3)
			  message3 = tweetsArray[2];
			if(tweetsArray.length>=4)
			  message4 = tweetsArray[3];
			if(tweetsArray.length>=5)
			  message5 = tweetsArray[4];
			if(tweetsArray.length>=6)
			  message6 = tweetsArray[5];
			io.emit('tweets', {'message1': message1,'message2': message2,'message3': message3,'message4': message4,'message5': message5,'message6': message6});
	}
});

sub.subscribe("WordCountTopology","TwitterLocation");

app.get('/', function(req, res){
    console.log("get request received");
    res.render('index')
})

app.get('/hello', function(req, res){
	io.emit('geoTweets', {'messageGeo1':'##Retail|42.9097484:-85.7630885==>##miprimertattoo|36.7167:-4.41667'});
    console.log("hello request received");
	return false;
})

app.get('/bye', function(req, res){
	io.emit('geoTweets', {'messageGeo1':'##job:|42.9097484:-85.7630885==>##CareerArc|33.6890603:-78.8866943'});
    console.log("hello request received");
	return false;
})
