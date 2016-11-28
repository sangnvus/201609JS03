// Declare all map variables, objects,..
var map;
var geocoder;
var ambulanceInfoWindow;
var markers = [];
var emergencyCenterPos;
var emergencyCenterIcon;
var emergencyCenterTitle;
var ambulanceList;




function initMap() {

  initDefaultMap(); 

  // call init 115 center marker
  iniAMarker(emergencyCenterPos, emergencyCenterIcon, emergencyCenterTitle);

  // call init all ambulance marker after load list ambulance
  initAmbulanceMarkerAfterLoad();

}

function initDefaultMap() {
  emergencyCenterPos = {lat: 21.0222965, lng: 105.8567074};
  emergencyCenterIcon = 'assets/img/markers/ic_marker_caller.png';
  emergencyCenterTitle = 'Trung tâm cấp cứu 115';
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 17,
    center: emergencyCenterPos,
    streetViewControl:false
  });
  geocoder = new google.maps.Geocoder;
} 

//----- INIT A MARKER BY POST, ICON, TITLE
function iniAMarker(pos, icon, title){
  var marker = new google.maps.Marker({
    position: pos,
    map: map,
    title: title,
    icon: icon
  });

  infoWindow = new google.maps.InfoWindow({
    content: title, 
    maxwidth: 300,
  });

  marker.addListener('mouseover', function() {
    infoWindow.open(map, marker);
  });

  marker.addListener('mouseout', function() {
    infoWindow.close(map, marker);
  });

  markers.push(marker);
}

// LOAD AMBULANCE LIST
let getAmbulanceList = new Promise(function(resolve, reject) {
   $.get('ambulance', function(data) {
    ambulanceList = data.ambulance;

    // markers affter load
    resolve(ambulanceList);
  });

});

// AFTER LOAD ALL AMBULANCE -> INIT MARKER
function initAmbulanceMarkerAfterLoad(){
  getAmbulanceList.then(initAmbulanceMarkers);
}

function initAmbulanceMarkers(ambulanceList) {
  for (var i = 0; i < ambulanceList.length; i++) {
    //initAnAmbulanceMarker(ambulanceList[i]);
    var pos = new google.maps.LatLng(ambulanceList[i].latitude, ambulanceList[i].longitude);
    var icon;
    if(ambulanceList[i].status == 'ready') {
      icon = 'assets/img/markers/ic_marker_ambulance_ready.png';
    } else if(ambulanceList[i].status == 'buzy') {
      icon = 'assets/img/markers/ic_marker_ambulance_buzy.png';
    }
    var title = 'Xe cứu thương đội ' + ambulanceList[i].team;

    iniAMarker(pos, icon, title);

  }
}

function initCallerMarkerAfterClearAll(caller, callback) {
  clearAllMarkers();
  callback(caller); // to tuong truyen o day la, do
}

function initAnCallerMarker(caller) {
  var pos = new google.maps.LatLng(caller.latitude, caller.longitude);
  var icon = 'assets/img/markers/ic_marker_caller.png';
  var title = 'Người gọi : ' + caller.phone;
  iniAMarker(pos, icon, title);
    map.panTo(pos);
}


function clearAllMarkers() {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
   markers = [];
}

function geocodeLatLng(geocoder, map, locationString, callback) {
  var address;
  var latlngStr = locationString.split(',', 2);
  var latlng = {lat: parseFloat(latlngStr[0]), lng: parseFloat(latlngStr[1])};
  geocoder.geocode({'location': latlng}, function(results, status) {
    if (status === 'OK') {
      if (results[1]) {
        address = results[1].formatted_address;
      } else {
        address = 'Không tìm được vị trí';
      }
    } else {
      address = 'Lỗi tìm vị trí';
    }
    callback(address);
  });
}






