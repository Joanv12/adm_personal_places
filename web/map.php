<?
// Google Maps Help:
// https://developers.google.com/maps/documentation/javascript/examples/marker-simple
// Google Maps Styler:
// http://gmaps-samples-v3.googlecode.com/svn/trunk/styledmaps/wizard/index.html
// Get location:
// http://universimmedia.pagesperso-orange.fr/geo/loc.htm
// Communication from javascript to android:
// http://developer.android.com/guide/webapps/webview.html
$debug = true;
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>WebView</title>
<style type="text/css">
html, body, #map_canvas {
<? if ($debug) { ?>
	height: 300px;
<? } else { ?>
	height: 100%;
<? } ?>
	margin: 0px;
	padding: 0px
}
.bold {
	font-weight: bold !important;
}
.info_window {
	width: 240px;
}
.info_window .name {
	color: #D20;
	font-weight: bold !important;
	font-size: 14px !important;
}
.googft-info-window.rustic_plot {
	width: 180px !important;
}
</style>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
<script type="text/javascript">

var map = null;
var info_window = null;
var layers_set = {
	"1": ["col2", "1ECdXQ3g_dmn463XsKpMDRi81yBYiwDc-NRsSTbQK"], /* fuentes bebedero */
	"2": ["col2", "1FbAFcAiVM9DNwvW96EiHE7XImwQV5y64IMxP6A4r"], /* valenbisi */
	"3": ["col21", "1MKmNw9vmCeOXepyFlElJyrTIwUFpsI4f6CWmpkW4"], /* parcela rústica */
	"4": ["col21", "1XqwcmK5vVenG9NPL00AmUJu2wf9W2a-pBTY0E_y6"], /* parcela urbana */
	"5": ["col5", "1G9z6HsHYsjAMTHRNFXqZvlLM0Qae1rxaPBqWEzHB"], /* códigos postales */
	"6": ["col6", "1kBqQ2gT32EHESCtq1AHOZmpWmxSe3G4YvSKsczTb"], /* portales */
};
var latLngCenter = new google.maps.LatLng(39.47169, -0.34711);

