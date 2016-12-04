function onCancelDispatchClick() {	
	if(caller != undefined) {
		bootbox.confirm({ 
	        size: "small",
	        message: "Bạn có hủy điều phối ?", 
	        callback: function(result){
	        	if (result) {
	        		cancelDispatch(caller);
	        	}
	        }
	    })
	}
}


function cancelDispatch(caller) {
	caller.status = 'cancel';
	updateCaller(caller, function(result) {
		if(result) {
	    	showNoti('success', 'Đã hủy điều phối xe');
	    	clearAMaker(callerMaker);
	    	initNewMap();
	    	reInitDefaultMarkers();
	    	clearCallerForm();
	    } else {
	    	showNoti('success', 'Lỗi kết nối, chưa hủy được xe');
	    }
	});
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

	getReadyAmbulance(listAmbulancePos, callerPos, function(){

		readyAmbulance.status = 'pending'
		readyAmbulance.caller_taking_id = caller.id;

		pendingCallers.push(caller);
		console.log(pendingCallers);
		caller = null;

		var database = firebase.database();
		sendTaskToAmbulance(readyAmbulance, function(status) {
			console.log(status);
		});

// -----------------------------------

		// // Draw
		// var ambulancePos = new google.maps.LatLng(readyAmbulance.latitude, readyAmbulance.longitude);
		// var callerPos = new google.maps.LatLng(caller.latitude, caller.longitude);

		// // Init ambulance marker
		// iniAMarker(ambulancePos, ambulanceBuzyIconDir, 'xe đội ' + readyAmbulance.team);

		// calculateAndDisplayRoute(directionsService, directionsDisplay, ambulancePos, callerPos, function(response) {
		// 	console.log(response);
		// });

	});
}


function implementDispatch() {

}

// RETURN SHORTEST DISTANCE TO CALLER 
function getReadyAmbulance(listAmbulancePos, callerPos, callback) {
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
      for (var i = 0; i < tmpElementList.length; i++) {
        if(tmpElementList[i].elements[0].status == 'OK') {
          readyAmbulanceList[i].distanceMatrix = tmpElementList[i].elements[0];
        } else {
           readyAmbulanceList.splice(i);
        }
      }

      // Sort
      readyAmbulanceList = readyAmbulanceList.slice(0);
      readyAmbulanceList.sort(function(a,b) {
        return a.distanceMatrix.distance.value - b.distanceMatrix.distance.value;
      });
      
      // Return ready ambulance
      readyAmbulance = readyAmbulanceList[0];    

      callback();
    }
  });
}


function sendTaskToAmbulance(ambulance, callback) {
	var database = firebase.database();
	setAnAmbulanceToFirebase(ambulance, database);

	showNoti(NOTI_TYPE_PENDING, 'đang đợi xe nhận nhiệm vụ');

	clearCallerForm();

	// Handle when a caller change
	database.ref('ambulances/' + ambulance.id).on('child_changed', snap => {
		status = snap.val();
		callback(status);
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

function updateCaller(callerParam, callback) {
	try{
		var data = JSON.stringify(callerParam);
		$.ajax({
			data: data,
			type: "post",
			url: "callerUpdate",
			async: false,
			success: function(data){
				callback(true);
			},
			error: function() {
				callback(false);
			}
		});
	} catch(err) {
		callback(false);
	}
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

function createListAmbulanceReady(ambulanceList, callback) {
	for (var i = 0; i < ambulanceList.length; i++) {
		if (ambulanceList[i].status == AMBULANCE_STATUS_READY) {
			readyAmbulanceList.push(ambulanceList[i]);
		}
	}
	callback();
}

function createListAmbulancePos() {
	createListAmbulanceReady(ambulanceList, function() {
		listAmbulancePos = [];
		var ambulanceTmpPos;
		for (var i = 0; i < readyAmbulanceList.length; i++) {
			ambulanceTmpPos = new google.maps.LatLng(readyAmbulanceList[i].latitude, readyAmbulanceList[i].longitude);
			listAmbulancePos.push(ambulanceTmpPos);
		}
		console.log(readyAmbulanceList);
		console.log(listAmbulancePos);
	});
	
	
	
}





