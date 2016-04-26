ACC.quotereview = {

    isQuoteBindUrl: ACC.config.contextPath + '/checkout/multi/isQuoteBind',
    bindQuoteUrl: ACC.config.contextPath + '/checkout/multi/bindQuote',
    quoteReviewUrl: ACC.config.contextPath + "/checkout/multi/quote/review",
    popupBackButtonConfirmationDivId: '#popup_confirm_unbind_quote',
    popupBackButtonConfirmationWrapperId: '#popup_confirm_unbind_quote_wrapper',
    popupCheckoutButtonConfirmationDivId: '#popup_confirm_bind_quote',
    popupCheckoutButtonConfirmationWrapperId: '#popup_confirm_bind_quote_wrapper',

    bindAll: function () 
    {
        this.bindAddOptions();
        this.quoteReviewBackButton();
        this.quoteReviewCheckoutButton();
    },
    
    bindAddOptions: function () 
    {
        var $addOptions = $('.quoteAddOptions');
        var $planInfo = $('.quotePlanInfoSection');
        
        
        $addOptions.find('.header').click(function() 
        {
            $addOptions.find('.content').slideToggle();
        });
        
        $planInfo.find('.header').click(function() 
        {
           $planInfo.find('.content').slideToggle();
        });
    },

    isQuoteBind: function() 
    {
        var $isQuoteBind = $.ajax({
            url: ACC.quotereview.isQuoteBindUrl,
            type: 'GET',
            async: false,
            dataType: "text",
            success: function (data) {
                return data;
            }
        });
        
        return $isQuoteBind.responseText === 'true';
    },

    bindQuote: function () {
        var $result = $.ajax({
            url: ACC.quotereview.bindQuoteUrl,
            type: 'POST',
            async: false,
            dataType: "text",
            success: function (data) {
                return data;
            }
        });
        
        return $result.responseText === 'true';
    },
    
    quoteReviewCheckoutButton: function()
    {
        var $checkoutButton = $('.certifyForm').find('input.checkoutBtn');
        var $checkoutSubmitForm = $checkoutButton.parents('form');
        $checkoutButton.on('click', function(e)
        {
            e.preventDefault();
            if(ACC.quotereview.isQuoteBind())
            {
                $checkoutSubmitForm.submit();
            }
            else
            {
                ACC.quotereview.popupCheckoutConfirmationBox($checkoutSubmitForm, e);
            }
        });

        var $popupConfirmDiv = $(ACC.quotereview.popupCheckoutButtonConfirmationDivId);
        $popupConfirmDiv.find("#yesButton").on("click", function () {
            if (ACC.quotereview.bindQuote()) {
                $checkoutSubmitForm.submit();
            }
            else {
                window.location.href = ACC.quotereview.quoteReviewUrl;
            }
        });
        $popupConfirmDiv.find("#cancelButton").on("click", function () {
            $.colorbox.close();
        });
        
    },

    popupCheckoutConfirmationBox: function (form, event) {
        event.preventDefault();
        $.colorbox({
            inline: true,
            height: 145,
            width: 660,
            href: ACC.quotereview.popupCheckoutButtonConfirmationDivId,
            onCleanup: function (){
                $(ACC.quotereview.popupCheckoutButtonConfirmationWrapperId).empty();
                $(ACC.quotereview.popupCheckoutButtonConfirmationDivId).appendTo(ACC.quotereview.popupCheckoutButtonConfirmationWrapperId);
            }
        });
    },
    
    quoteReviewBackButton: function()
    {
        $('#form_button_panel').find('a.checkQuoteStatus').on('click', function(e){
            var $redirect = $(this);
            if(ACC.quotereview.isQuoteBind()){
                ACC.quotereview.popupBackButtonConfirmationBox($redirect, e);
            }
        });

    },
    
    popupBackButtonConfirmationBox: function(trigger, event)
    {
        event.preventDefault();
        $.colorbox({
            inline: true,
            height: false,
            width: 620,
            href: ACC.quotereview.popupBackButtonConfirmationDivId,
            onOpen: function(){
                var $popupConfirmDiv = $(ACC.quotereview.popupBackButtonConfirmationDivId);
                $popupConfirmDiv.find("#yesButton").on("click",function(){
                    window.location.href = trigger.prop("href");
                });
            },
            onComplete: function () {
                $(this).colorbox.resize();
            },
            onCleanup: function (){
                $(ACC.quotereview.popupBackButtonConfirmationWrapperId).empty();
                $(ACC.quotereview.popupBackButtonConfirmationDivId).appendTo(ACC.quotereview.popupBackButtonConfirmationWrapperId);
            }
        });
    }
};

$(document).ready(function ()
{
	ACC.quotereview.bindAll();
});
