(function($){
	"use strict";
	/**
		create countdown box within a jQuery element. This element should be the container 
		of the count down box
		@UTCEndTime: end time for count down as format of millisecond, should be UTC time
		@paramArray" the parameters Array for callback method
		@notification: notification when count down to zero
		@contextObject: context object for callback method
	*/
	$.fn.countDown = function(UTCEndTime, paramArray,notification, contextObject){
		var that = this;
		var __showTime = {
					d: {value:'-1', unit:86400},
					h: {value:'-1', unit:3600},
					m: {value:'-1', unit:60},
					s: {value:'-1', unit:1}
			},

			__UTCEndTime = UTCEndTime,
			__leftTime = 0,
			__lastTimeStamp = 0,
			__defaultConfig = {
				"d":$("#unit_d" ,this).val() || 'Day',
				"h":$("#unit_h" ,this).val() || 'Hour',
				"m":$("#unit_m" ,this).val() || 'Minute',
				"s":$("#unit_s" ,this).val() || 'Second',
				"t":$("#bottom_text" ,this).val() || 'Until to start'

			},
			__param = undefined,
			__callback = undefined,
			__contextObject = undefined;

			if(paramArray){
				if($.isArray(paramArray)){
					__param = paramArray;
					__callback = notification;
					__contextObject = contextObject;
				}else if(typeof paramArray === 'function'){
					__callback = paramArray;
					__contextObject = notification;
				}else{
					console.error("wrong parameter for function CountDown, 'paramArray' should be an array," + 
					"but you provide " + Object.prototype.toString.call(paramArray));
				}
			}

			init();


			function calUITime(){
				var diffSeconds = Math.floor((__leftTime)/1000);
				function timeChanslate(timer){
					var time = Math.floor(diffSeconds/timer.unit);
					timer.value = time >= 10? time: '0' + time;
					diffSeconds = diffSeconds - time * timer.unit;
				}

				timeChanslate(__showTime.d);
				timeChanslate(__showTime.h);
				timeChanslate(__showTime.m);
				__showTime.s.value = diffSeconds > 0? (diffSeconds >= 10? diffSeconds: '0' + diffSeconds):"00";
			}

			function init(){
					
					// init time
					var current = Date.now(); 
					__leftTime = __UTCEndTime - current >= 0 ?__UTCEndTime - current:0;
					calUITime();

					// draw UI box
					var $frame = $("<div>").addClass('countdown-frame');
					var $itemBoxD = $("<div>",{"class": "countdow-itemBox"}).append($("<div>",{"class":"countdown-number","text": __showTime.d.value }).attr("id","data-countdown-d"))
						.append($("<div>",{"class": "countdown-unit","text": __defaultConfig.d}));
					var	$itemBoxH = $("<div>",{"class": "countdow-itemBox"}).append($("<div>",{"class":"countdown-number","text":  __showTime.h.value }).attr("id","data-countdown-h"))
						.append($("<div>",{"class": "countdown-unit","text": __defaultConfig.h})),
						$itemBoxM = $("<div>",{"class": "countdow-itemBox"}).append($("<div>",{"class":"countdown-number","text":  __showTime.m.value }).attr("id","data-countdown-m"))
						.append($("<div>",{"class": "countdown-unit","text": __defaultConfig.m})),
						$itemBoxS = $("<div>",{"class": "countdow-itemBox"}).append($("<div>",{"class":"countdown-number","text":  __showTime.s.value }).attr("id","data-countdown-s"))
						.append($("<div>",{"class": "countdown-unit","text": __defaultConfig.s})),
						$botomText = $("<div>").addClass('countdown-text').text(__defaultConfig.t);

					$frame.append($itemBoxD).append($itemBoxH).append($itemBoxM).append($itemBoxS).append($botomText);
					that.append($frame);

					var callback = function(timestamp){
			
						
						timestamp = timestamp || Date.now();
						var currentTime = timestamp < 1e12 ? Date.now():timestamp;

						if(currentTime >= __UTCEndTime){
							__leftTime = 0;
							updateUITime();
							if(typeof __callback === 'function'){
								if(__contextObject){
									__callback.apply(__contextObject, __param);
								}else{
									__callback(__param);
								}
							}
							return;
						}

						/** when briowser is not in active status, requestAnimationFrame will not call callback mothed but only count down time,
						for this reason, it should be substract several seconds but not only 1 second if the browser
						switch back to active after long time in inactive.
						*/
						var diffTime = currentTime - __lastTimeStamp;
						if(diffTime >= 990){
							__leftTime = __leftTime - diffTime;
							updateUITime();
							__lastTimeStamp = currentTime;
						}
						requestAnimationFrame(callback);
					}

					var requestAnimationFrame = window.requestAnimationFrame ||
							window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame ||
							window.oRequestAnimationFrame || window.msRequestAnimationFrame || null;

					if(requestAnimationFrame !== null){

						__lastTimeStamp = window.animationStartTime ||
								window.webkitAnimationStartTime || window.mozAnimationStartTime ||
								window.oAnimationStartTime || window.msAnimationStartTime || 
								(window.performance&&window.performance.now()) || Date.now() ;
						
						__lastTimeStamp = __lastTimeStamp < 1e12 ? Date.now():__lastTimeStamp;

						requestAnimationFrame(callback);
					}else{
						__lastTimeStamp = Date.now();
						setInterval(callback, 990); 
					}
			}

			function updateUITime(){
				  	calUITime();
				  	$("#data-countdown-d", that).text(__showTime.d.value );
				  	$("#data-countdown-h", that).text(__showTime.h.value);
					$("#data-countdown-m", that).text(__showTime.m.value);
					$("#data-countdown-s", that).text(__showTime.s.value);
  			}

  			return this;

	}

	
})(jQuery);
