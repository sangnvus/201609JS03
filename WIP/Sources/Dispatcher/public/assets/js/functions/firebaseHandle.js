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



var listStatus;


getAmbulanceFromFirebase();


// START get all callers
function getAmbulanceFromFirebase() {
	// Get caller database reference
	const dbRefAmbulances = firebase.database().ref().child('ambulances');

	// Get UI element
	const ulListAmbulance = document.getElementById('listAmbulance'); 

	// Handle when a caller added
	handleAmbulanceChange(dbRefAmbulances, ulListAmbulance);

}

// // START get all callers
// function getCallers() {
// 	// Get caller database reference
// 	const dbRefCallers = firebase.database().ref().child('callers');

// 	// Get UI element
// 	const ulListWaiting = document.getElementById('listWaiting'); 
// 	const ulListProcessing = document.getElementById('listProcessing'); 

// 	// Handle when a caller added
// 	handlerCallerChange(dbRefCallers, ulListWaiting, ulListProcessing);

// }


function handleAmbulanceChange(dbRef, ulListAmbulance, callback) {
	dbRef.on('child_added', snap => {
		// Get list caller object
		ambulanceObj = snap.val();
		
		// push ambulance object to list available ambulance
		ambulanceList.push(ambulanceObj); 
		
		addAmbulanceToUl(ulListAmbulance, ambulanceObj);

		// TODO : just change what change
		reInitAmbulanceMaker();
		
	});

	// Handle when a caller remove
	dbRef.on('child_removed', snap => {
		var liChangeId = '#' + 'liAmbulance' + snap.key;

		deleteLi(liChangeId);
		
		ambulanceObj = snap.val();

		// Remove from aumbulance list
		ambulanceList = ambulanceList.filter(function(el) { return el.id != ambulanceObj.id; }); 

		reInitAmbulanceMaker();

		// Set Noti if ambulance is unavailabe
		setUnAvailableAmbulanceNoti()

	});

	// Handle when a caller change
	dbRef.on('child_changed', snap => {
		var liChangeId = 'liAmbulance' + snap.key;

		ambulanceObj = snap.val();

		var index = ambulanceList.findIndex(x => x.id == ambulanceObj.id);

		ambulanceList[index] = ambulanceObj;

		editLi(liChangeId, ambulanceObj);

		//reInitAmbulanceMaker();
		//reInitAnAmbulanceAmarker(ambulanceObj, ambulanceMakers[index]);

		//TODO : 
		
		reInitAnAmbulanceAmarker(ambulanceMakers[index], ambulanceObj);


		// Set Noti if ambulance is unavailabe
		setUnAvailableAmbulanceNoti()

	});
}


// function handlerCallerChange(dbRef, ulListWaiting, ulListProcessing) {
// 	dbRef.on('child_added', snap => {
// 		// Get list caller object
// 		callerObj = snap.val();
// 		callerKey = snap.key;

// 		if(callerObj.status == sttWaiting) {
// 			//addCallerToUl(ulListWaiting, callerObj, callerKey);
// 		} else if(callerObj.status == sttProcessing) {
// 		//	addCallerToUl(ulListProcessing, callerObj, callerKey);
// 		}		
// 	});

// 	// Handle when a caller remove
// 	dbRef.on('child_removed', snap => {
// 		var liChangeId = '#' + snap.key + snap.val().status;
// 		deleteLi(liChangeId);
// 	});

// 	// Handle when a caller change
// 	dbRef.on('child_changed', snap => {
// 		// Get list caller object
// 		callerObj = snap.val();
// 		callerKey = snap.key;
		
// 		if(callerObj.status == sttWaiting) {
// 			var liChangeId = '#' + callerKey + sttProcessing;
// 			deleteLi(liChangeId);
// 			addCallerToUl(ulListWaiting, callerObj, callerKey);
// 		} else if(callerObj.status == sttProcessing) {
// 			var liChangeId = '#' + callerKey + sttWaiting;
// 			deleteLi(liChangeId);
// 			addCallerToUl(ulListProcessing, callerObj, callerKey);
// 		}		
// 	});
// }