$(function(){
	initialize_map();
	$("#button_showplace").click(function(){
		var lat = "39.47169";
		var lng = "-0.34711";
		var name = "Cantina Mariachi";
		var type = "Restaurante";
		var description = "Nachos y Tapas a todas horas, Nachos y Tapas a todas horas, Nachos y Tapas a todas horas, Nachos y Tapas a todas horas";
		add_place(lat, lng, name, type, description);
	});
	$("#button_showlayer").click(function(){
		add_layer("6");
	});
	$("#button_showplot").click(function(){
		var coords = "39.47214801806927,-0.34699738025665283;39.47217079342821,-0.34706711769104004;39.47218528683459,-0.34712880849838257;39.47220806218134,-0.3471797704696655;39.47222255557995,-0.34724414348602295;39.47223911946035,-0.34728705883026123;39.472195639265856,-0.3473058342933655;39.47215215904417,-0.34732192754745483;39.472110749283985,-0.34734874963760376;39.47206519851929,-0.34735947847366333;39.4720320706717,-0.34738630056381226;39.471988590347806,-0.3474023938179016;39.47194718049027,-0.34741848707199097;39.47190784110277,-0.3474453091621399;39.47186850169303,-0.34746140241622925;39.47183123275801,-0.3474801778793335;39.47178568181034,-0.34749627113342285;39.47174841283096,-0.3475204110145569;39.47170286182905,-0.347541868686676;39.4716655928053,-0.34756332635879517;39.471622112252376,-0.3475821018218994;39.47158277268109,-0.34760087728500366;39.47154964460378,-0.3476250171661377;39.47150409347175,-0.34764111042022705;39.47146268332583,-0.3476625680923462;39.47142127315528,-0.34768134355545044;39.471388145001086,-0.3476893901824951;39.47135294631997,-0.34767329692840576;39.4713301706933,-0.3476223349571228;39.471307395059156,-0.34756332635879517;39.47129704249572,-0.3475096821784973;39.47127840787767,-0.34746676683425903;39.47127012582358,-0.3474131226539612;39.47125356171247,-0.347367525100708;39.47124320914103,-0.34731924533843994;39.47122457450855,-0.3472656011581421;39.471210080902054,-0.34720927476882935;39.47119765780836,-0.3471609950065613;39.47118523471246,-0.3471073508262634;39.47116660006446,-0.34705907106399536;39.47115417696303,-0.3470054268836975;39.47119144626069,-0.34698665142059326;39.47122250399353,-0.3469651937484741;39.47126805530991,-0.3469464182853699;39.47131774762106,-0.34691959619522095;39.471369510407435,-0.346892774105072;39.4714109206088,-0.3468713164329529;39.47144611926058,-0.34684449434280396;39.471493740937575,-0.3468230366706848;39.47152893954748,-0.34680962562561035;39.47156620864441,-0.346793532371521;39.47160968923227,-0.34676671028137207;39.47164902878836,-0.34674525260925293;39.4716883683222,-0.34673184156417847;39.4717359898334,-0.3467103838920593;39.47178154081361,-0.3466916084289551;39.47181880977523,-0.34667015075683594;39.47187057218881,-0.346643328666687;39.47191198209197,-0.34662455320358276;39.471949250983734,-0.34659773111343384;39.47199066084005,-0.3465843200683594;39.47202585919853,-0.34656286239624023;39.47204656410698,-0.34662187099456787;39.47205691655889,-0.34669965505599976;39.47207762145808,-0.3467532992362976;39.47209625586209,-0.34680426120758057;39.47210867879533,-0.34687668085098267;39.47212524270285,-0.34691691398620605;";
		var name = "Plaza del Cedro";
		var description = "Plaza en zona estudiantil de Valencia. Plaza en zona estudiantil de Valencia. Plaza en zona estudiantil de Valencia";
		add_plot(coords, name, description);
	});
	$("#button_clearmap").click(function(){
		initialize_map();
	});
});
function get_info_window_content_place(name, type, description) {
	var result = 
		"<div class='info_window'>\n" +
		"<span class='name'>" + name + "</span><br/>\n" +
		"<span class='bold'>Tipo:</span> " + type + "<br/>\n" +
		"<span class='bold'>Descripción:</span> " + description + "<br/>\n" +
		"</div>\n";
	return result;
}
function get_info_window_content_plot(name, description) {
	var result = 
		"<div class='info_window'>\n" +
		"<span class='name'>" + name + "</span><br/>\n" +
		"<span class='bold'>Descripción:</span> " + description + "<br/>\n" +
		"</div>\n";
	return result;
}
function add_layer(layer) {
	var layer = new google.maps.FusionTablesLayer({
		map: map,
		heatmap: { enabled: false },
		query: {
			select: layers_set[layer][0],
			from: layers_set[layer][1],
			where: ""
		},
		options: {
			styleId: 2,
			templateId: 2
		}
	});
}
function add_place(lat, lng, name, type, description) {
	var latlngObj = new google.maps.LatLng(lat, lng);
	var marker = new google.maps.Marker({
		position: latlngObj,
		map: map,
	});
	google.maps.event.addListener(marker, "click", function (event) {
		if (info_window)
	        info_window.close();
		info_window = new google.maps.InfoWindow({
			content: get_info_window_content_place(name, type, description),
			position: event.latLng,
		});
		info_window.open(map);
	});
	google.maps.event.addListener(map, 'click', function() {
		if (info_window)
	        info_window.close();
    });
}
function add_plot(coords, name, description) {
	var markersArray = [];
	var coordsArr = coords.split(";");
	for (var i = 0; i < coordsArr.length; i++) {
		if (coordsArr[i] != "") {
			var temp = coordsArr[i].split(",");
			var lat = temp[0];
			var lng = temp[1];
			var pos = new google.maps.LatLng(lat, lng);
			var markerImage = {
				url: "imgs/marker_circle_red.png",
				size: new google.maps.Size(10, 10),
				origin: new google.maps.Point(0,0),
				anchor: new google.maps.Point(5, 5)
			};
			var marker = new google.maps.Marker({
				position: pos, 
				map: null,
				icon: markerImage
			});
			markersArray.push(marker);
		}
	}
	drawPolyline(markersArray, name, description);
	
	// polyline tooltip?
}
function drawPolyline(markersArray, name, description) {
	var polyline = null;
	var closingLine = null;
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
	google.maps.event.addListener(polyline, "click", function (event) {
		if (info_window)
	        info_window.close();
		info_window = new google.maps.InfoWindow({
			content: get_info_window_content_plot(name, description),
			position: event.latLng,
		});
		info_window.open(map);
	});
	google.maps.event.addListener(map, 'click', function() {
		if (info_window)
	        info_window.close();
    });
}
function initialize_map() {
	var canvas_selector = "#map_canvas";
	$(canvas_selector).empty();
	map = null;
	info_window = null;
	var mapOptions = {
		zoom: 16,
		center: latLngCenter,
		styles: [ { "stylers": [ { "visibility": "off" } ] },{ "featureType": "landscape", "stylers": [ { "visibility": "on" } ] },{ "featureType": "road", "stylers": [ { "visibility": "on" } ] },{ "featureType": "water", "stylers": [ { "visibility": "on" } ] },{ "featureType": "poi.park", "stylers": [ { "visibility": "on" } ] },{ "featureType": "administrative", "stylers": [ { "visibility": "on" } ] },{ "featureType": "poi.school", "stylers": [ { "visibility": "on" } ] },{ "featureType": "water", "stylers": [ { "visibility": "on" } ] } ],
		streetViewControl: false,
	}
	map = new google.maps.Map($(canvas_selector)[0], mapOptions);
}
</script>
</head>

<body>

<div id="map_canvas"></div>
<? if ($debug) { ?>
<input type="button" id="button_showlayer" value="html mostrar capa" />
<input type="button" id="button_showplace" value="html mostrar lugar" />
<input type="button" id="button_showplot" value="html mostrar parcela" />
<input type="button" id="button_clearmap" value="html limpiar mapa" />
<? } ?>
</body>
</html>
