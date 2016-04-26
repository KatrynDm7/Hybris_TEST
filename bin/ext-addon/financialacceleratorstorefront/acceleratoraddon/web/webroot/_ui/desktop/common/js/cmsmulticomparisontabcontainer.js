ACC.multitabs = {

    getTabContentUrl: ACC.config.contextPath + "/c/tab/",

    activeTabSwitch: function($main){
        $('.multiTabs .tab').each(function ()
        {
            var $tab = $(this);
            $tab.removeClass('active');
        });
        $main.addClass('active');
    },

    getTabContent: function($main){
        $.get(ACC.multitabs.getTabContentUrl + $main.attr("id"), function (result)
        {
        	var isSessionExpires = $("#isSessionExpires",$(result)).html();       	
        	
        	if(isSessionExpires == 'true')
        	{
        		window.location = ACC.config.contextPath;
        	}
        	
            var $result = $("<div />").append(result).html();
            $('#tab_content').html($result);
        	
            ACC.insurance.bindAll();
        });
    },

    bindTabAction: function(){
        $('.multiTabs .tab').each(function ()
        {
            var $tab = $(this);
            $tab.on('click', function(){
                ACC.multitabs.activeTabSwitch($tab);
                ACC.multitabs.getTabContent($tab);
            });
        });
    }
},

$(document).ready(function()
{
    with(ACC.multitabs)
    {
        ACC.multitabs.bindTabAction();
    }
})