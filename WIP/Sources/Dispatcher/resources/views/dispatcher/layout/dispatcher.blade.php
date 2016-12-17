<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <link rel="icon" type="image/png" href="assets/img/markers/ic_115_center.png">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

    <title>Dispatcher</title>

    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />
    <meta name="viewport" content="width=device-width" />


    <!-- Bootstrap core CSS     -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" />

    <!-- Animation library for notifications   -->
    <link href="assets/css/animate.min.css" rel="stylesheet"/>

    <!--  Light Bootstrap Table core CSS    -->
    <link href="assets/css/dashboard.css" rel="stylesheet"/>    

    <!--  CSS for google map -->    
    <link href="assets/css/googlemap.css" rel="stylesheet" />

    <!--     Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
    <link href="assets/css/pe-icon-7-stroke.css" rel="stylesheet" />

    <!-- jquery UI lib -->
    <link rel="stylesheet" type="text/css" href="assets/css/jquery-ui.min.css">

    <base href="{{ asset('') }}">


</head>
<body>
    @if(session('noti'))
           <input type="text" id="sessionNoti" name="sessionNoti" value="{{ session('noti') }}" hidden>
           <input type="text" id="sessionCaller" name="sessionCaller" value="{{ session('caller') }}" hidden>
    @endif
    @if(session('ambulance'))
        <input type="text" id="sessionAmbulance" name="sessionNoti" value="{{ session('ambulance') }}" hidden>
        <input type="text" id="sessionCaller" name="sessionCaller" value="{{ session('caller') }}" hidden>
    @endif

    <div class="wrapper">
        <!-- sidebar left -->
        @include('dispatcher.layout.sidebarleft')

        <div class="main-panel">
            <!-- navbar -->
             @include('dispatcher.layout.navbar')

            <div id="map"></div>

        </div>

        <!-- sidebar right -->
        @include('dispatcher.layout.sidebarright')
    </div>


</body>

<!-- MAP -->
<script type="text/javascript">
    window.initMap = function() {

        initDefaultMap(); 

        // call init 115 center marker
        iniAMarker(emergencyCenterPos, emergencyCenterIconDir, emergencyCenterTitle, 'emergencyCenter');

        // call init all ambulance marker after load list ambulance
        // initAmbulanceMarkerAfterLoad();

        handlerReturnAmbulance();

} 

function initDefaultMap() {
  // Init service
  directionsService = new google.maps.DirectionsService;
  geocoder = new google.maps.Geocoder;

  emergencyCenterPos = {lat: 21.0222965, lng: 105.8567074};
  tmpPos = {lat: 21.0000, lng: 105.0000};
  emergencyCenterTitle = 'Trung tâm cấp cứu 115';

  initNewMap();

} 


function initNewMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    zoom: 17,
    center: emergencyCenterPos,
    streetViewControl:false
  });
  directionsDisplay = new google.maps.DirectionsRenderer({suppressMarkers: true});
  directionsDisplay.setMap(map);

}

//----- INIT A MARKER BY POST, ICON, TITLE
function iniAMarker(pos, icon, object, type){
  var marker = new google.maps.Marker({
    position: pos,
    map: map,
    icon: icon
  });
 
  infoWindow = new google.maps.InfoWindow({
    maxwidth: 300,
  });

  marker.addListener('mouseover', function() {
    if(type == MAKER_TYPE_AMBULANCE) {
      if(object.status == 'ready') {
        infoWindow.setContent(
          '<div style="color:green;">Đội: ' + object.team + '</div></br>' + 
          '<div style="color:green;"Vị trí: >' + object.latitude + '</div></br>'
        );
      } else {
         if(object.caller_taking_id != null) {
            getCallerInfoByID(object.caller_taking_id);
            infoWindow.setContent(
              '<div style="color:green;">Đội: ' + object.team + '</div></br>' + 
              '<div style="color:green;">Vị trí: ' + object.latitude + '</div></br>' +
              '<div style="color:green;">Đang đón: ' + takingCaller.phone + '</div></br>'
            );
          }
        
      }
      infoWindow.open(map, marker);
    } else {
      infoWindow.setContent('<h1>abc</h1>');
      infoWindow.open(map, marker);
    }
  });

  marker.addListener('mouseout', function() {
   infoWindow.close(map, marker);
  });

  markers.push(marker);

  if (type == MAKER_TYPE_AMBULANCE) {
    ambulanceMakers.push(marker);
  } else if(type == MAKER_TYPE_CALLER) {
    callerMaker = marker;
  }


}

