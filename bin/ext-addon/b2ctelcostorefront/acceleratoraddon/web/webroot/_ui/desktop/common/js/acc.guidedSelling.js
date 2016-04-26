ACC.guidedSelling = {

    bindAll: function ()
    {
        this.addAddons();
        this.dashboardMoreThenThreeLines();
        this.addDevice();
    },

    addAddons: function ()
    {
        if($('#box_expanded').length>0){
            var e=document.getElementById("refreshed");
            if(e.value=="no") {
                e.value="yes";
            }
            else {
                e.value="no";
                location.reload();
            };



            if ($("#globalMessages div.span-24 div").hasClass("negative")) {
                $(document).scrollTop(0).focus();
            }
            else {

                $(document).scrollTop( $("#box_expanded").focus().offset().top );
            };
        }
    },

    dashboardMoreThenThreeLines: function ()
    {
        if($('.dashboard').length>0){
            $('.dashboard div.dashboard-item:nth-child(3n)').addClass('row_extras');
            var count = $('.row_extras .dashboard-item-row-container').children().length;
            $('.row_extras .dashboard-item-row-container div.dashboard-item-row').hide();
            $('.row_extras .dashboard-item-row-container div.dashboard-item-row:lt(3)').show();
            var count_rest = count-3;
            if (count_rest > 0) {
                $('.row_extras').append('<div id="showall_extras"><div class="showall_extras_showing">Showing 3 of '+count+' Items</div><a href="#" id="showall_extras_showall">Show All</a></div>');
                $('.row_extras').append('<div id="hideall_extras"><a href="#" id="hideall_extras_hideall">Hide All</a></div>');
                $('#hideall_extras').hide();
                $('#showall_extras_showall').click(function() {
                    $('.row_extras .dashboard-item-row-container div.dashboard-item-row').show();
                    $('#showall_extras').hide();
                    $('#hideall_extras').show();
                });
                $('#hideall_extras_hideall').click(function() {
                    $('.row_extras .dashboard-item-row-container div.dashboard-item-row').hide();
                    $('.row_extras .dashboard-item-row-container div.dashboard-item-row:lt(3)').show();
                    $('#showall_extras').show();
                    $('#hideall_extras').hide();
                });
            }
        }
    },

    addDevice: function (){
        if($('.select-device').length>0) {
            $(".delayed-button").removeAttr("disabled")
            $(".delayed-button").click(function(event){
                event.preventDefault();
                var $this=$(this);
                $this.attr("disabled", "disabled").addClass("disabled").html("Please wait");

                setTimeout(function() {
                    $this.parents("form").submit();
                }, 200);
            });
        }
    }


};

$(document).ready(function ()
{
    ACC.guidedSelling.bindAll();
});