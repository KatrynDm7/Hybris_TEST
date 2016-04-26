ACC.paymentType = {

	bindPaymentTypeSelect: function ()
	{
		$("#paymentTypeSelect").change(function()
		{
			ACC.paymentType.showCostCenterSelect();
		});
	},

	showCostCenterSelect: function() {
		var paymentTypeSelected = $("#paymentTypeSelect").val();
		if(paymentTypeSelected == "ACCOUNT") {
			$("#costCenter").show();
		} else {
			$("#costCenter").hide();
		}
	}
}


$(document).ready(function ()
{
	ACC.paymentType.showCostCenterSelect();
	ACC.paymentType.bindPaymentTypeSelect();
});
	
	
	
	
