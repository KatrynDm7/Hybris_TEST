ORBEON.xforms.Events.errorEvent.subscribe(function(eventName, eventData) {
	ORBEON.xforms.Globals.requestIgnoreErrors=true;
	if(eventData[0].title.startsWith('Session has expired')) {
   		window.location.href=/^(http.*\/\/[^/]+\/[^/]+\/?)/.exec(location.href)[1]+'?'+new Date().getTime();
	}
});
