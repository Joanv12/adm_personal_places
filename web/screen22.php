<?
require_once("includes/utils.inc.php");

$isplace = (isset($_GET["isplace"]) && $_GET["isplace"] == "true");
$coords = isset($_GET["coords"])? $_GET["coords"]: "";

$coords_js = trim($coords, ";");
$coords_js = explode(";", $coords_js);

if ($isplace)
	$coords_js = array($coords_js[0]);

if (count($coords_js) > 0 && $coords_js[0] != "")
	$coords_js = "[[".implode("],[", $coords_js)."]]";
else
	$coords_js = "[]";

?>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>screen05</title>
<style type="text/css">
html, body, #map_canvas {
	margin: 0;
	padding: 0;
	width: 100%;
	height: 100%;
}
</style>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
<script type="text/javascript">

// Interesting web pages:
// http://gmaps-samples-v3.googlecode.com/svn/trunk/styledmaps/wizard/index.html
// http://www.w3schools.com/googleAPI/google_maps_controls.asp

var map;
var isplace = <?=$isplace?"true":"false";?>;
var coords = <?=$coords_js;?>;
var markersArray = [];
var polyline = null;
var closingLine = null;

var standardMarkerImage = {
	url: "../imgs/marker_standard_red.png",
	size: new google.maps.Size(20, 34),
	origin: new google.maps.Point(0,0),
	anchor: new google.maps.Point(10, 34)
};

var circleMarkerImage = {
	url: "../imgs/marker_circle_red.png",
	size: new google.maps.Size(10, 10),
	origin: new google.maps.Point(0,0),
	anchor: new google.maps.Point(5, 5)
};

$(function(){
	initMap();
	for (var i = 0; i < coords.length; i++) {
		var location = new google.maps.LatLng(coords[i][0], coords[i][1]);
		var marker = new google.maps.Marker({
			position: location, 
			map: map,
			icon: ((isplace)?standardMarkerImage:circleMarkerImage),
		});
		markersArray.push(marker);
	}
	if (!isplace)
		pointsCount = markersArray.length;
	drawPolyline();
	centerMap();
});

function centerMap() {
	var bounds = new google.maps.LatLngBounds();
	for (var i = 0; i < markersArray.length; i++)
		bounds.extend(markersArray[i].position);
	if (markersArray.length > 0)
		map.fitBounds(bounds);
}

function initMap() {
	var latlng = new google.maps.LatLng(39.47171528483254, -0.34714221954345703);
	var styles = [ { "stylers": [ { "visibility": "off" } ] },{ "featureType": "landscape", "stylers": [ { "visibility": "on" } ] },{ "featureType": "road", "stylers": [ { "visibility": "on" } ] },{ "featureType": "water", "stylers": [ { "visibility": "on" } ] },{ "featureType": "poi.park", "stylers": [ { "visibility": "on" } ] },{ "featureType": "administrative", "stylers": [ { "visibility": "on" } ] },{ "featureType": "poi.school", "stylers": [ { "visibility": "on" } ] },{ "featureType": "water", "stylers": [ { "visibility": "on" } ] } ];
	var options = {
		center: latlng,
		mapTypeControl: false,
		streetViewControl: false,
		panControl: false,
		zoomControl: true,
		zoomControlOptions: {
			style: google.maps.ZoomControlStyle.SMALL
		},
		zoom: 16,
		mapTypeId: 'Styled'
	};
	map = new google.maps.Map(document.getElementById("map_canvas"), options);
    var styledMapType = new google.maps.StyledMapType(styles, { name: 'Styled' });
    map.mapTypes.set('Styled', styledMapType);
	google.maps.event.addListener(map, "click", function(event) {
		placeMarker(event.latLng.lat(), event.latLng.lng());
		setCoordinates(event.latLng.lat(), event.latLng.lng());
	});
}

function placeMarker(lat, lng) {
	var location = new google.maps.LatLng(lat, lng);
	deleteOverlays();
	var marker = new google.maps.Marker({
		position: location, 
		map: map,
		icon: ((isplace)?standardMarkerImage:circleMarkerImage),
	});
	markersArray.push(marker);
	drawPolyline();
	if (isplace) {
		map.panTo(marker.getPosition());
		setDataOnJava();
	}
}

function deleteOverlays() {
	var j = 0;
	if (markersArray) {
		for (i in markersArray) {
			j++;
			if (j > pointsCount)
				markersArray[i].setMap(null);
		}
		markersArray.length = pointsCount;
	}
	// workaround of issue explained here:
	// https://code.google.com/p/android/issues/detail?id=36803
	// https://code.google.com/p/gmaps-api-issues/issues/detail?id=4597
	var zoom = map.getZoom();
	map.setZoom(zoom-5);
	map.setZoom(zoom)
}

var pointsCount = 0;
function fixLastPoint() {
	pointsCount = markersArray.length;
	setDataOnJava();
}

function deleteLastPoint() {
	if (markersArray.length != 0 && (markersArray.length == pointsCount))
		pointsCount--;
	deleteOverlays();
	drawPolyline();
	setDataOnJava();
}

function drawPolyline() {
	if (polyline)
		polyline.setMap(null);
	if (closingLine)
		closingLine.setMap(null);
		
	var path = [];
	for (i in markersArray) {
		path.push(markersArray[i].position);
	}
	polyline = new google.maps.Polyline({
		path: path,
		strokeColor: "#0000FF",
		strokeOpacity: 0.8,
		strokeWeight: 2
	});
	polyline.setMap(map);
	
	if (markersArray.length > 2) {
		var closingLinePath = [];
		closingLinePath.push(markersArray[0].position);
		closingLinePath.push(markersArray[markersArray.length-1].position);
		var lineSymbol = {
			path: 'M 0, -2 0, 1',
			strokeOpacity: 0.4,
			scale: 3
		};
		closingLine = new google.maps.Polyline({
			path: closingLinePath,
			strokeColor: "#FF0000",
			strokeWeight: 2,
			strokeOpacity: 0,
			icons: [{
				icon: lineSymbol,
				offset: '0',
				repeat: '20px'
			}]
		});
		closingLine.setMap(map);
	}
}

function setDataOnJava() {
	var output = "";
	if (markersArray) {
		for (i in markersArray) {
			var marker_position = markersArray[i].getPosition();
			var lat = marker_position.lat();
			var lng = marker_position.lng();
			output += lat + "," + lng + ";";
		}
	}
	JavascriptManager.setDataFromJavaScript(output);
}

</script>
</head>

<body>
    <div id="map_canvas"></div>
</body>
</html>
