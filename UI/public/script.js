var socket = io.connect();
var i =0;
var j =0;
var result="";
var labelsArray=[];
var dataArray=[];
var mapFlag = 0;
var mapsKey="AIzaSyBGNBCXJvP-0j9yyvX0F10W06icU5FeuU0"
var mymap="";
var markers2="";
var map1="";
var markers2="";
var map2="";

function addTweets(data6,data5,data4,data3,data2,data1) {
    var datasArray = data1.split("==>");
    var result="";
    var result1="",result2="",result3="",result4="",result5="",result6="";
    for(var i=0;i<datasArray.length;i++)
    {
      var datas = datasArray[i].split("|");
      result=result+'<tr><td>' + datas[0] + '</td></tr>';
    }
    result1 = '<table id="t01"><tr><th><b>Current</b></th></tr>' +  result + '</table>';
    result="";
    datasArray = data2.split("==>");
    for(var i=0;i<datasArray.length;i++)
    {
      var datas = datasArray[i].split("|");
      result=result+'<tr><td>' + datas[0] + '</td></tr>';
    }
    result2 = '<table id="t01"><tr><th><b>10 seconds ago</b></th></tr>' +  result + '</table>';
    result="";
    datasArray = data3.split("==>");
    for(var i=0;i<datasArray.length;i++)
    {
      var datas = datasArray[i].split("|");
      result=result+'<tr><td>' + datas[0] + '</td></tr>';
    }
    result3 = '<table id="t01"><tr><th><b>20 seconds ago</b></th></tr>' +  result + '</table>';
    result="";
    datasArray = data4.split("==>");
    for(var i=0;i<datasArray.length;i++)
    {
      var datas = datasArray[i].split("|");
      result=result+'<tr><td>' + datas[0] + '</td></tr>';
    }
    result4 = '<table id="t01"><tr><th><b>30 seconds ago</b></th></tr>' +  result + '</table>';
    result="";
    datasArray = data5.split("==>");
    for(var i=0;i<datasArray.length;i++)
    {
      var datas = datasArray[i].split("|");
      result=result+'<tr><td>' + datas[0] + '</td></tr>';
    }
    result5 = '<table id="t01"><tr><th><b>40 seconds ago</b></th></tr>' +  result + '</table>';
    result="";
    datasArray = data6.split("==>");
    for(var i=0;i<datasArray.length;i++)
    {
      var datas = datasArray[i].split("|");
      result=result+'<tr><td>' + datas[0] + '</td></tr>';
    }
    result6 = '<table id="t01"><tr><th><b>50 seconds ago</b></th></tr>' +  result + '</table>';
    result="";

    $(".tweetEntries0").empty();
    $(".tweetEntries1").empty();
    $(".tweetEntries2").empty();
    $(".tweetEntries3").empty();
    $(".tweetEntries4").empty();
    $(".tweetEntries5").empty();
          //$("#tweetEntries").append('<div class="message"><p>' + 'topic : ' + datas[0] + ' Frequency : ' + datas[1] + '</p></div>');
    $(".tweetEntries0").append(result1);
    $(".tweetEntries1").append(result2);
    $(".tweetEntries2").append(result3);
    $(".tweetEntries3").append(result4);
    $(".tweetEntries4").append(result5);
    $(".tweetEntries5").append(result6);
}