function addAmbulanceToUl(ul, ambulanceObject) {
	// Add li to ul
	const liAmbulance = document.createElement('li');
	liAmbulance.id = 'liAmbulance' + ambulanceObject.id;
	liAmbulance.className += ' active';

	const a = document.createElement('a');
	a.href = "javascript:void(0)";
	a.onclick = function() {
		
		onClickLiAmbulance(ambulanceObject);
	}

	const pAmbulanceTeam = document.createElement('p');
	var pCallerNumberText = document.createTextNode('Đội : ' + ambulanceObject.team);
	pAmbulanceTeam.appendChild(pCallerNumberText);
	pAmbulanceTeam.appendChild(document.createElement('br'));

	const pLocation = document.createElement('p');
	var pLocationText = document.createTextNode('Vị trí : '+ ambulanceObject.latitude + ' | ' + ambulanceObject.longitude);
	pAmbulanceTeam.appendChild(pLocationText);

	const pStatus = document.createElement('p');

	switch(ambulanceObject.status) {
	    case AMBULANCE_STATUS_BUZY:
	        var pStatusText = document.createTextNode('Trạng thái : đang bận');
			pStatus.className += ' item-danger';
	        break;
	    case AMBULANCE_STATUS_READY:
	        var pStatusText = document.createTextNode('Trạng thái : sẵn sàng');
			pStatus.className += ' item-completed';
	        break;
        case AMBULANCE_STATUS_PENDING:
	        var pStatusText = document.createTextNode('Trạng thái : đang đợi');
			pStatus.className += ' item-danger';
	        break;
	    case AMBULANCE_STATUS_PROBLEM:
	        var pStatusText = document.createTextNode('Trạng thái : sự cố');
			pStatus.className += ' item-danger';
	        break;
	    default:
	        var pStatusText = document.createTextNode('Trạng thái : chưa đăng nhập');
			pStatus.className += ' item-danger';
	}

	pStatus.appendChild(pStatusText);

	liAmbulance.appendChild(a);
	a.appendChild(pAmbulanceTeam);
	a.appendChild(pLocation);
	a.appendChild(pStatus);


	ul.appendChild(liAmbulance);
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

function editLi(liID, ambulanceObject) {
	liAmbulance = document.getElementById(liID);
	liAmbulance.innerHTML = '';

	const a = document.createElement('a');
	a.href = "javascript:void(0)"
	a.onclick = function() {
		
		onClickLiAmbulance(ambulanceObject);
	}

	const pAmbulanceTeam = document.createElement('p');
	var pCallerNumberText = document.createTextNode('Đội : ' + ambulanceObject.team);
	pAmbulanceTeam.appendChild(pCallerNumberText);
	pAmbulanceTeam.appendChild(document.createElement('br'));

	const pLocation = document.createElement('p');
	var pLocationText = document.createTextNode('Vị trí : '+ ambulanceObject.latitude + ' | ' + ambulanceObject.longitude);
	pAmbulanceTeam.appendChild(pLocationText);

	const pStatus = document.createElement('p');

	switch(ambulanceObject.status) {
	    case AMBULANCE_STATUS_BUZY:
	        var pStatusText = document.createTextNode('Trạng thái : đang bận');
			pStatus.className += ' item-danger';
	        break;
	    case AMBULANCE_STATUS_READY:
	        var pStatusText = document.createTextNode('Trạng thái : sẵn sàng');
			pStatus.className += ' item-completed';
	        break;
        case AMBULANCE_STATUS_PENDING:
	        var pStatusText = document.createTextNode('Trạng thái : đang đợi');
			pStatus.className += ' item-danger';
	        break;
        case AMBULANCE_STATUS_PROBLEM:
	        var pStatusText = document.createTextNode('Trạng thái : sự cố');
			pStatus.className += ' item-danger';
	        break;
	    case 'picked':
	        var pStatusText = document.createTextNode('Trạng thái : Đã đón nạn nhân');
			pStatus.className += ' item-danger';
	        break;    
	    default:
	        var pStatusText = document.createTextNode('Trạng thái : chưa đăng nhập');
			pStatus.className += ' item-danger';
	}

	pStatus.appendChild(pStatusText);

	liAmbulance.appendChild(a);
	a.appendChild(pAmbulanceTeam);
	a.appendChild(pLocation);
	a.appendChild(pStatus);

}





//initUlCaller();

function initUlCaller() {
    var ulListWaiting = document.getElementById('listWaiting');
    var ulListProcessing = document.getElementById('listProcessing');

    var liTabWaiting = document.getElementById('tabWaiting');
    var liTabProcessing = document.getElementById('tabProcessing');

    ulListWaiting.style.display = 'block';
    liTabWaiting.className += ' active';

    ulListProcessing.style.display = 'none';
    liTabProcessing.className = '';
}


function getUl(status) {
    var ulListAmbulance = document.getElementById('listAmbulance');
    var ulListCallerWaiting = document.getElementById('listWaiting');
    var liAmbulance = document.getElementById('liAmbulance');
    var liCaller = document.getElementById('liCaller');
    
    if(status == 'ambulance') {
        ulListAmbulance.style.display = 'block';
        ulListCallerWaiting.style.display = 'none';
        liAmbulance.className += ' active';
        liCaller.className = '';
    } else if(status == 'caller') {
        ulListAmbulance.style.display = 'none';
        ulListCallerWaiting.style.display = 'block';
        liCaller.className += ' active';
        liAmbulance.className = '';
    }

}


// function getCallers(evt, status) {
//     var ulListWaiting = document.getElementById('listWaiting');
//     var ulListProcessing = document.getElementById('listProcessing');
//     var liTabWaiting = document.getElementById('tabWaiting');
//     var liTabProcessing = document.getElementById('tabProcessing');
    
//     if(status == 'waiting') {
//         ulListWaiting.style.display = 'block';
//         ulListProcessing.style.display = 'none';
//         liTabWaiting.className += ' active';
//         liTabProcessing.className = '';
//     } else if(status == 'processing') {
//         ulListWaiting.style.display = 'none';
//         ulListProcessing.style.display = 'block';
//         liTabProcessing.className += ' active';
//         liTabWaiting.className = '';
//     }

// }





// END