function handlerReturnAmbulance() {
    if(document.getElementById("sessionAmbulance") != null) {
        var sessionAmbulance = document.getElementById("sessionAmbulance").value;
        var sessionCaller = document.getElementById("sessionCaller").value;
    }
    if(sessionAmbulance != null) {
        readyAmbulance = JSON.parse(sessionAmbulance);
        caller = JSON.parse(sessionCaller);
        pendingAmbulance(readyAmbulance, function(status) {
            if(status == AMBULANCE_STATUS_BUZY) {
                showNoti(NOTI_TYPE_SUCCESS, 'Đã nối xe cho người gọi', 2000);
                closeNotiBox();
                drawCallerAmbulancePatch(readyAmbulance, caller);
                processingCaller.push(caller);
                caller = null;
            } else if(status == AMBULANCE_STATUS_PROBLEM) {
                closeNotiBox();
                showConfirmBox('xe gặp sự cố, nối lại', function(result) {
                    if(result) {
                        onDispatchClick();
                    } else {
                        callCanCelDispatcheService(caller.id);
                        caller = null;
                        showAlertBox('Đã hủy');
                    }

                });
            }
        });
    }   
}

</script>

<!-- API -->
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBcG5eSgX7ZSWWdvnnNUTz4tzNhYIK3uBs&callback=initMap"
async defer></script> 
<script src="https://www.gstatic.com/firebasejs/3.6.1/firebase.js"></script>

<!--   Core JS Files   -->
<script src="assets/js/customizes/jquery-1.10.2.js" type="text/javascript"></script>
<script src="assets/js/customizes/bootstrap.min.js" type="text/javascript"></script>
<script src="assets/js/customizes/bootbox.min.js" type="text/javascript"></script>   
<script src="assets/js/customizes/bootstrap-checkbox-radio-switch.js"></script>
<script src="assets/js/customizes/chartist.min.js"></script>
<script src="assets/js/customizes/bootstrap-notify.js"></script>
<script src="assets/js/customizes/light-bootstrap-dashboard.js"></script>
<script src="assets/js/customizes/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="assets/js/customizes/jquery-ui.min.js" type="text/javascript"></script>

<!-- include commons script -->
<script src="assets/js/commons/models.js" type="text/javascript"></script>
<script src="assets/js/commons/variables.js" type="text/javascript"></script>

<!-- include function script -->
<script src="assets/js/functions/dispatchHandle.js" type="text/javascript"></script>
<script src="assets/js/functions/firebaseHandle.js" type="text/javascript"></script>
<script src="assets/js/functions/googlemapHandle.js" type="text/javascript"></script>
<script src="assets/js/functions/domControl.js" type="text/javascript"></script>    




<!-- autocomplete search phone number -->
<script type="text/javascript">
    $('#phone').autocomplete({
        source : '{!!URL::route('autocompletePhone')!!}',
        minlenght : 1, 
        autoFocus : true,
        select : function(e, ui){
                // Assign value for caller object
                initNewMap();
                var tmpObj = ui.item;
                caller = ui.item;
                tmpCallerPost = new google.maps.LatLng(caller.latitude, caller.longitude);
                callerPos = [];
                callerPos.push(tmpCallerPost);
                iniCallerForm(caller);
                clearAllMarkers();
                initAnCallerMarker(caller);
        }
    });
</script>
    

 <!-- Get list ambulance and update to firebase -->
    @if(session('noti'))
        <script type="text/javascript">
            checkNoti();
        </script> 
    @endif

     @if(session('ambulance'))
        <script type="text/javascript">

        </script>
     @endif

     <script type="text/javascript">
         //callCanCelDispatcheService(2);
     </script>

     <script type="text/javascript">
      setUnAvailableAmbulanceNoti();
     </script>




</html>


