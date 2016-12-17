function checkNoti() {
	var sessionNoti = document.getElementById("sessionNoti").value;

	switch(sessionNoti) {
		case 'noAmbulance':
			showAlertBox('hết xe cứu thương !');
			//var sessionCaller = document.getElementById("sessionCaller").value;
			//alert(sessionCaller);
			break;
		case 'noCaller':
			showAlertBox('không tồn tại người gọi !');
			break;
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

function onCancelDispatchClick() {
	if(caller != null) {
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
	    	showNoti('success', 'Đã hủy điều phối xe', 2000);
	    	clearAMaker(callerMaker);
	    	initNewMap();
	    	reInitDefaultMarkers();
	    	clearCallerForm();
	    } else {
	    	showNoti('success', 'Lỗi kết nối, chưa hủy được xe', 2000);
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
    $('#caller_id').val(caller.id);
}

function onDispatchClick() {
	$( "#form_caller" ).submit();
	// clearAllCurrentObject();
	// if(caller == null) {
	// 	showAlertBox('Chưa khởi tạo trường hợp khẩn cấp');
	// } else {
	// 	$( "#form_caller" ).submit();

	// 	// -----------------
	// 	// TODO :
	// 	//getListAmbulanceAndDispatch();		
	// }
}

function clearAllCurrentObject() {
	readyAmbulanceList = [];
	readyAmbulance = null;
	listAmbulancePos = [];
}

function getListAmbulanceAndDispatch() {

	createListAmbulanceReady(ambulanceList);

	// Check if has | does not have any ready ambulance
	if(readyAmbulanceList.length == 0) {
		showConfirmBox('hết xe, xếp người gọi vào hàng chờ', function(result) {
			if (result) {
				showAlertBox('đã xếp');
				return;
			} else {
				showAlertBox('Không xếp');
				return;
			}
		});
		return;
	} 

	createListAmbulancePos();


 	getReadyAmbulance(listAmbulancePos, callerPos, function(){

 		// Check if can get ambulance that ready to serve
 		if(readyAmbulance == null) {
 			showConfirmBox('tìm đường không thành công, xếp người gọi vào hàng chờ', function(result) {
				if (result) {
					showAlertBox('đã xếp');
					return;
				} else {
					showAlertBox('Không xếp');
					return;
				}
			});
 			return;
 		}

		sendTaskToAmbulance(readyAmbulance, function(status) {
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
						showAlertBox('Đã hủy');
					}

				});
			}
		});
	});
}

function disableCallerForm() {
	$('#phone').attr('disabled', true);
    $('#injury_id').attr('disabled', true);
    $('#symptom').attr('disabled', true);
	$('#address').attr('disabled', true);
    $('#latitude').attr('disabled', true);
    $('#longitude').attr('disabled', true);
}

function enableCallerForm() {
	$('#phone').removeAttr('disabled', true);
    $('#injury_id').removeAttr('disabled', true);
    $('#symptom').removeAttr('disabled', true);
	$('#address').removeAttr('disabled', true);
    $('#latitude').removeAttr('disabled', true);
    $('#longitude').removeAttr('disabled', true);
}

function drawCallerAmbulancePatch(ambulance, caller) {

	// Draw
	var ambulancePos = new google.maps.LatLng(ambulance.latitude, ambulance.longitude);
	var callerPos = new google.maps.LatLng(caller.latitude, caller.longitude);

	// Init ambulance marker
	iniAMarker(ambulancePos, ambulanceBuzyIconDir, 'xe đội ' + ambulance.team);

	// Init caller marker
	iniAMarker(callerPos, callerIconDir, 'Caller: ' + caller.phone);

	calculateAndDisplayRoute(directionsService, directionsDisplay, ambulancePos, callerPos, function(response) {
		console.log(response);
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
      	
        if(tmpElementList[i].elements[0].status !== 'OK') {
        	 readyAmbulanceList.splice(i, 1);
        	 tmpElementList.splice(i, 1);
        	 i--;
        } else {
          readyAmbulanceList[i].distanceMatrix = tmpElementList[i].elements[0];
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


function sendTaskToAmbulance(readyAmbulance, callback) {
	showDialogPending('Đang đợi nối xe...', function(result) {

	});

	clearCallerForm();

	// Handle when a caller change
	database.ref('ambulances/' + readyAmbulance.id).on('child_changed', snap => {
		status = snap.val();
		callback(status);
	});
}

function pendingAmbulance(readyAmbulance, callback) {
	var database = firebase.database();
	showDialogPending('Đang đợi nối xe...', function(result) {
		if(result) {

		} else {
			releaseAmbulance();
			releaseCaller();
		}
	});	
	// Handle when a caller change
	database.ref('ambulances/' + readyAmbulance.id).on('child_changed', snap => {
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

function createListAmbulanceReady(ambulanceList) {
	for (var i = 0; i < ambulanceList.length; i++) {
		if(ambulanceList[i].latitude == null || ambulanceList[i].longitude == null) {
			continue;
		}
		if (ambulanceList[i].status == AMBULANCE_STATUS_READY) {
			readyAmbulanceList.push(ambulanceList[i]);
		}
	}	
}

function createListAmbulancePos() {
	listAmbulancePos = [];
	var ambulanceTmpPos;
	for (var i = 0; i < readyAmbulanceList.length; i++) {
		ambulanceTmpPos = new google.maps.LatLng(readyAmbulanceList[i].latitude, readyAmbulanceList[i].longitude);
		listAmbulancePos.push(ambulanceTmpPos);
	}
}

function releaseAmbulance() {
	
}

function releaseCaller() {
	
}


function callCanCelDispatcheService(caller_id) {
	$.ajax({
		type: "get",
		url: "canceldispatchservice/" + caller_id,
		async: false
	});
}

function isAvailableListAmbulance(callback) {
	$.ajax({
		type:'GET',
		url:'isavailableambulance',
		async: false,
		success:function(result){
			callback(result);
		}
	});
}

function setUnAvailableAmbulanceNoti() {
	isAvailableListAmbulance(function(result) {
		console.log(result);
		if(result == false) {
			if(IS_AVAILABLE_AMBULANCE) {
				showNoti(NOTI_TYPE_ERROR, 'Hiện tại đã hết xe', 0);
				IS_AVAILABLE_AMBULANCE = false;
			}
		} else {
			if(!IS_AVAILABLE_AMBULANCE) {
				showNoti(NOTI_TYPE_SUCCESS, 'Đã có xe sẵn sàng', 2000);
				IS_AVAILABLE_AMBULANCE = true;
			}
		}
	});
}



