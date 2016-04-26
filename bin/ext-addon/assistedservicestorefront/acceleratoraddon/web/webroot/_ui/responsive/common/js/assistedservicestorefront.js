var sessionSec;
var counter;
var carts;

function addASMHandlers() {
	
	revertAutocompleteNormalize();
	addCloseBtnHandler();
    addASMFormHandler();
    addHideBtnHandler();

    if ($("#sessionTimer").length && $('#asmLogoutForm').length ) {
        startTimer();
    }

    if ($("#resetButton").length) {
        $("#resetButton").click(function() {
            resetSession();
        });
    }

    /* for <=IE9 */
    if (placeholderNotAvailable()) {
        $('[placeholder]').focus(function() {
            var input = $(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
                input.removeClass('placeholder');
            }
        }).blur(function() {
                var input = $(this);
                if (input.val() == '' || input.val() == input.attr('placeholder')) {
                    input.addClass('placeholder');
                    input.val(input.attr('placeholder'));
                }
            }).blur();
    }

    $('[placeholder]').blur(function() {
        var input = $(this);
        if (input.val() == '') {
            if (input.attr("name")) {
                toggleBind(false);
            }
        }
    });

    if ($('.ASM_alert_cart').length) {
        $("input[name='cartId']").addClass('ASM-input-error');
    }
    
    if ($('.ASM_alert_customer').length) {
        $("input[name='customerName']").addClass('ASM-input-error');
    }
    
    if ($('.ASM_alert_cred').length) {
    	$("input[name='username']").addClass('ASM-input-error');
    	$("input[name='password']").addClass('ASM-input-error');
	}
    
    if ($('.ASM_alert_create_new').length) {
    	toggleCreateAccount(true);
    }
    
    /* form validation */
    if ($('#_asmLogin').length) {
        var loginUser = $("#asmLoginForm input[name='username']");
        var min = 1;
        if ( loginUser.val().length >= min) {
            loginUser.parent().addClass('checked');
        }
    }

    $("#asmLoginForm input[name='username'], #asmLoginForm input[name='password']").keyup(function () {
        var min = 1;
        var parentNode = $(this.parentNode)

        if (this.value.length >= (min) ) {
            parentNode.addClass('checked');
            checkSignInButton(parentNode);
        }
        else {
            parentNode.removeClass('checked');
            checkSignInButton(parentNode);
        }
    });

    /* start session validation */
    $("input[name='customerName']").keyup(function (e) {
        validateNewAccount(this);
        $(this).removeData( "hover" );
        removeAsmHover();
        toggleBind(false);
        toggleStartSessionButton(this, false);

        if ($(this).val().length < 3) {
            toggleCreateAccount(false);
        }
    });

    $("#_asmPersonifyForm input[name='cartId']").keyup(function () {
        formValidate(this, 8, true, 8);
        if (isErrorDisplayed()) {
   		    $("input[name='cartId']").removeClass('ASM-input-error');
   		    if ($('.ASM_alert')) {
   		        $('.ASM_alert').remove();
   		    }
        }
    });
    
    $("#_asmPersonifyForm input[name='customerName']").keyup(function () {
    	if (isErrorDisplayed()) { 
   		    $("input[name='customerName']").removeClass('ASM-input-error');
   		    if ($('.ASM_alert')) {
   		        $('.ASM_alert').remove();
   		    }
    	    if ($(this).val() == "") {
    		    $("input[name='cartId']").removeClass('ASM-input-error');
    		    toggleStartSessionButton ($("input[name='cartId']"), true);
    		    $("input[name='customerId']").val("");
    	    }
    	}
    	if ($(this).val() == "") { 
    		    $("input[name='cartId']").val("");
    		    $( "#asmAutoCompleteCartId" ).empty();
   	    }
    });

    $("#_asmPersonifyForm input[name='cartId']").blur(function () {
        var regEx = /^\s+$/;
        if (regEx.test($(this).val()) ) {
            $(this).val('');
            formValidate(this, 8, true, 8);
        }
    });

    $("#_asmBindForm input[name='cartId']").keyup(function (e) {
    	checkCartIdFieldAndToggleBind(this);
    });
    
    $("#_asmBindForm input[name='cartId']").bind('paste', function (e) {
    	var inputField = this;
    	setTimeout(function () {
    		checkCartIdFieldAndToggleBind(inputField);
    	}, 100);
	});
    /* end form validation */

    $("input[name='customerName'], input[name='customerId']").hover(function() {
            var item = ( $(this).attr('data-hover') )? jQuery.parseJSON($(this).attr('data-hover')) : $(this).data( "hover" );
            var disabled = ( $(this).attr('data-hover') )? "disabled" : "";

            if( !(item === null || item === undefined) ) {
                $(this).after( '<div id="asmHover" class="'+ disabled +'"><span class="name">' + item.name + '</span><span class="email">' + item.email + '</span><span class="date">' + item.date +'</span><span class="card">' + item.card + '</span></div>' );
            }
        }, function () {
            removeAsmHover();
        }
    );


    $("#_asmPersonifyForm input[name='cartId']").autocomplete({
        source: function( request, response ) {
            response(carts);
        },
        appendTo: "#asmAutoCompleteCartId",
        autoFocus: true,
        minLength: 0,
        select: function( event, ui ) {
            if (ui.item.value == "") {
                event.preventDefault();
            }
            toggleStartSessionButton (this, true);
        }
    });

    $("#_asmPersonifyForm input[name='cartId']").on('click, focus', function() {
        $("#_asmPersonifyForm input[name='cartId']").autocomplete('search', '');
    });

    if ($("input[name='customerName']").length > 0) {
        $("input[name='customerName']").autocomplete({
            source: function( request, response ) {
                $.ajax({
                    url: ACC.config.contextPath + "/assisted-service/autocomplete",
                    dataType: "jsonp",
                    data: {
                        customerId: request.term
                    },
                    success: function( data ) {
                        response( data );
                    }
                });
            },
            minLength: 3,
            appendTo: "#asmAutoComplete",
            select: function( event, ui ) {
                if (ui.item.value == "") {
                    event.preventDefault();
                    return;
                }
                toggleStartSessionButton (this, true);
                $(this).data('hover',{name:ui.item.value, email:ui.item.email, card:ui.item.card, date:ui.item.date});

                /* insert item.value in customerId hidden field */
                $("input[name='customerId']").val(ui.item.email);

                carts = ui.item.carts;
                if ($("input[name='cartId']").attr("orig_value") == null) {
                	$("input[name='cartId']").val('');
                	if (carts != null) {
		                if (carts.length == 1) {
		                    $("input[name='cartId']").val(carts[0]);
		                } else {
		                    $("input[name='cartId']").autocomplete('search', '');
		                    $("input[name='cartId']").focus();
		                }
		            } else {
		                carts = [{label: "No Existing Carts", value: ""}];
		                $("input[name='cartId']").autocomplete('search', '');
		                $("input[name='cartId']").focus();
		            }
                }

                toggleBind(true);

            }
        }).data( "ui-autocomplete" )._renderItem = function( ul, item ) {
            if (item.value == "") {
                toggleCreateAccount(true);
                return $( "<li></li>" ).data( "item.autocomplete", item ).append( "<a><span class='noresult'>No Customer ID found</span></a>")
                    .appendTo( ul );
            }
            else { toggleCreateAccount(false); }

            return $( "<li></li>" )
                .data( "item.autocomplete", item )
                .append( "<a><span class='name'>" + item.value + "</span><span class='email'>" + item.email +
                    "</span><span class='date'>" + item.date + "</span><span class='card'>" + item.card + "</span></a>")
                .appendTo( ul );
        };
    }

    if ($("#_asmBindForm").length) {
        var customerId = $("input[name='customerName']").attr('readonly');
        var cartId = $(".cartId input").attr('disabled');

        if(customerId === "readonly"){
            $(".ASM_icon-chain").removeClass('invisible').addClass('ASM_chain-bind');
        }
    }

    if ($(".add_to_cart_form").length && $("#_asm input[name='cartId']").val() == "") {
    	$( ".add_to_cart_form" ).submit(function( event ) {
    		setTimeout(function () {
    			var url = ACC.config.encodedContextPath + "/assisted-service/add-to-cart";
        		$.post(url, function( data ) {
        			$("#_asm").replaceWith(data);
                    addASMHandlers();
            	})
    	    }, 400);
    	});	
    }
    
    enableAsmPanelButtons();
}

