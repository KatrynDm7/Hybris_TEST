ACC.liveEdit = {

	bindAll: function ()
	{
		this.bindGlobalDoubleClick();
	},

	bindGlobalDoubleClick: function ()
	{
		$('body').click(function (event)
		{
			event.preventDefault();
		});
		
		var _this = this;
		
		// Hook click event on body element to load CMS Component editor
		$('body').dblclick(function (event)
		{
			var cmsComponent = ACC.liveEdit.findNearestCMSComponent(event);
			if (cmsComponent.length > 0)
			{
				var pageUid = ACC.previewCurrentPageUid;
				var position = _this.getSlotPosition($(cmsComponent));
				var cmsComponentUid = cmsComponent.data('cmsComponent');
				var cmsContentSlotUid = cmsComponent.data('cmsContentSlot');

				ACC.liveEdit.displayCMSComponentEditor(pageUid, position, cmsComponentUid, cmsContentSlotUid);
				return false;
			}
		});
	},

	findNearestCMSComponent: function (event)
	{
		return $(event.target).closest('.yCmsComponent');
	},

	displayCMSComponentEditor: function (pageUid, position, cmsComponentUid, cmsContentSlotUid)
	{
		if (undefined != cmsComponentUid && cmsComponentUid != "")
		{
            parent.postMessage({eventName:'notifyIframeZkComponent', data: [pageUid, position, cmsComponentUid, cmsContentSlotUid]},'*');
		}
	},
	
	getSlotPosition: function($component) {
		var slot_name = ".yCmsContentSlot";
		var slotComponent = $component.closest(slot_name);
		if(slotComponent && slotComponent.data('cmsContentSlotPosition') != undefined) {
			return slotComponent.data('cmsContentSlotPosition');
		} else {
			return "";
		}
		
	}
};