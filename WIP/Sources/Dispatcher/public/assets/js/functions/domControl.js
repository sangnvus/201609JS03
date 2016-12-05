function showNoti(type, content) {	
	$("#divNoti").removeClass();
	$("#divNoti").show();
	$("#divNavContent").hide();
	$("#divNoti").text(content);

	switch(type) {
		case NOTI_TYPE_SUCCESS:
			$("#divNoti").addClass("successNoti");
			$("#divNoti").delay(2000).fadeOut(function() {
				$("#divNavContent").show();
			});
			break;
		case NOTI_TYPE_ERROR:
			$("#divNoti").addClass("erroNoti");
			$("#divNoti").delay(2000).fadeOut(function() {
				$("#divNavContent").show();
			});
			break;
		case NOTI_TYPE_PENDING:
			$("#divNoti").addClass("pendingNoti");
			break;
	}
}

function clearCallerForm() {
	$('#phone').val('');
    $('#injury_id').val(0);
    $('#symptom').val('');
	$('#address').val('');
    $('#latitude').val('');
    $('#longitude').val('');
}

function showAlertBox(content) {
	bootbox.alert({ 
        size: "small",
        message: content,
        closeButton: false,
    })
}

function showConfirmBox(content, callback) {
	bootbox.confirm({ 
        size: "small",
        message: content,
        buttons: {
        	cancel: {
				label: 'Hủy',
			},
			confirm: {
				label: 'Xác nhận',
			}	
        } ,
        closeButton: false,
        callback: function(result){
        	callback(result);
        }
    })
}

function showDialogPending(content) {
	bootbox.dialog({
		message: '<div class="text-center"><i class="fa fa-spin fa-spinner"></i>' + content + '</div>',
		buttons: {
			cancel: {
				label: 'Hủy nối xe',
			},
			confirm: {
				label: 'Nối xe khác',
			}			
		},
		closeButton: false,
		callback: function (result) {
			
		}
	});
}

function closeNotiBox() {
	bootbox.hideAll();
}
