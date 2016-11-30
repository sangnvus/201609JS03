// Declare all map variables, objects,..
var map;
var geocoder;
var ambulanceInfoWindow;
var markers = [];
var emergencyCenterPos;

var emergencyCenterIcon;
var emergencyCenterTitle;
var ambulanceList;
var listAmbulancePos;
var caller;
var callerPos;

var readyAmbulance;


var tmpPos1;
var tmpPos2;

// Service 
var directionsService;
var directionsDisplay;
var distanceMatrixService;

// Directory
var callerIconDir = 'assets/img/markers/ic_marker_caller.png';
var ambulanceReadyIconDir = 'assets/img/markers/ic_marker_ambulance_ready.png';
var ambulanceBuzyIconDir = 'assets/img/markers/ic_marker_ambulance_buzy.png';