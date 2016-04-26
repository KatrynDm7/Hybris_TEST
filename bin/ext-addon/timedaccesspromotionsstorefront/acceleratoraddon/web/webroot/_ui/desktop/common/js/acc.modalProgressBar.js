(function(g){
	"use strict";

	 var global = g.ACC || window;
	 var __instance;

	 function ProgressBar(ops){

	 	var text = ops&&ops.text? ops.text:"";

	 	if(__instance === undefined){
 			 	this.__cover = $('#cboxOverlay');
			 	if(this.__cover.length === 0){
			 		this.__cover = $("<div>",{"id": 'cboxOverlay'}).css("display","none");
			 		$("body").prepend(this.__cover);
			 	}

			 	this.__frame = $("div.acc-progressFrame");
			 	if(this.__frame.length === 0){
				 	this.__bar = $("<div>").addClass('acc-progressBar').append($("<span>"));
				 	this.__title = $("<div>").addClass('acc-progressTitle').text(text);
				 	this.__text = $("<div>").addClass('acc-progressText').text(text);
				 	this.__icon = $("<span>").addClass('acc-progressIcon');
				 	this.__frame = $("<div>").addClass('acc-progressFrame').addClass('acc-progressFrame-hide');
				 	this.__frame.append(this.__icon).append(this.__title).append(this.__text).append(this.__bar);
				 	this.__cover.after(this.__frame);
			 	}else{
			 		this.__bar = $(".acc-progressBar");
			 		this.__title = $(".acc-progressTitle");
			 	}
			__instance = this;
	 	}else{
	 		__instance.__title.text(text);
	 	}
	 	 return __instance;
	 }

	 ProgressBar.prototype.show = function(){

	 	this.__frame.addClass('progressBar-show');
	 	this.__cover.css({
	 		"display":"block",
	 		"opacity":0.7
	 	});
	 	var top = $(window).height()/2;
	 	this.__frame.css({
	 		"top": top + "px",
	 		"margin-top": -this.__frame.height()/2 + "px"
	 	});

		this.__frame.removeClass('acc-progressFrame-hide');

	 }

	  ProgressBar.prototype.hide = function(){
	  	this.__cover.css({
	  		"display": "none",
	  		"opacity":0
	  	});
	  	 this.__frame.addClass('acc-progressFrame-hide');
	  }

	 global.ProgressBar = ProgressBar;

})(window);