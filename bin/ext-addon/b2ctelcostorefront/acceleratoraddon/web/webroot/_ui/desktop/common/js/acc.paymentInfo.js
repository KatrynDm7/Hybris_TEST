$(document).ready(function () 
{
   $(document).on("click", "#editWithSubscriptions-account", function(){
	
    	if(isSessionAlive('../'))
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
   })
   
   $(document).on("click", "#editWithSubscriptions-manage", function(e){

       e.preventDefault();
	   if(isSessionAlive('../../'))
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

   $(document).on("click", "#payment-method-subscriptions .r_action_btn", function(e){
       e.preventDefault();
       $.colorbox.close();
   });
   
   $(document).on("click", "#payment-method-delete .r_action_btn", function(e){
       e.preventDefault();
       $.colorbox.close();
   });
   
   function isSessionAlive(prefix) {
   		var isAlive;
    	$.ajax({
			    url : prefix +"my-account/is-alive",
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
   
   function submitSetDefault(id) {
		document.getElementById('setDefaultPaymentDetails'+id).submit();
   }
});