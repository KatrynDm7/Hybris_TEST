ACC.insurance = {

    addToCartType: 'addToCart',
    changePlanType: 'changePlan',
    popupDivId: '#popup_confirm_plan_removal',
    popupWrapperId: '#popup_confirm_plan_removal_wrapper',
    popupDivId_RetrieveQuote: '#popup_confirm_retrieve_quote',
    popupWrapperId_RetrieveQuote: '#popup_confirm_retrieve_quote_wrapper',
    changePlanStoreFormDataCheckboxId: '#changePlanStoreFormDataCheckbox',
    cartPageUrl: ACC.config.contextPath + "/cart",
    checkFormDataUrl: ACC.config.contextPath + "/cart/rollover/checkFormData",
    myQuotes: '.myQuotes',
    myPolicies: '.myPolicies',

    bindAll: function()
    {
        this.displayToolTip();
        this.changePlanAction();
        this.addToCartAction();
        this.addPotentialProductToCartAction();
        this.changeSelect();
        this.lookForToggle();
        this.retrieveQuoteButton();
        this.dontTriggerXforms();
        this.sameHeight();
        this.enableAutoProduct();
        
    },

    dontTriggerXforms: function() {
        $(".fr-section-title").append("<div class='overlay'></div>");
    },

    displayToolTip: function()
    {
        if($(".tooltip").next(".tip")){
            $(".tooltip").on("click", function (e){
                if(($(this).offset().top+$(".tip").height())>$(window).height()+$(window).scrollTop()) {
                    $(".tip").removeClass("bottom");
                    $(".tip").addClass("top");
                } else {
                    $(".tip").removeClass("top");
                    $(".tip").addClass("bottom");
                }
                $(".tip").css("display","none");
                $(this).next(".tip").toggle();
            });
            $(".tip .closeTip").on("click", function (){
                $(this).parent().toggle();
            });
            $(document).keydown(function( event ) {
               if ( event.which == 27 ) {
                    $(".tip").css("display","none");
                }
            });
        }
    },

    submitFormAction: function(button, form, type, isSaveCart)
    {
        button.on("click", function ()
        {
            ACC.insurance.submitFormActionFunction(form, type, isSaveCart);
        })
    },

    submitFormActionFunction: function (form, type, isSaveCart) {
        var $storeFormDataCheckVal = $(ACC.insurance.changePlanStoreFormDataCheckboxId).prop('checked');
        if ($storeFormDataCheckVal) {
            ACC.insurance.appendIsStoreFormDataValue(form, 'true');
        }
        else {
            ACC.insurance.appendIsStoreFormDataValue(form, 'false');
        }

        if (type === ACC.insurance.changePlanType) {
            form.submit();
        }
        else if (type === ACC.insurance.addToCartType) {
            ACC.insurance.appendSaveCartValue(form, isSaveCart);
            $.ajax({
                url: form.attr('action'),
                type: 'POST',
                async: false,
                dataType: "json",
                data: form.serialize(),
                success: function (data) {
                    if (data.result.success === 'true') {
                        window.location.href = ACC.insurance.cartPageUrl;
                    }
                }
            });
        }


        parent.$.colorbox.close();
    },

    changePlanAction: function ()
    {
        var $changePlanSubmitBtn = $('.changePlanForm .changePlanButton');
        $changePlanSubmitBtn.on('click', function() {
            ACC.insurance.popupConfirmationBox($changePlanSubmitBtn.parent(), ACC.insurance.changePlanType);
        });
    },
    
    addToCartAction: function()
    {
        $('.addToCartForm :submit').each(function () 
        {
            var $formButton = $(this);
            var $form = $formButton.parent();
            $formButton.on('click', function () {
            	 /** START: 5.5.1 temporary snippet to be removed in 5.6 after BIT-1299 is done**/
                if ($('#_asm').length) {
                 setTimeout(function () {
                 var url = ACC.config.encodedContextPath + "/assisted-service/add-to-cart";
                 $.post(url, function( data ) {
                  $("#_asm").replaceWith(data);
                           addASMHandlers();
                    })
                 }, 10);
                }
               /** END: 5.5.1 temporary snippet to be removed in 5.6 **/
                var $popupConfirmDiv = $(ACC.insurance.popupDivId);
                $.ajax({
                    url: ACC.insurance.checkFormDataUrl,
                    type: 'GET',
                    async: false,
                    dataType: "json",
                    success: function (result) {
                        $.ajax({
                            url: $form.data('addtocart'),
                            type: 'POST',
                            async: false,
                            dataType: "json",
                            data: $form.serialize(),
                            success: function (data) {
                                if (data.result.success === 'true') {
                                    window.location.href = ACC.insurance.cartPageUrl;
                                }
                                else {
                                    if (data.result.cartUpperLimitReached === 'true')
                                    {
                                        if (data.result.sameInsuranceType === 'true')
                                        {
                                            ACC.insurance.appendIsSameInsuranceType($form, true);
                                            $(ACC.insurance.changePlanStoreFormDataCheckboxId).prop('checked', true);
                                            ACC.insurance.submitFormActionFunction($form, ACC.insurance.addToCartType);
                                        }
                                        else
                                        {
                                            if (result.result.hasFormData === 'true')
                                            {
                                                $popupConfirmDiv.find('.saveData').show();
                                                $(ACC.insurance.changePlanStoreFormDataCheckboxId).prop('checked', true);
                                            }
                                            ACC.insurance.popupConfirmationBox($form, ACC.insurance.addToCartType);
                                        }
                                    }
                                    else
                                    {
                                        window.location.href = ACC.insurance.cartPageUrl;
                                    }
                                }
                            }});
                    }});

                return false;
            });
        });
    },
    
    addPotentialProductToCartAction: function()
    {
        $('.addPotentialProductToCartForm :submit').each(function () 
        {
            var $formButton = $(this);
            var $form = $formButton.parent();
            $formButton.on('click', function () {
                $.ajax({
                    url: $form.attr('action'),
                    type: 'POST',
                    async: false,
                    dataType: "json",
                    data: $form.serialize(),
                    success: function () {
                        window.location.href = ACC.insurance.cartPageUrl;
                    }
                });
                return false;
            });
        });
    },

    popupConfirmationBox: function(form, type) 
    {
        $.colorbox({
            inline: true,
            height: 185,
            width: 620,
            href: ACC.insurance.popupDivId,
            onOpen: function () {
                var $popupConfirmDiv = $(ACC.insurance.popupDivId);
                $.ajax({
                    url: ACC.insurance.checkFormDataUrl,
                    type: 'GET',
                    async: false,
                    dataType: "json",
                    success: function (data) {
                        var $changePlanStoreFormDataCheckbox = $(ACC.insurance.changePlanStoreFormDataCheckboxId);
                        if (data.result.hasFormData === 'true') 
                        {
                            $popupConfirmDiv.find('.saveData').show();
                            $changePlanStoreFormDataCheckbox.prop('checked', true);
                        }
                        else 
                        {
                            $changePlanStoreFormDataCheckbox.prop('checked', false);
                        }
                    }});
                ACC.insurance.submitFormAction($("#saveProceedButton"), form, type, true);
                $popupConfirmDiv.find("#cancelButton").on("click", function () {
            		$.colorbox.close();
        		});                
            },
            onClosed: function() {
                form.find('input[name=isStoreFormData]').remove();
                form.find('input[name=isSaveCart]').remove();
            },
            onCleanup: function (){
                $(ACC.insurance.popupWrapperId).empty();
                $(ACC.insurance.popupDivId).appendTo(ACC.insurance.popupWrapperId);
            }
        });
    },

    appendIsStoreFormDataValue: function(form, value)
    {
        $('<input>').attr('type', 'hidden').attr('name', 'isStoreFormData').attr('value', value).appendTo(form);
    },
    
    appendSaveCartValue: function(form, value)
    {
        $('<input>').attr('type', 'hidden').attr('name', 'isSaveCart').attr('value', value).appendTo(form);
    },

    appendIsSameInsuranceType: function(form, value)
    {
        $('<input>').attr('type', 'hidden').attr('name', 'isSameInsuranceType').attr('value', value).appendTo(form);
    },
    /* neu */

    changeSelect: function()
    {
        if(!document.getElementById && !document.createTextNode){return;}
        
    // Classes for the link and the visible dropdown
        var ts_selectclass='sortOptions';  // class to identify selects
        var ts_listclass='turnintoselect';      // class to identify ULs
        var ts_boxclass='dropcontainer';        // parent element
        var ts_triggeron='activetrigger';       // class for the active trigger link
        var ts_triggeroff='trigger';            // class for the inactive trigger link
        var ts_dropdownclosed='dropdownhidden'; // closed dropdown
        var ts_dropdownopen='dropdownvisible';  // open dropdown
    /*
        Turn all selects into DOM dropdowns
    */
        var count=0;
        var toreplace=new Array();
        var sels=document.getElementsByTagName('select');
        for(var i=0;i<sels.length;i++){
            if (ts_check(sels[i],ts_selectclass))
            {
                var hiddenfield=document.createElement('input');
                hiddenfield.name=sels[i].name;
                hiddenfield.type='hidden';
                hiddenfield.id=sels[i].id;
                hiddenfield.value=sels[i].options[0].value;
                sels[i].parentNode.insertBefore(hiddenfield,sels[i])
                var trigger=document.createElement('a');
                ts_addclass(trigger,ts_triggeroff);
                trigger.href='#';
                trigger.onclick=function(){
                    ts_swapclass(this,ts_triggeroff,ts_triggeron)
                    ts_swapclass(this.parentNode.getElementsByTagName('ul')[0],ts_dropdownclosed,ts_dropdownopen);
                    return false;
                }
                trigger.appendChild(document.createTextNode(sels[i].options[0].text));
                sels[i].parentNode.insertBefore(trigger,sels[i]);
                var replaceUL=document.createElement('ul');
                for(var j=0;j<sels[i].getElementsByTagName('option').length;j++)
                {
                    var newli=document.createElement('li');
                    var newa=document.createElement('a');
                    newli.v=sels[i].getElementsByTagName('option')[j].value;
                    newli.elm=hiddenfield;
                    newli.istrigger=trigger;
                    newa.href='#';
                    newa.appendChild(document.createTextNode(
                    sels[i].getElementsByTagName('option')[j].text));
                    newli.onclick=function(){ 
                        this.elm.value=this.v;
                        ts_swapclass(this.istrigger,ts_triggeron,ts_triggeroff);
                        ts_swapclass(this.parentNode,ts_dropdownopen,ts_dropdownclosed)
                        this.istrigger.firstChild.nodeValue=this.firstChild.firstChild.nodeValue;
                        return false;
                    }
                    newli.appendChild(newa);
                    replaceUL.appendChild(newli);
                }
                ts_addclass(replaceUL,ts_dropdownclosed);
                var div=document.createElement('div');
                div.appendChild(replaceUL);
                ts_addclass(div,ts_boxclass);
                sels[i].parentNode.insertBefore(div,sels[i])
                toreplace[count]=sels[i];
                count++;
            }
        }
        
    /*
        Turn all ULs with the class defined above into dropdown navigations
    */  

        var uls=document.getElementsByTagName('ul');
        for(var i=0;i<uls.length;i++)
        {
            if(ts_check(uls[i],ts_listclass))
            {
                var newform=document.createElement('form');
                var newselect=document.createElement('select');
                for(j=0;j<uls[i].getElementsByTagName('a').length;j++)
                {
                    var newopt=document.createElement('option');
                    newopt.value=uls[i].getElementsByTagName('a')[j].href;  
                    newopt.appendChild(document.createTextNode(uls[i].getElementsByTagName('a')[j].innerHTML)); 
                    newselect.appendChild(newopt);
                }
                newselect.onchange=function()
                {
                    window.location=this.options[this.selectedIndex].value;
                }
                newform.appendChild(newselect);
                uls[i].parentNode.insertBefore(newform,uls[i]);
                toreplace[count]=uls[i];
                count++;
            }
        }
        for(i=0;i<count;i++){
            toreplace[i].parentNode.removeChild(toreplace[i]);
        }
        function ts_check(o,c)
        {
            return new RegExp('\\b'+c+'\\b').test(o.className);
        }
        function ts_swapclass(o,c1,c2)
        {
            var cn=o.className
            o.className=!ts_check(o,c1)?cn.replace(c2,c1):cn.replace(c1,c2);
        }
        function ts_addclass(o,c)
        {
            if(!ts_check(o,c)){o.className+=o.className==''?c:' '+c;}
        }
    },
    /* neu end */

    lookForToggle: function()
    {
        $('.js-toggle').each(function ()
        {
            $(this).click(function(){
                var div = $(this).next().data("open");

                //if(this.className.indexOf("open")>-1) {
                    $("#"+div).toggleClass("hidden");
                    $(this).next().toggleClass("open");
                    $(this).next().toggleClass("close");
                //} else {
            })
        });
        $('.open.js').each(function ()
        {
            $(this).click(function(){
                var div = $(this).data("open");

                //if(this.className.indexOf("open")>-1) {
                $("#"+div).toggleClass("hidden");
                $(this).toggleClass("open");
                $(this).toggleClass("close");
                //} else {
            })
        });
    },

    retrieveQuoteButton: function()
    {
        $("#myQuotesList").find("a.retrieveBtn").on('click', function(e){
            var $formButton = $(this);
            var $form = $formButton.parents('form');
           	ACC.insurance.popupRetrieveQuoteConfirmationBox($form, e);
        })
    },
    
    enableAutoProduct: function() {
    	var mandatoryOptionalId = $("#mandatoryBundleProduct").attr("data-code");
    	if(mandatoryOptionalId != undefined) {
    		$('input[type=submit]').each(function () {
	   	       if($(this).attr("id") == mandatoryOptionalId) {
	   	    	 $(this).attr('disabled','disabled');  	    	    	  
	   	       }
	   	  	});
	    	
	    	$('#mandatoryBundleProduct').click(function() {
	    	    if($(this).is(':checked')){
	    	    	$('input[type=submit]').each(function () {
	  	    	       if($(this).attr("id") == mandatoryOptionalId) {
	  	    	    	 $(this).removeAttr("disabled");
	  	    	       }
	  	    	  });
	    	    } else {
	    	    	$('input[type=submit]').each(function () {
	  	    	       if($(this).attr("id") == mandatoryOptionalId) {
	  	    	    	 $(this).attr('disabled','disabled');  	    	    	  
	  	    	       }
	  	    	  });
	    	    }
	    	});
    	}
    },

    popupRetrieveQuoteConfirmationBox: function(form, event)
    {
    	event.preventDefault();
        var quoteCode = form.find('input[name="code"]').val();
        var cartCode = form.find('input[name="cartCode"]').val();
        if (quoteCode == cartCode)
        {
        	form.submit();
        }
        else
        {
	        $.colorbox({
	            inline: true,
	            height: 145,
	            width:620,
	            href: ACC.insurance.popupDivId_RetrieveQuote,
	            onOpen: function(){
	                var $popupConfirmDiv = $(ACC.insurance.popupDivId_RetrieveQuote);
	                $popupConfirmDiv.find("#cancelButton").on("click", function () {
	            		$.colorbox.close();
	        		})   	                
	                $popupConfirmDiv.find("#saveProceedButton").on("click", function(){
	                    ACC.insurance.appendSaveCartValue(form, true);
	                    form.submit();
	                })
	            },
                onCleanup: function (){
                    $(ACC.insurance.popupWrapperId_RetrieveQuote).empty();
                    $(ACC.insurance.popupDivId_RetrieveQuote).appendTo(ACC.insurance.popupWrapperId_RetrieveQuote);
                }
	        });
        }
    },

    sameHeight: function()
    {
        if( $(ACC.insurance.myQuotes).height() ) $('.viewPolicies').css('height', $(ACC.insurance.myQuotes).height()-70);
        if( $(ACC.insurance.myPolicies).height() ) $('.retrieveQuote').css('height', $(ACC.insurance.myPolicies).height()-70);
    }
}

$(document).ready(function()
{
    with(ACC.insurance)
    {
        ACC.insurance.bindAll();
    }
})