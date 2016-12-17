
function reInitAmbulanceMaker() {
  clearAllAmbulanceMakers();
  initAmbulanceMarkers(ambulanceList);
}

function reInitDefaultMarkers() {
  clearAllAmbulanceMakers();
  iniAMarker(emergencyCenterPos, emergencyCenterIconDir, emergencyCenterTitle, 'emergencyCenter');
  initAmbulanceMarkers(ambulanceList);
}

function calculateAndDisplayRoute(directionsService, directionsDisplay, origin, destination, callback) {
  directionsService.route({
    origin: origin,
    destination: destination,
    travelMode: 'DRIVING'
  }, function(response, status) {
    if (status === 'OK') {
      directionsDisplay.setDirections(response);
      callback(response);
    } else {
      window.alert('Directions request failed due to ' + status);
    }
  });
}



// AFTER LOAD ALL AMBULANCE -> INIT MARKER
function initAmbulanceMarkerAfterLoad(){
  getListAmbulance();
  initAmbulanceMarkers(ambulanceList);
}

function initAmbulanceMarkers(ambulanceList) {
  for (var i = 0; i < ambulanceList.length; i++) {
    //initAnAmbulanceMarker(ambulanceList[i]);
    var pos = new google.maps.LatLng(ambulanceList[i].latitude, ambulanceList[i].longitude);
    var icon;
     var title = 'Xe cứu thương đội ' + ambulanceList[i].team;
    switch(ambulanceList[i].status) {
      case AMBULANCE_STATUS_READY:
        icon = ambulanceReadyIconDir;
        break;
      default:
        icon = ambulanceBuzyIconDir;
        break;
    }

   



    iniAMarker(pos, icon, ambulanceList[i], 'ambulance');

  }
}

function initAnCallerMarker(caller) {
  var pos = new google.maps.LatLng(caller.latitude, caller.longitude);
  var icon = 'assets/img/markers/ic_marker_caller.png';
  var title = 'Người gọi : ' + caller.phone;
  iniAMarker(pos, icon, title, 'caller');
    map.panTo(pos);
}

function clearAllMarkers() {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
   markers = [];
}

function clearAMaker(marker) {
  marker.setMap(null);
}

function clearAllAmbulanceMakers() {
  for (var i = 0; i < ambulanceMakers.length; i++) {
    ambulanceMakers[i].setMap(null);
  }
   ambulanceMakers = [];
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

function onClickLiAmbulance(ambulanceObject) {
  

  if(ambulanceObject.latitude != null && ambulanceObject.longitude != null) {
    var ambulancePos = new google.maps.LatLng(ambulanceObject.latitude, ambulanceObject.longitude);
  } else {
    ambulancePos = null;
  }

  if(ambulancePos != null) {
    initNewMap();
    getCallerInfoByID(ambulanceObject.caller_taking_id);
    clearCallerForm();
    if(takingCaller != null) {
      iniCallerForm(takingCaller);
      var callerPos = new google.maps.LatLng(takingCaller.latitude, takingCaller.longitude);
      calculateAndDisplayRoute(directionsService, directionsDisplay, ambulancePos, callerPos, function(results) {
        console.log(results);
      });
      // Init caller marker
      iniAMarker(callerPos, callerIconDir, 'caller');
      iniAMarker(ambulancePos, ambulanceBuzyIconDir, 'ambulance');
    } else {

        if(ambulanceObject.status == AMBULANCE_STATUS_READY) {
          iniAMarker(ambulancePos, ambulanceReadyIconDir, 'ambulance');

        } else {
          iniAMarker(ambulancePos, ambulanceBuzyIconDir, 'ambulance');
        }
        
        map.panTo(ambulancePos);  
    }
  } else {
    showNoti(NOTI_TYPE_ERROR, 'Không xác định được vị trí xe', 2000)
  }

}

// function onClickLiAmbulance(id) {
//   initNewMap();

//   // Get all value of this ambulance
//   var team = document.querySelector('#pAmbulanceTeam' + id).innerHTML;
//   var latitude = document.querySelector('#pAmbulanceLatitude' + id).innerHTML;
//   var longitude = document.querySelector('#pAmbulanceLongitude' + id).innerHTML;
//   var status = document.querySelector('#pAmbulanceStatus' + id).innerHTML;

//   // Clear all marker
//   clearAllMarkers();

//   // Create marker
//   var pos = new google.maps.LatLng(latitude, longitude);
//   var icon;
//   if(status == 'ready') {
//     icon = ambulanceReadyIconDir;
//   } else if(status == 'buzy') {
//     icon = ambulanceBuzyIconDir;
//   }
//   iniAMarker(pos, icon, 'ambulance');
//   map.panTo(pos);
//   map.setZoom(17);
// }

function drawPath(ambulancePos, callerPos) {

  // Init ambulance marker
  iniAMarker(ambulancePos, ambulanceBuzyIconDir, 'On the way');

  // Init caller marker
  iniAMarker(callerPos, callerIconDir, 'caller');

  calculateAndDisplayRoute(directionsService, directionsDisplay, ambulancePos, callerPos);
}
 

 function getCallerInfoByID(id) {
  $.ajax({
   type:'GET',
   url:'getCaller/' + id,
   data:'_token = <?php echo csrf_token() ?>',
   dataType: 'json',
   async: false,
   success:function(data){
     takingCaller = data.caller;
   }
  });
}

