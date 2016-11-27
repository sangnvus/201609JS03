// START Initialize Firebase
var config = {
	apiKey: "AIzaSyBioI0HSh4p-EY1qGgSOKcsRm7vQjyIuuE",
	authDomain: "favn-e63df.firebaseapp.com",
	databaseURL: "https://favn-e63df.firebaseio.com",
	storageBucket: "favn-e63df.appspot.com",
	messagingSenderId: "702762710809"
};
firebase.initializeApp(config);
// END 



// ------------------------------------------------------------------------------------

const sttWaiting = 'waiting';
const sttProcessing = 'processing';
var listStatus;


getCallers();

// START get all callers
function getCallers() {
	// Get caller database reference
	const dbRefCallers = firebase.database().ref().child('callers');

	// Get UI element
	const ulListWaiting = document.getElementById('listWaiting'); 
	const ulListProcessing = document.getElementById('listProcessing'); 

	// Handle when a caller added
	handlerCallerChange(dbRefCallers, ulListWaiting, ulListProcessing);

}


function handlerCallerChange(dbRef, ulListWaiting, ulListProcessing) {
	dbRef.on('child_added', snap => {
		// Get list caller object
		callerObj = snap.val();
		callerKey = snap.key;

		if(callerObj.status == sttWaiting) {
			addCallerToUl(ulListWaiting, callerObj, callerKey);
		} else if(callerObj.status == sttProcessing) {
			addCallerToUl(ulListProcessing, callerObj, callerKey);
		}		
	});

	// Handle when a caller remove
	dbRef.on('child_removed', snap => {
		var liChangeId = '#' + snap.key + snap.val().status;
		deleteLi(liChangeId);
	});

	// Handle when a caller change
	dbRef.on('child_changed', snap => {
		// Get list caller object
		callerObj = snap.val();
		callerKey = snap.key;
		
		if(callerObj.status == sttWaiting) {
			var liChangeId = '#' + callerKey + sttProcessing;
			deleteLi(liChangeId);
			addCallerToUl(ulListWaiting, callerObj, callerKey);
		} else if(callerObj.status == sttProcessing) {
			var liChangeId = '#' + callerKey + sttWaiting;
			deleteLi(liChangeId);
			addCallerToUl(ulListProcessing, callerObj, callerKey);
		}		
	});
}

function addCallerToUl(ul, callerObject, callerKey){
		// Add li to ul
		const li = document.createElement('li');
		li.id = callerKey + callerObject.status;
		li.className += ' active';

		const a = document.createElement('a');
		a.href = "javascript:void(0)"
		a.onclick = function() {
			$("#phone").val(callerObject.phone);
			$("#injury_id").val(callerObject.injuryId);
			$("#address").val('Tọa độ : ' + callerObject.latitude + ' | ' + callerObject.longitude);
			$("#latitude").val(callerObject.latitude);
			$("#longitude").val(callerObject.longitude);

		}

		const pCallerNumber = document.createElement('p');
		var pCallerNumberText = document.createTextNode('Người gọi : ' + callerObject.phone);
		pCallerNumber.appendChild(pCallerNumberText);
		pCallerNumber.appendChild(document.createElement('br'));

		const pLocation = document.createElement('p');
		var pLocationText = document.createTextNode('Vị trí : '+ callerObject.latitude + ' | ' + callerObject.longitude);
		pCallerNumber.appendChild(pLocationText);

		const pStatus = document.createElement('p');
		if(callerObject.status == sttWaiting) {
			var pStatusText = document.createTextNode('Trạng thái : đang đợi');
			pStatus.className += ' item-danger';
		} else if(callerObject.status == sttProcessing) {
			var pStatusText = document.createTextNode('Trạng thái : đang xử lý');
			pStatus.className += ' item-completed';
		}
		pStatus.appendChild(pStatusText);

		li.appendChild(a);
		a.appendChild(pCallerNumber);
		a.appendChild(pLocation);
		a.appendChild(pStatus);


		ul.appendChild(li);
}

function deleteLi(liChangeId){
	$(liChangeId).remove();
}





// END




