ACC.checkremovepayment = {

	bindAll: function ()
	{
		this.bindCheckRemovePaymentDetails();
	},

	bindCheckRemovePaymentDetails: function ()
	{
		$('.checkSubmitRemove-account').on("click", function (e)
		{
            e.preventDefault();
		    if(ACC.checkremovepayment.isSessionAlive('../'))
	    	{
	        	$self=$(this);
	        	$.colorbox({
	        		href:$self.data("url"),
	        		width:680,
	        		height:false,
	        		onComplete: function(){
	        			$.colorbox.resize();
	        		}
	        	});
	    	}
		    else
	    	{
	    		location.reload();
	    	}
		});
		
		$('.checkSubmitRemove-manage').on("click", function (e)
	    {
            e.preventDefault();
			if(ACC.checkremovepayment.isSessionAlive('../../'))
			{
				$self=$(this);
				$.colorbox({
					href:$self.data("url"),
					width:false,
					height:false,
					onComplete: function(){
						$.colorbox.resize();
					}
				});
			}
			else
			{
				location.reload();
			}
				});
	},
	isSessionAlive: function(prefix) 
	{
   		var isAlive;
    	$.ajax({
			    url : prefix + "my-account/is-alive",
			    type: "GET",
			    async: false,
			    success: function(data, textStatus, jqXHR)
			    {
			        if(data == 'alive')
			        	{	
			        		isAlive = true;
			        	}
			        else
			        	{
				      	isAlive = false;
			        	}
			    },
			    error: function (jqXHR, textStatus, errorThrown)
			    {
			    	isAlive = false;
			    }
			});   
   		
   		return isAlive;
   }
	
};

$(document).ready(function ()
{ 
	ACC.checkremovepayment.bindAll();
});
