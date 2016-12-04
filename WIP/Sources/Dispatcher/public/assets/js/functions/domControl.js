function showNoti(type, content) {	
	$("#divNoti").show();

	switch(type) {
		case NOTI_TYPE_SUCCESS:
			$("#divNoti").addClass("successNoti");
			break;
		case NOTI_TYPE_ERROR:
			$("#divNoti").addClass("erroNoti");
			break;
		case NOTI_TYPE_PENDING:
			$("#divNoti").addClass("pendingNoti");
			break;
	}

	$("#divNavContent").hide();
	$("#divNoti").text(content);
	$("#divNoti").delay(2000).fadeOut(function() {
	$("#divNavContent").show();
	});
}

function clearCallerForm() {
	$('#phone').val('');
    $('#injury_id').val(0);
    $('#symptom').val('');
	$('#address').val('');
    $('#latitude').val('');
    $('#longitude').val('');
}

