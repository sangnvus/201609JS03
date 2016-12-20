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
var readyAmbulanceList = [];
var listAmbulancePos = [];
var caller;
var pendingCallers = [];
var takingCaller;
var callerPos;
var processingCaller = [];

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


const sttWaiting = 'waiting';
const sttProcessing = 'processing';
const AMBULANCE_STATUS_BUZY = 'buzy';
const AMBULANCE_STATUS_READY = 'ready';
const AMBULANCE_STATUS_PENDING = 'pending';
const AMBULANCE_STATUS_PROBLEM = 'problem';
const AMBULANCE_STATUS_PICKED = 'picked';



const NOTI_TYPE_SUCCESS = 'success';
const NOTI_TYPE_ERROR = 'error';
const NOTI_TYPE_PENDING = 'pending';

IS_AVAILABLE_AMBULANCE = true;

var DISPATCHER_ID;
var callerHistoryList = [];