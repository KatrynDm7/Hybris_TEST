ACC.payment = {

	bindUseThisSavedCardButton: function ()
	{
		$(document).on("click",'.use_this_saved_card_button', function ()
		{
			var paymentId = $(this).attr('data-payment');
			$.postJSON(setPaymentDetailsUrl, {paymentId: paymentId}, ACC.payment.handleSelectSavedCardSuccess);
			return false;
		});
	},

	handleSelectSavedCardSuccess: function (data)
	{
		if (data != null)
		{
			ACC.refresh.refreshPage(data);

			parent.$.colorbox.close();
		}
		else
		{
			alert("Failed to set payment details");
		}
	},

	refreshPaymentDetailsSection: function (data)
	{
		$('.summaryPayment').replaceWith($('#paymentSummaryTemplate').tmpl(data));

		//bind edit payment details button
		if (!typeof bindSecurityCodeWhatIs == 'undefined')
			bindSecurityCodeWhatIs();
	},
	
	showSavedPayments: function ()
	{
		$(document).on("click","#viewSavedPayments",function(){
			var data = $("#savedPaymentListHolder").html();
			$.colorbox({
				width: 500,
				height: false,
				html: data,
				onComplete: function ()
				{
					$(this).colorbox.resize();
				}
			});
			
		})
		
		
	},

	bindPaymentCardTypeSelect: function ()
	{
		ACC.payment.filterCardInformationDisplayed();
		$("#card_cardType").change(function ()
		{
			var cardType = $(this).val();
			if (cardType == '024' || cardType == 'switch')
			{
				$('#startDate, #issueNum').show();
			}
			else
			{
				$('#startDate, #issueNum').hide();
			}
		});
	},
	
    filterCardInformationDisplayed: function ()
	{
		var cardType = $('#card_cardType').val();
		if (cardType == '024' || cardType == 'switch')
		{
			$('#startDate, #issueNum').show();
		}
		else
		{
			$('#startDate, #issueNum').hide();
		}
	},
	
	clearBillingFields: function () {
        document.getElementById('address.line1').value = "";
        document.getElementById('address.line2').value = "";
        document.getElementById('address.townCity').value = "";
        document.getElementById('address.postcode').value = "";
        document.getElementById('address.country').value = "";
        if(document.getElementById('address.region')){ document.getElementById('address.region').value = ""};
        document.getElementById('billingAddressForm').textContent="";
    }
}

$(document).ready(function ()
{
	ACC.payment.bindPaymentCardTypeSelect();
	ACC.payment.showSavedPayments();
	if (!typeof bindSecurityCodeWhatIs == 'undefined')
	{
		bindSecurityCodeWhatIs();
	}
});
	
	
	
	
