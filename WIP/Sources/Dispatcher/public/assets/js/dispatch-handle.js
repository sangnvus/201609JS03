
function onCancelDispatchClick() {
	clearAllMarkers();
	initAmbulanceMarkerAfterLoad();
	iniAMarker(emergencyCenterPos, emergencyCenterIcon, emergencyCenterTitle);
	map.panTo(emergencyCenterPos);
}

function iniCallerForm(caller) {
	$('#phone').val(caller.phone);
    $('#injury_id').val(caller.injury_id);
    $('#symptom').val(caller.symptom);
	geocodeLatLng(geocoder, map, caller.latitude + ',' + caller.longitude, setAddressCallerForm);
    $('#latitude').val(caller.latitude);
    $('#longitude').val(caller.longitude);
     
}

function setAddressCallerForm(address){
	$('#address').val(address);
}





