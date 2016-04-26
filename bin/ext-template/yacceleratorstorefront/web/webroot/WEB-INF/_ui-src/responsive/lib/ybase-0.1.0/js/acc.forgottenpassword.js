ACC.forgottenpassword = {

	_autoload: [
		"bindLink"
	],

	bindLink: function(){
		$(document).on("click",".js-password-forgotten",function(e){
			e.preventDefault();

			ACC.colorbox.open(
				$(this).data("cboxTitle"),
				{
					href: $(this).attr("href"),
					width:"350px",
					onOpen: function()
					{
						$('#validEmail').remove();
					},
					onComplete: function(){
						$('form#forgottenPwdForm').ajaxForm({
							success: function(data)
							{
								if ($(data).closest('#validEmail').length)
								{
									
									if ($('#validEmail').length === 0)
									{
										$(".forgotten-password").replaceWith(data);
										ACC.colorbox.resize();
									}
								}
								else
								{
									$("#forgottenPwdForm .control-group").replaceWith($(data).find('.control-group'));
									ACC.colorbox.resize();
								}
							}
						});
					}
				}
			);
		});
	}

};