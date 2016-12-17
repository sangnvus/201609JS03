function showNoti(type, content, time) {	
	$("#divNoti").removeClass();
	$("#divNoti").show();
	$("#divNavContent").hide();
	$("#divNoti").text(content);

	switch(type) {
		case NOTI_TYPE_SUCCESS:
			$("#divNoti").addClass("successNoti");
			if(time != 0) {
				$("#divNoti").delay(time).fadeOut(function() {
					$("#divNavContent").show();
				});	
			} else {
				$("#divNoti").show();
			}

			
			break;
		case NOTI_TYPE_ERROR:
			$("#divNoti").addClass("erroNoti");
			if(time != 0) {
				$("#divNoti").delay(time).fadeOut(function() {
					$("#divNavContent").show();
				});	
			} else {
				$("#divNoti").show();
			}

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


function confirmLogout() {

	showConfirmBox('Bạn có muốn logout', function(result) {
        if(result) {
         window.location.href = 'logout';
        }
    });
}

