ACC.producttocarttabs = {

    bindAll: function ()
    {
        if($('.prod_add_to_cart').length>0){

            // only load review one at init
            ACC.producttocarttabs.productTabs = $('.prod_add_to_cart .tabs').accessibleTabs({
                wrapperClass: 'content',
                currentClass: 'current',
                tabhead: 'h2',
                fx:'show',
                fxspeed: null,
                currentInfoText: 'current tab: ',
                currentInfoPosition: 'prepend',
                currentInfoClass: 'current-info',
                autoAnchor:true
            });

            $(".prod_add_to_cart .subTabs").accessibleTabs({
                wrapperClass: 'content',
                currentClass: 'sub-current',
                tabhead:'h3',
                tabbody:'.sub-tabbody',
                fx:'show',
                fxspeed: null,
                currentInfoText: 'current tab: ',
                currentInfoPosition: 'prepend',
                currentInfoClass: 'current-info',
                autoAnchor:true
            });

            $(".prod_add_to_cart td.item-only-add button").click(function(){
                $("#cart_popup div.title a h3").focus();
            });

            $(".prod_add_to_cart .delayed-button").click(function(){
                /* Onclick Handling for the add to cart button, that is disabled for 1 second after click*/
                $('.prod_add_to_cart').delay('50').queue(function () {
                    $(this).find('button.delayed-button[type=submit]').prop("disabled", true).addClass("disabled").html("Please wait").delay('2000').queue(function() {
                        $(this).prop("disabled", false).removeClass("disabled").html("Add to cart").dequeue();
                    });
                    $(this).dequeue();
                });
            });
        }

        this.openActiveTabs();
    },

    openActiveTabs: function() {
        $("#preselected").trigger('click');
        /* tricky to select all a tags with that id, not only the first one, no chance to do it with class because of the accessibleTabs */
        $("a#preselected_sub").parents('.subTabs').find('.sub-tabbody').hide();
        $("a#preselected_sub").trigger('click');
    }
};

$(document).ready(function ()
{
    ACC.producttocarttabs.bindAll();
});

