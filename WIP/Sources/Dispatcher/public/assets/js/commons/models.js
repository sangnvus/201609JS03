function Ambulance(id, team, latitude, longitude, status, caller_taking_id, distanceMatrix) {
   this.id = id;
   this.team = team;
   this.latitude = latitude;
   this.longitude = longitude;
   this.status = status;
   this.caller_taking_id = caller_taking_id;
   this.distanceMatrix = distanceMatrix;
}

function Caller(id, phone, injury_id, symptom, latitude, longitude, status, dispatcher_user_id, ambulance_user_id) {
	this.id = id; 
	this.phone = phone;
	this.injury_id = injury_id;
	this.symptom = symptom;
	this.latitude = latitude;
	this.longitude = longitude;
	this.status = status;
	this.dispatcher_user_id = dispatcher_user_id;
	this.ambulance_user_id = ambulance_user_id;
}


