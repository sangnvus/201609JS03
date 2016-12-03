function showSuccessNoti(content) {
	$("#divNoti").show();
	$("#divNavContent").hide();
	$("#divNoti").text(content);
	$("#divNoti").delay(2000).fadeOut(function() {
			$("#divNavContent").show();
	});
}


