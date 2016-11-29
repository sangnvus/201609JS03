
initUlCaller();

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

function getCallers(evt, status) {
    var ulListWaiting = document.getElementById('listWaiting');
    var ulListProcessing = document.getElementById('listProcessing');
    var liTabWaiting = document.getElementById('tabWaiting');
    var liTabProcessing = document.getElementById('tabProcessing');
    
    if(status == 'waiting') {
        ulListWaiting.style.display = 'block';
        ulListProcessing.style.display = 'none';
        liTabWaiting.className += ' active';
        liTabProcessing.className = '';
    } else if(status == 'processing') {
        ulListWaiting.style.display = 'none';
        ulListProcessing.style.display = 'block';
        liTabProcessing.className += ' active';
        liTabWaiting.className = '';
    }

}

