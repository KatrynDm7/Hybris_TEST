// LIVE EDIT CONTENT SLOT MENU

(function($) {
	
	// NAMESPACE LiveEditAction
	LiveEditSlotAction = function() {};
	
	LiveEditSlotAction.ADD = function(slotData) {

		var pageId = slotData.pageUid;
		var contentSlotId = slotData.slotUid;
		var position = slotData.position;
		
		if (pageId != "") {
            parent.postMessage({eventName:'notifyIframeZkComponentCreate', data: [pageId, position, contentSlotId]},'*');
		}
	};
	
	LiveEditSlotAction.SHOW_ALL = function(slotData) {

		var pageId = slotData.pageUid;
		var contentSlotId = slotData.slotUid;
		var position = slotData.position;
		
		if (pageId != "") {
            parent.postMessage({eventName:'notifyIframeZkComponentShowAll', data: [pageId, position, contentSlotId]},'*');
		}
	};
	
	LiveEditSlotAction.OVERRIDE = function(slotData) {
		
		var pageId = slotData.pageUid;
		var position = slotData.position;
		
		if (pageId != "") {
            parent.postMessage({eventName:'notifyIframeZkSlotCreate', data: [pageId, position, "OVERRIDE"]},'*');
		}
	};
	
	LiveEditSlotAction.OVERRIDE_REVERSE = function(slotData) {
		
		var pageId = slotData.pageUid;
		var position = slotData.position;
		var slotId = slotData.slotUid;
		
		if (pageId != "") {
            parent.postMessage({eventName:'notifyIframeZkSlotRemove', data: [pageId, position, slotId]},'*');
		}
	};
	
	LiveEditSlotAction.LOCK = function(slotData) {
		
		var pageId = slotData.pageUid;
		var position = slotData.position;
		var slotId = slotData.slotUid;
		
		if (pageId != "") {
            parent.postMessage({eventName:'notifyIframeZkSlotLock', data: [pageId, position, slotId]},'*');
		}
	};

    LiveEditSlotAction.UNLOCK = function(slotData) {

        var pageId = slotData.pageUid;
        var position = slotData.position;
        var slotId = slotData.slotUid;

        if (pageId != "") {
            parent.postMessage({eventName:'notifyIframeZkSlotLock', data: [pageId, position, slotId]},'*');
        }
    };

	LiveEditSlotAction.SYNC = function(slotData) {
		
		var pageId = slotData.pageUid;
		var position = slotData.position;
		var slotId = slotData.slotUid;
		
		if (pageId != "") {
            parent.postMessage({eventName:'notifyIframeZkItemSync', data: [slotId]},'*');
		}
	};
	
})(jQuery);