$(document).ready(function(){
	
	disableMessage();
	
	$('select[id="status"]').change(function() {
		disableMessage();
	});

	function disableMessage(){
		var currentTiecketStatus = $('input[id="currentTicketStatus"]').val();
		var selectedStatus = $('select[id="status"]').val();
		
		
			if(currentTiecketStatus == 'COMPLETED' && selectedStatus == 'COMPLETED') {
				$('textarea[id="message"]').attr('disabled','disabled');
				$('button[id="updateTicket"]').attr('disabled','disabled');
			} else {
				$('textarea[id="message"]').removeAttr('disabled');
				$('button[id="updateTicket"]').removeAttr('disabled');
			}
	}
 });