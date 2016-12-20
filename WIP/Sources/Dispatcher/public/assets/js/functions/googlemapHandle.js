function initMap() {

    initDefaultMap(); 

    // call init 115 center marker
    iniAMarker(emergencyCenterPos, emergencyCenterIconDir, emergencyCenterTitle, 'emergencyCenter');

    // call init all ambulance marker after load list ambulance
    // initAmbulanceMarkerAfterLoad();
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
function iniAMarker(pos, icon, title, type){
    var marker = new google.maps.Marker({
        position: pos,
        map: map,
        icon: icon
    });

    infoWindow = new google.maps.InfoWindow({
        maxwidth: 300,
    });

    marker.addListener('mouseover', function() {
            infoWindow.setContent(title);
            infoWindow.open(map, marker);
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

// function handlerReturnAmbulance() {
//     if(document.getElementById("sessionAmbulance") != null) {
//         var sessionAmbulance = document.getElementById("sessionAmbulance").value;
//         var sessionCaller = document.getElementById("sessionCaller").value;
//     }
//     if(sessionAmbulance != null) {
//         readyAmbulance = JSON.parse(sessionAmbulance);
//         caller = JSON.parse(sessionCaller);
//         pendingAmbulance(readyAmbulance, function(status) {
//             if(status == AMBULANCE_STATUS_BUZY) {
//                 showNoti(NOTI_TYPE_SUCCESS, 'Đã nối xe cho người gọi', 2000);
//                 closeNotiBox();
//                 drawCallerAmbulancePatch(readyAmbulance, caller);
//                 processingCaller.push(caller);
//                 caller = null;
//             } else if(status == AMBULANCE_STATUS_PROBLEM) {
//                 closeNotiBox();
//                 showConfirmBox('xe gặp sự cố, nối lại', function(result) {
//                     if(result) {
//                         onDispatchClick();
//                     } else {
//                         callCanCelDispatcheService(caller.id);
//                         caller = null;
//                         showAlertBox('Đã hủy');
//                     }

//                 });
//             }
//         });
//     }   
// }



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
    travelMode: 'DRIVING',
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



    iniAMarker(pos, icon, ambulanceList[i].team, MAKER_TYPE_AMBULANCE);

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
  takingCaller = null;
  initNewMap();
  ambulanceObject = ambulanceObject;
  
  if(ambulanceObject.latitude != null && ambulanceObject.longitude != null) {
    var ambulancePos = new google.maps.LatLng(ambulanceObject.latitude, ambulanceObject.longitude);
  } else {
    ambulancePos = null;
  }

  if(ambulancePos != null) {

    // Re assign list
    ambulanceMakers = [];
    ambulanceList = [];
    ambulanceList.push(ambulanceObject);

    initNewMap();
    getCallerInfoByID(ambulanceObject.caller_taking_id);
    clearCallerForm();
    if(takingCaller != null) {
      iniCallerForm(takingCaller);
      var callerPos = new google.maps.LatLng(takingCaller.latitude, takingCaller.longitude);
      if(ambulanceObject.status != 'picked') {
         calculateAndDisplayRoute(directionsService, directionsDisplay, ambulancePos, callerPos, function(results) {
          ambulanceObject.distance = results.routes[0].legs[0].distance.text;
          ambulanceObject.duration = results.routes[0].legs[0].duration.text;
          showInfoBox(ambulanceObject, takingCaller);
        });
        // Init caller marker
        iniAMarker(callerPos, callerIconDir, takingCaller.phone,  'caller');
        iniAMarker(ambulancePos, ambulanceBuzyIconDir,ambulanceObject.team , MAKER_TYPE_AMBULANCE);
      } else {
        // Init caller marker
        iniAMarker(ambulancePos, ambulanceBuzyIconDir,ambulanceObject.team , MAKER_TYPE_AMBULANCE);
      }
      showInfoBox(ambulanceObject, takingCaller);
     
      
      map.panTo(ambulancePos);
    } else {

        if(ambulanceObject.status == AMBULANCE_STATUS_READY) {
          iniAMarker(ambulancePos, ambulanceReadyIconDir,ambulanceObject.team, MAKER_TYPE_AMBULANCE);

        } else {
          iniAMarker(ambulancePos, ambulanceBuzyIconDir,ambulanceObject.team, MAKER_TYPE_AMBULANCE);
        }
        
        map.panTo(ambulancePos);
        showInfoBox(ambulanceObject, takingCaller);  
    }
  } else {
    showNoti(NOTI_TYPE_ERROR, 'Không xác định được vị trí xe', 2000) 
    showInfoBox(ambulanceObject, takingCaller);  
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

// function drawPath(ambulancePos, callerPos) {

//   // Init ambulance marker
//   iniAMarker(ambulancePos, ambulanceBuzyIconDir, 'On the way');

//   // Init caller marker
//   var title = 'Người gọi : ' + caller.phone;
//   iniAMarker(callerPos, callerIconDir, 'caller');

//   calculateAndDisplayRoute(directionsService, directionsDisplay, ambulancePos, callerPos);
// }
 

 function getCallerInfoByID(id) {
  $.ajax({
   type:'GET',
   url:'getcaller/' + id,
   data:'_token = <?php echo csrf_token() ?>',
   dataType: 'json',
   async: false,
   success:function(data){
     takingCaller = data;
   }
  });
}


function reInitAnAmbulanceAmarker(ambulanceMarker, ambulance) {
  // New location
  newPosition = new google.maps.LatLng( ambulance.latitude, ambulance.longitude );
  ambulanceMarker.setPosition(newPosition);

  // New marker
  if(ambulance.status == AMBULANCE_STATUS_READY) {
    ambulanceMarker.setIcon(ambulanceReadyIconDir); 
  } else {
    ambulanceMarker.setIcon(ambulanceBuzyIconDir); 
  }

}

function showInfoBox(ambulance, takingCaller) {
  clearInfoBox();
  if(ambulance != null) {
    $("#ambulanceInfoBox").show();

    // Team
    $("#infobox_team").text('Kíp xe: ' + ambulance.team);
    $("#infobox_team").show();

    // Status
    status = ambulance.status;
    switch(status) {
      case AMBULANCE_STATUS_BUZY:
        statusVal = "đang làm nhiệm vụ";
        break;
      case AMBULANCE_STATUS_PENDING:
        statusVal = "đang chờ chấp nhận nhiệm vụ";
        break;
      case AMBULANCE_STATUS_PROBLEM:
        statusVal = "đang gặp sự cố";
        break;
      case AMBULANCE_STATUS_READY:
        statusVal = "Sẵn sàng";
        break;
      case AMBULANCE_STATUS_PICKED:
        statusVal = "Đã đón người gọi";
        break;
    }

    $("#infobox_status").text(statusVal);
    $("#infobox_status").show();

    // Caller
    if(takingCaller != null)
    if(ambulance.caller_taking_id != null) {
      // Distance
      if(ambulance.distance != null) {
        $("#inforbox_distance").text('Khoảng cách còn: ' + ambulance.distance);
        $("#inforbox_distance").show();
      }
      
      // Duration
      if(ambulance.duration != null) {
        $("#inforbox_duration").text('Thời gian còn: ' + ambulance.duration);
        $("#inforbox_duration").show();
      }
    }
    
  }
  $("#ambulanceInfoBox").show();
}

function clearInfoBox() {
  $("#infobox_team").hide();
  $("#infobox_status").hide();
  $("#inforbox_caller").hide();
  $("#inforbox_distance").hide();
  $("#inforbox_duration").hide();
}