$( document ).ready(function() {
    addASMHandlers();
});

function addASMFormHandler() {
    if ($) {
        if ($(".asmForm").length) {
            $(".asmForm").each(function () {
                $(this).submit(function() {

                    $(this).find('[placeholder]').each(function() {
                        var input = $(this);
                        if (input.val() == input.attr('placeholder')) {
                            input.val('');
                        }
                    })

                    $.ajax({
                        type: "POST",
                        url: $(this).attr('action'),
                        data: $(this).serialize(),
                        success: function(data) {
                            $("#_asm").replaceWith(data);
                            addASMHandlers();
                        }
                    });
                    return false;
                });
            });
        }
    }
}

function addCloseBtnHandler() {
    $("#_asm .closeBtn").click( function() { 
    	var url = ACC.config.encodedContextPath + "/assisted-service/quit";
    	$.post(url, function( data ) {
            var oldurl = window.location.href;
            var newurl = oldurl.replace("&asm=true", "").replace("?asm=true&", "?").replace("?asm=true", "");
            window.location.replace(newurl);
    	});
    });
}

function addHideBtnHandler() {
    $("#_asm .ASM_control_collapse").click( function() { $("#_asm").toggleClass("ASM-collapsed"); });
}

function startTimer() {
    sessionSec = timer;
    clearInterval(counter);
    counter = setInterval(timerFunc, 1000);
}

