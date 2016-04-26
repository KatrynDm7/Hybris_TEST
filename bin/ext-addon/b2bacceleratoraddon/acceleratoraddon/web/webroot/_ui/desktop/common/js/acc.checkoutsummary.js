ACC.checkoutSummary = {
	bindScheduleReplenishment: function(data){
			 
			$(document).on("click",'#placeOrder .scheduleReplenishmentButton', function (e){
				e.preventDefault();

				$("#placeOrder .requestQuoteButton").attr('disabled', 'disabled');
				$(".place-order").attr('disabled', 'disabled');
				$("#placeOrder .scheduleReplenishmentButton").addClass('pressed');
				$('#replenishmentSchedule').show();
				ACC.checkoutSummary.replenishmentInit();
			});

			$(document).on("click",'#replenishmentSchedule #placeReplenishmentOrder', function (e){
				e.preventDefault();
				$(".replenishmentOrderClass").val(true);
				var securityCode = $("#SecurityCodePayment").val();
				$(".securityCodeClass").val(securityCode);
				$("#placeOrderForm1").submit();
			});

			$(document).on("click",'#replenishmentSchedule #cancelReplenishmentOrder', function (e){
				e.preventDefault();
				if (typeof cartDataQuoteAllowed !== 'undefined' && cartDataQuoteAllowed) {
					$("#placeOrder .requestQuoteButton").removeAttr('disabled');
				}
				$(".place-order").removeAttr('disabled');
				$("#placeOrder .scheduleReplenishmentButton").removeClass('pressed');
				$('#replenishmentSchedule').hide();
				$(".replenishmentOrderClass").val(false);
			});

			$(document).on("click",'#replenishmentSchedule .replenishmentfrequencyD, #replenishmentSchedule .replenishmentfrequencyW, #replenishmentSchedule .replenishmentfrequencyM', function (e){
				$('.scheduleform').removeClass('selected');
				$(this).parents('.scheduleform').addClass('selected');
			});	

			var placeOrderFormReplenishmentOrder = $('#replenishmentSchedule').data("placeOrderFormReplenishmentOrder")

			if(placeOrderFormReplenishmentOrder){
				$('#placeOrder .scheduleReplenishmentButton').click()
			}
		
	},

	replenishmentInit: function ()
	{
		var dateForDatePicker = $('#replenishmentSchedule').data("dateForDatePicker");
		var placeOrderFormReplenishmentRecurrence = $('#replenishmentSchedule').data("placeOrderFormReplenishmentRecurrence");
		var placeOrderFormNDays = $('#replenishmentSchedule').data("placeOrderFormNDays");
		var placeOrderFormNthDayOfMonth = $('#replenishmentSchedule').data("placeOrderFormNthDayOfMonth");
		var placeOrderFormNegotiateQuote = $('#replenishmentSchedule').data("placeOrderFormNegotiateQuote");
		var placeOrderFormReplenishmentOrder = $('#replenishmentSchedule').data("placeOrderFormReplenishmentOrder");
		
		$("input:radio[name='replenishmentRecurrence'][value=" + placeOrderFormReplenishmentRecurrence + "]").attr('checked', true);
		$("input:radio[name='replenishmentRecurrence'][value=" + placeOrderFormReplenishmentRecurrence + "]").parents('.scheduleform').addClass('selected');
		$("#nDays option[value=" + placeOrderFormNDays + "]").attr('selected', 'selected');
		
		if (placeOrderFormNthDayOfMonth != '')		
			$("#daysoFWeek option[value=" + placeOrderFormNthDayOfMonth + "]").attr('selected', 'selected');
			
		$("#replenishmentStartDate").datepicker({dateFormat: dateForDatePicker});
		$("#replenishmentStartDate").datepicker("option", "appendText", dateForDatePicker);	
	},

	bindRequestQuoteForm: function() {
		$(document).on("click",'#placeOrder .requestQuoteButton', function (e) {
			e.preventDefault();
			ACC.checkoutSummary.displayRequestQuoteDiv();
		});

		$(document).on("click",'#requestQuote #cancelRequestQuote', function (e) {
			e.preventDefault();
			$("#placeOrder .scheduleReplenishmentButton").removeAttr('disabled');
			$("#placeOrder .place-order").removeAttr('disabled');
			$("#placeOrder .requestQuoteButton").removeClass('pressed');
			$('#requestQuote').hide();
			$(".requestQuoteClass").val(false);
		});
	},
	
	displayRequestQuoteDiv: function() {
		$("#placeOrder .scheduleReplenishmentButton").attr('disabled', 'disabled');
		$("#placeOrder .place-order").attr('disabled', 'disabled');
		$("#placeOrder .requestQuoteButton").addClass('pressed');
		$('#requestQuote').show();
		$(".requestQuoteClass").val(true);
	}
};

$(document).ready(function () {
	ACC.checkoutSummary.bindScheduleReplenishment();
	// if quote allowed, bind it
	if(typeof cartDataQuoteAllowed !== 'undefined' && cartDataQuoteAllowed) {
		// only enable quote button if replenishment div is not showing
		if (!$("#placeOrder .scheduleReplenishmentButton").hasClass('pressed')) {
			$("#placeOrder .requestQuoteButton").removeAttr('disabled');
		}
		ACC.checkoutSummary.bindRequestQuoteForm();
	} else {
		$("#placeOrder .requestQuoteButton").attr('disabled', 'disabled');
	}
	
	if (typeof negotiateQuote !== 'undefined' && negotiateQuote) {
		ACC.checkoutSummary.displayRequestQuoteDiv();
	}
});