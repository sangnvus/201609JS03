// Declare all map variables, objects,..
var map;
var geocoder;
var ambulanceInfoWindow;
var markers = [];
var ambulanceMakers = [];
var callerMaker;
var emergencyCenterPos;

var emergencyCenterIcon;
var emergencyCenterTitle;
var ambulanceList = [];
var listAmbulancePos;
var caller;
var takingCaller;
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
var emergencyCenterIconDir = 'assets/img/markers/ic_115_center.png';

// constan
const MAKER_TYPE_AMBULANCE = 'ambulance';
const MAKER_TYPE_CALLER = 'caller';