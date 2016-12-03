function onCancelDispatchClick() {
console.log(ambulanceList);

	// initNewMap();
	// clearAllMarkers();
	// initAmbulanceMarkerAfterLoad();
	// iniAMarker(emergencyCenterPos, emergencyCenterIcon, emergencyCenterTitle);
	// map.panTo(emergencyCenterPos);
}

function iniCallerForm(caller) {
	$('#phone').val(caller.phone);
    $('#injury_id').val(caller.injury_id);
    $('#symptom').val(caller.symptom);
	geocodeLatLng(geocoder, map, caller.latitude + ',' + caller.longitude, function(address) {
		$('#address').val(address);
	});
    $('#latitude').val(caller.latitude);
    $('#longitude').val(caller.longitude);
     
}

function onDispatchClick() {

	getListAmbulanceAndDispatch();	

}

function getListAmbulanceAndDispatch() {

	createListAmbulancePos();

	handleDispatch(listAmbulancePos, callerPos, function(){

		// Draw
		origin = new google.maps.LatLng(readyAmbulance.latitude, readyAmbulance.longitude);
		destination = new google.maps.LatLng(caller.latitude, caller.longitude);

		// Init ambulance marker
		iniAMarker(origin, ambulanceBuzyIconDir, 'On the way');

		calculateAndDisplayRoute(directionsService, directionsDisplay, origin, destination);

	});
}

// RETURN SHORTEST DISTANCE TO CALLER 
function handleDispatch(listAmbulancePos, callerPos, callback) {
    distanceMatrixService = new google.maps.DistanceMatrixService;
    distanceMatrixService.getDistanceMatrix({
    origins: listAmbulancePos,
    destinations: callerPos,
    travelMode: 'DRIVING',
    unitSystem: google.maps.UnitSystem.METRIC,
    avoidHighways: false,
    avoidTolls: false
  }, function(response, status) {
    if (status !== 'OK') {
      alert('Error was: ' + status);
    } else {   
       var tmpElementList = response.rows;
       console.log(tmpElementList);
      for (var i = 0; i < tmpElementList.length; i++) {
        if(tmpElementList[i].elements[0].status == 'OK') {
          ambulanceList[i].distanceMatrix = tmpElementList[i].elements[0];
        } else {
           ambulanceList.splice(i);
        }
      }

      // Sort
      ambulanceList = ambulanceList.slice(0);
      ambulanceList.sort(function(a,b) {
        return a.distanceMatrix.distance.value - b.distanceMatrix.distance.value;
      });
      
      // Return ready ambulance
      readyAmbulance = ambulanceList[0];    

      callback();
    }
  });
}

// Update ambulance
function updateAmbulance(ambuParam) {
	var data = JSON.stringify(ambuParam);
	$.ajax({
		data: data,
		type: "post",
		url: "ambulanceUpdate",
		async: false,
		success: function(data){
			//alert("Data Save: " + data);
		}
	});
}

function updateCaller(callerParam) {
	var data = JSON.stringify(callerParam);
	 $.ajax({
         data: data,
         type: "post",
         url: "callerUpdate",
         async: false,
         success: function(data){
              //alert("Data Save: " + data);
         }
	 });
}

function updateAfterMatch() {
	readyAmbulance.caller_taking_id = caller.id;
	readyAmbulance.status = 'buzy';

	caller.ambulance_user_id = readyAmbulance.user_id;
	caller.status = 'taked';

	updateAmbulance(readyAmbulance);
	updateCaller(caller);
}

function getListAmbulance() {
	// $.ajax({
	// 	type:'GET',
	// 	url:'ambulance',
	// 	data:'_token = <?php echo csrf_token() ?>',
	// 	dataType: 'json',
	// 	async: false,
	// 	success:function(data){
	// 		ambulanceList = data.ambulance;
	// 		console.log(data);
	// 	}
	// });
}

function createListAmbulancePos() {
	listAmbulancePos = [];
	var ambulanceTmpPos;
	for (var i = 0; i < ambulanceList.length; i++) {
		ambulanceTmpPos = new google.maps.LatLng(ambulanceList[i].latitude, ambulanceList[i].longitude);
		listAmbulancePos.push(ambulanceTmpPos);
	}
}





