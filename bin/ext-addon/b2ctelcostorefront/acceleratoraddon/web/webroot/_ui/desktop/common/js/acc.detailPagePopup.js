ACC.detailPagePopup = {

    bindAll: function ()
    {
        if($('.prod_add_to_cart').length>0){

            $('.prod_add_to_cart a.details').colorbox({
                inline: true,
                width:"500px",
                height:"440px",
                onComplete: function(){
                    $(this).colorbox.resize();
                    $('#cboxClose').attr('tabindex', -1).focus();
                    $("#cboxClose").bind('keyup',function(event){
                        if(event.keyCode == 13){
                            $("#cboxClose").click();
                        }
                    });
                },
                onCleanup:  function(){
                    $("#cboxClose").unbind( 'keyup', false );

                    var popupId = $(this).attr('href'),
                        idWrapper = $(this).next();
                    idWrapper.empty();
                    $(popupId).appendTo(idWrapper);
                }

            });

            $('.prod_add_to_cart a.details').keypress(function(e) {
                var keyCode;
                if (window.event) keyCode = window.event.keyCode;
                else if(e) keyCode = e.which;
                else return true;
                if (keyCode == 13 || keyCode == 32) {
                    $(this).click();
                    return false;
                } else return true;
            });
        }
    }
};

$(document).ready(function ()
{
    ACC.detailPagePopup.bindAll();
});