function drawCharts(data6,data5,data4)
{
        var labelsArray =[];
        var dataArray =[];
/******************CHART1 CONTENT***************************************************************/
        var datasArray = data6.split("==>");
        for(var i=0;i<datasArray.length;i++)
        {
            var datas = datasArray[i].split("|");
            labelsArray.push(datas[0].substring(0,21));
            dataArray.push(parseInt(datas[1]));
        }
        var riceData = {
            labels : labelsArray,
            datasets :
             [
                {
                  fillColor : "rgba(172,194,132,0.4)",
                  strokeColor : "#ACC26D",
                  pointColor : "#fff",
                  pointStrokeColor : "#9DB86D",
                  data : dataArray
                }
             ]
            }
        var rice = document.getElementById('myChart1').getContext('2d');
        new Chart(rice).Line(riceData);
        dataArray=[];
        labelsArray=[];
/******************CHART2 CONTENT***************************************************************/
        datasArray = data5.split("==>");
        for(var i=0;i<datasArray.length;i++)
        {
            var datas = datasArray[i].split("|");
            labelsArray.push(datas[0].substring(0,21));
            dataArray.push(parseInt(datas[1]));
        }
        var riceData = {
            labels : labelsArray,
            datasets :
             [
                {
                  fillColor : "rgba(172,194,132,0.4)",
                  strokeColor : "#ACC26D",
                  pointColor : "#fff",
                  pointStrokeColor : "#9DB86D",
                  data : dataArray
                }
             ]
            }
        var rice = document.getElementById('myChart2').getContext('2d');
        new Chart(rice).Line(riceData);
        dataArray=[];
        labelsArray=[];

/******************CHART3 CONTENT***************************************************************/
        datasArray = data4.split("==>");
        for(var i=0;i<datasArray.length;i++)
        {
            var datas = datasArray[i].split("|");
            labelsArray.push(datas[0].substring(0,21));
            dataArray.push(parseInt(datas[1]));
        }
        var riceData = {
            labels : labelsArray,
            datasets :
             [
                {
                  fillColor : "rgba(172,194,132,0.4)",
                  strokeColor : "#ACC26D",
                  pointColor : "#fff",
                  pointStrokeColor : "#9DB86D",
                  data : dataArray
                }
             ]
            }
        var rice = document.getElementById('myChart3').getContext('2d');
        new Chart(rice).Line(riceData);
        dataArray=[];
        labelsArray=[];
}

function populate(data,map,markersq) {
  markersq.clearLayers();
  var arrayOfLatLngs1 = data.split("==>")
  for (var i=0; i<arrayOfLatLngs1.length; i++)
    {
        var tempArray = arrayOfLatLngs1[i].split("|")
        if(tempArray.length!=2)
          continue;
		var anotherTempArray = tempArray[1].split(":");
		if(anotherTempArray.length!=2)
		   continue;
		var marker = new L.marker([anotherTempArray[0],anotherTempArray[1]]);
		marker.bindPopup(tempArray[0], {
		        showOnMouseOver: true
		    });
		markersq.addLayer(marker);
   }
	arrayOfLatLngs1=[];
    return false;
}

function drawMap(data1, data2) {
      if(mapFlag==0)
      {
			var mapLink = '<a href="http://openstreetmap.org">OpenStreetMap</a>';
            var leaflet1 = L.tileLayer( 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		        attribution: '&copy; ' + mapLink + ' Contributors',
		        maxZoom: 18,
		        });
			
			var leaflet2 = L.tileLayer( 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
		        attribution: '&copy; ' + mapLink + ' Contributors',
		        maxZoom: 18,
		        });

             map1 = L.map('map1').setView([46.7296, 94.6859], 8).addLayer(leaflet1);
             markers1 = new L.FeatureGroup();

			 map2 = L.map('map2').setView([94.6859, 94.6859], 8).addLayer(leaflet2);
             markers2 = new L.FeatureGroup();
      }
	  if(data1!="")
	  {
      	 map1.removeLayer(markers1);
         populate(data1,map1,markers1);
         map1.addLayer(markers1);
	  }

	  if(data2!="")
	  {
	  	map2.removeLayer(markers2);
      	populate(data2,map1,markers2);
      	map2.addLayer(markers2);
	  }

      mapFlag=1
}

socket.on('tweets', function(data) {
    addTweets(data['message1'],data['message2'],data['message3'],data['message4'],data['message5'],data['message6']);
    drawCharts(data['message6'],data['message5'],data['message4']);
})

socket.on('geoTweets', function(data) {
	var dataComing1="";
	var dataComing2="";

    console.log(data['messageGeo1']);
	console.log(data['messageGeo2']);

	if(typeof data['messageGeo1'] !== 'undefined')
		dataComing1=data['messageGeo1'];

	if(typeof data['messageGeo2'] !== 'undefined')
		dataComing2=data['messageGeo2'];

	drawMap(dataComing1,dataComing2);
})
