function Ambulance(id, user_id, team, latitude, longitude) {
   this.id = id;
   this.user_id = user_id;
   this.team = team;
   this.latitude = latitude;
   this.longitude = longitude;
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