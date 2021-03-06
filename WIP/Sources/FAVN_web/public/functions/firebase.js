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
	if(ambulance.isDeleted == 1) {
		firebase.database().ref('ambulances/' + ambulance.id).set(null);
	} else {
		firebase.database().ref('ambulances/' + ambulance.id).set(ambulance);
	}
}

function setAllAmbulanceToFirebase() {
	var ambulances = document.getElementById("ambulances").value;
    ambulances = JSON.parse(ambulances);
    for (var i = 0; i < ambulances.length; i++) {
    	setAnAmbulanceToFirebase(ambulances[i]);
    }
}