function timerFunc() {
    if (sessionSec <= 0) {
        clearInterval(counter);
        finishASMagentSession();
        return;
    }
    sessionSec = sessionSec - 1;
    var min = Math.floor(sessionSec / 60);
    var sec = sessionSec % 60;
    if (min < 10)
    {
        min = "0" + min;
    }
    if (sec < 10)
    {
        sec = "0" + sec;
    }
    $("#sessionTimer").html("<span class='hidden-xs hidden-sm hidden-md'>Session timeout: </span><span class='ASM_timer_count'>" + min + ":" + sec + "</span> min");
}

function resetSession() {
    var request = $.ajax({
        url : ACC.config.contextPath + "/assisted-service/resetSession",
        type : "POST"
    });
    request.done(function(msg) {
        sessionSec = timer + 1;
    });
    request.fail(function(jqXHR, textStatus) {
        $('#errors').empty();
        $('#errors').append("Request failed: " + textStatus);
    });
}

function finishASMagentSession() {
	$.ajax({
        url : ACC.config.encodedContextPath + "/assisted-service/logoutasm",
        type : "POST",
        success: function(data) {
            $("#_asm").replaceWith(data);
            addASMHandlers();
        }
    });
}

function isStartEmulateButtonPresent() {
	return $(".ASM-btn-start-session").length == 1;
}

function enableAsmPanelButtons() {
    $('div[id="_asm"] button').not(".ASM-btn-start-session, .ASM-btn-create-account, .ASM-btn-login").removeAttr('disabled');
    if (isStartEmulateButtonPresent()) {
	    if ($("#_asmPersonifyForm input[name='customerId']").val() != "") {
	    	$("#_asmPersonifyForm input[name='customerId']").parent().addClass("checked");
	    }
        formValidate($("#_asmPersonifyForm input[name='cartId']")[0], 8, true, 8);
	}
}

function placeholderNotAvailable(){
    var i = document.createElement('input');
    return !('placeholder' in i);
}

function removeAsmHover() {
    $('#asmHover').remove();
}

function toggleCreateAccount(activate){
    var bindIcon = $(".ASM_icon-chain");
    var createButton = $("#_asmCreateAccountForm button.ASM-btn-create-account[type='submit']");
    if (activate) {
        createButton.removeClass('hidden');
        bindIcon.removeClass('invisible');
    } else {
        createButton.addClass('hidden');
        bindIcon.addClass('invisible');
    }
}
function toggleActivationState(button, activate){
    if (activate) {
        button.removeAttr('disabled');
    }
    else {
        button.attr('disabled','');
    }
}
function checkSignInButton (el) {
    var signInBtn = $("#asmLoginForm button[type='submit']");
    var checkSum = $(el).parent().find('.checked').length;
    if(checkSum > 1) {
        toggleActivationState(signInBtn, true);
    }
    else {
        toggleActivationState(signInBtn, false);
    }
}

function checkStartSessionButton (el) {
    toggleStartSessionButton (el, false);
    var checkSum = $(el.parentNode).siblings('.checked').length;
    if(checkSum > 0) {
        toggleActivationState($("button.ASM-btn-start-session"), true);
    }
}

