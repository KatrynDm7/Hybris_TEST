ACC.subcriptionDetails = {

    bindAll: function ()
    {
        this.subscriptionPopup();
        this.subscriptionTabs();
        this.subscriptionActions();
    },

    subscriptionPopup: function () {
        if($(".js-cancel-subscription").length>0){

            $(document).on("click",".js-cancel-subscription",function(e){
                e.preventDefault();

                $.colorbox({
                    inline: true,
                    href:"#cancel-subscription-confirm",
                    width: "380px",
                    height:"100px",
                    overlayClose:false,
                    close: false,
                    onComplete: function(){
                        $.colorbox.resize();
                    },
                    onCleanup:  function(){
                        var popupId = $("#cancel-subscription-confirm"),
                            idWrapper = $(".js-cancel-subscription").next();
                        idWrapper.empty();
                        $(popupId).appendTo(idWrapper);
                    }
                })

            })

            $(document).on("click","#cancel-subscription-confirm .r_action_btn",function(e){
                e.preventDefault();
                $.colorbox.close();
            })
        }
    },

    subscriptionTabs: function () {
        $(".account-upgrade-subscription .tabs").accessibleTabs({
            tabhead:'h2',
            fx:"show",
            autoAnchor:true,
            fxspeed:null
        });
    },

    subscriptionActions: function () {
//      remove checkmark if not equal
//        $(".equalCheck").each(function(){
//            var tds = $(this).parents("tr").find("td");
//            if($(tds[0]).html() == $(tds[2]).html()){
//                $(this).remove();
//            }
//        });


        $(document).on("click", ".viewPotentialUpgradeBillingDetails", function(){
            $self=$(this);
            $.colorbox({
                href:$self.data("url"),
                width:"730px",
                height:false,
                onComplete: function(){
                    $.colorbox.resize();

                    if($("#addUpgradeButton").hasClass("not-upgradable")){
                        $("#upgrade-billing-changes .confirm").attr("disabled","disabled").addClass("not-upgradable")
                    }
                }
            })
        })

        $(document).on("click", "#upgrade-billing-changes .r_action_btn", function(e){
            e.preventDefault();
            $.colorbox.close();
        })
    }
};

$(document).ready(function () {
    ACC.subcriptionDetails.bindAll();
});
