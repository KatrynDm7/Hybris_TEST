$(function(){
	"use strict";
	var $parent = $("div.countdownbox");
	var __oProgress,
	    __productID,
	    __URI;
	var __messageText;
	var showResultInterval = $("#showResultInterval", $parent).val();
	var showResultIntervalInt = parseInt(showResultInterval);	
	if($parent.length > 0){
		
		var leftTime = $("#startTime", $parent).val();
		
		if( leftTime > 0){
			var current = Date.now();
			var startTime = parseInt(leftTime) + parseInt(current);
			$parent.countDown(startTime, timerToZero);
			
		}else{

			var soldout = $(".flashbuy-frame #h-soldout").val();
			if(!soldout){
				var waitingText = $("#process-text").val();
				__oProgress = new ACC.ProgressBar({"text": waitingText});
				enableButton();
			}else{
				
			}
		}
	}
	amountValidate();
	
	function enableButton(){

		$(".flashbuy-frame #addToCartButton").removeClass("disableButton").on("click",function(e){
			__oProgress.show();
			var $addToCartForm = $('#addToCartForm');
			var url = $addToCartForm.attr('action');
			__productID = url.slice(url.lastIndexOf('/') + 1);
			__URI = url.slice(0, url.lastIndexOf('/'));
			$addToCartForm.attr('action', __URI + '/enqueue/' + __productID);

			$addToCartForm.ajaxSubmit(function(text, status, xhr){		
				if(text === "success"){
					setTimeout(goToCartPage,showResultIntervalInt);
				}else if(text.indexOf("reject") >= 0){
					__messageText = text;
					setTimeout(showRunout,showResultIntervalInt);
				}

			});

		}).ajaxError(function(event, xhr, settings, thrownError) {
			console.log(thrownError);
			__oProgress.hide();
		});


	}
	
	function goToCartPage(){
		var sCartAddress = $('#miniCartLayer').prev().attr('href');
		window.location.href = sCartAddress;
	}
	
	function showRunout(){
		
		var text = $.parseJSON(__messageText);
		$("#addToCartButton").text(text.label);
		disableButton();
		__oProgress.hide();
	
	}

	function disableButton(){
		$(".flashbuy-frame #addToCartButton").addClass("disableButton").off('click');

	}
	
	function timerToZero(){
		var soldout = $(".flashbuy-frame #h-soldout").val();
		if(!soldout){
			var waitingText = $("#process-text").val();
			__oProgress = new ACC.ProgressBar({"text": waitingText});
			$parent.hide();
			enableButton();
		}else{
			
		}

	}
	
	function amountValidate(){		
		$(".flashbuy-frame #flashbuy-input-qty").change(function(){
			var currAmountStr = $(this).val();			
			var currAmount = Number(currAmountStr);
			var availableAmount = Number($(".flashbuy-frame #max-available-qty").val());
			$(".flashbuy-frame #invalidQty").remove();
			if(! /^[0-9]*$/.test(currAmountStr) || currAmount<1 ){
				$(this).val(1);
				var errortxt = $(".flashbuy-frame #input-not-digit").val();
				$("div.errorMessage##errorMessage").append($("<span id='invalidQty'></span>").text(errortxt));
				return;
			}else if(currAmount > availableAmount){
				$(this).val(availableAmount);
				var errortxt = $(".flashbuy-frame #input-over-max").val();
				$("div.errorMessage#errorMessage").append($("<span id='invalidQty'></span>").text(errortxt+availableAmount));
				return;
			}
		});
	}
	
});