function checkCartIdFieldAndToggleBind(cartIdField) {
	if( !$(cartIdField).hasClass('placeholder') &&  $("input[name='customerId']").val().length > 0) {
        if (!isNaN(cartIdField.value) && (cartIdField.value.length == 8) && (cartIdField.value != cartIdField.getAttribute('orig_value'))) {
            $("#_asmBindForm button[type='submit']").removeClass('hidden');
            $(".ASM_icon-chain").removeClass('invisible');
            return;
        }
    }
    $("#_asmBindForm button[type='submit']").addClass('hidden');
    $(".ASM_icon-chain").addClass('invisible');
}

function toggleBind (activate) {
    if ($("#_asmBindForm").length) {
        var bindIcon = $(".ASM_icon-chain");
        var bindButton = $("#_asmBindForm button.ASM-btn-bind-cart[type='submit']");
        if (activate){
            bindButton.removeClass('hidden');
            bindIcon.removeClass('invisible');
        } else {
            bindButton.addClass('hidden');
            if($('.ASM-btn-create-account').hasClass('hidden')){
                bindIcon.addClass('invisible');
            }
        }
    }
}

function toggleStartSessionButton (el, activate) {
    var checkedItem = $(el).parent();
    var button = $("button.ASM-btn-start-session");
    if (activate){
        button.removeAttr('disabled');
        checkedItem.addClass("checked");
    } else {
        button.attr('disabled','');
        checkedItem.removeClass("checked");
    }
}

function formValidate (el, min, number, max ) {
    if( !$(el).hasClass('placeholder') ) {
    	if ($(el).hasClass("ASM-input-error")) {
    		toggleStartSessionButton (el, false);
    		return false;
    	}
        if (number !== false) {
            if (isNaN(el.value)) {
                toggleStartSessionButton (el, false);
                return false;
            }
        }
        if (el.value.length >= (min) ) {
            toggleStartSessionButton (el, true);

            if ( max !== undefined && el.value.length > (max) ) {
                toggleStartSessionButton (el, false);
            }
        }
        else if (el.value.length === 0 ) {
            checkStartSessionButton(el);
        }
        else {
            toggleStartSessionButton (el, false);
            return false;
        }
        return true;
    }
    return false;
}

function validateEmail(mailAddress) {
    return ($('<input>').attr({ type: 'email', required:'required' }).val(mailAddress))[0].checkValidity() && (mailAddress.indexOf(".") > 0);
}

function validateName(name){
    var regEx = /^[a-zA-Z-]+\s[a-zA-Z-]+$/;
    return (name != '' && regEx.test(name));
}

function validateNewAccount(el) {
    var createAccountButton = $("#_asmCreateAccountForm button.ASM-btn-create-account[type='submit']");
    var customerValues = el.value.split(', ');
    var IdInput = $("#_asmCreateAccountForm input[name='customerId']");
    var NameInput = $("#_asmCreateAccountForm input[name='customerName']");

    if (customerValues.length > 1) {
        var validName = validateName(customerValues[0]);
        var validMail = validateEmail(customerValues[1]);
        if (validName && validMail) {
            toggleActivationState(createAccountButton, true);
            /* fill hidden input fields */
            IdInput.val(customerValues[1].replace(/^\s\s*/, '').replace(/\s\s*$/, ''));
            NameInput.val(customerValues[0]);
        }
        else {
            /* no valid customer values */
            toggleActivationState(createAccountButton, false);
            return false;
        }
    }
    else {
        /* too less customer values */
        toggleActivationState(createAccountButton, false);
        return false;
    }
}

function revertAutocompleteNormalize() {
	/* After http://bugs.jqueryui.com/ticket/9762 there was a change when for empty value label is placed instead. 
	 * But we want to send empty values for NO_FOUND case */
	$.ui.autocomplete.prototype._normalize =  function(a){
		if ( a.length && a[ 0 ].label && a[ 0 ].value ) {return a; }
		return $.map( a, function( b ) {if ( typeof b === "string" ) { return {label: b,value: b};}
			return $.extend({label: b.label || b.value, value: b.value || b.label}, b );});
	};
}

function isErrorDisplayed() {
	return $('.ASM_alert').length;
}
