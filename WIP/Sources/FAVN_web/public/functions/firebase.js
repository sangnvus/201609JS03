  // Initialize Firebase
  var config = {
    apiKey: "AIzaSyBioI0HSh4p-EY1qGgSOKcsRm7vQjyIuuE",
    authDomain: "favn-e63df.firebaseapp.com",
    databaseURL: "https://favn-e63df.firebaseio.com",
    storageBucket: "favn-e63df.appspot.com",
    messagingSenderId: "702762710809"
  };
  firebase.initializeApp(config);


// Get a reference to the database service


function writeUserData(userId, name, email, imageUrl) {
	database = firebase.database();
	firebase.database().ref('users/' + userId).set({
		username: name,
		email: email,
		profile_picture : imageUrl
	});
}

function setAnAmbulanceToFirebase(ambulance) {
	database = firebase.database();
	firebase.database().ref('ambulances/' + ambulance.id).set({
		id: ambulance.id,
		user_id: ambulance.user_id,
		team: ambulance.team,
		latitude: ambulance.latitude,
		longitude: ambulance.longitude,
		status: ambulance.status,
		caller_taking_id: ambulance.caller_taking_id
	});
}

function setAllAmbulanceToFirebase() {
	var ambulances = document.getElementById("ambulances").value;
    ambulances = JSON.parse(ambulances);
    for (var i = 0; i < ambulances.length; i++) {
    	setAnAmbulanceToFirebase(ambulances[i]);
    }
}