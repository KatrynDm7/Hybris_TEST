ACC.commerceorg = {

    bindAll: function()
    {
        this.commerceorgInit()
        this.selectDeselectBudgetInit()
        this.selectDeselectUserInit()
        this.selectDeselectLinkInit()
        $("#unittree").treeview();

    },

    commerceorgInit: function()
    {
        if($("#budgetStartDate").length>0 && $("#budgetEndDate").length>0){
            $("#budgetStartDate").datepicker({dateFormat: budgetStartEnd});
            $("#budgetEndDate").datepicker({dateFormat: budgetStartEnd});
            $("#budgetStartDate").datepicker( "option", "appendText",  budgetStartEndHint);
            $("#budgetEndDate").datepicker( "option", "appendText",  budgetStartEndHint);

            $('#editB2bBudgetform').validate({
                rules:{
                    startDate:{
                        required: true,
                        dpDate: true
                    },
                    endDate:{
                        required: true,
                        dpDate: true
                    }
                }
            });
        }

    },

    selectDeselectBudgetInit: function()
    {
        ACC.commerceorg.bindToSelectBudget();
        ACC.commerceorg.bindToDeselectBudget();
    },

    bindToSelectBudget: function()
    {
        $(document).on('click','.selectBudget',function(){
            $.postJSON(this.getAttribute('url'),{}, ACC.commerceorg.selectionCallback);
            return false;
        });

    },

    bindToDeselectBudget: function()
    {
        $(document).on('click','.deselectBudget',function(){
            var url = this.getAttribute('url');
            ACC.commerceorg.bindConfirmDeselectButton(url);
            ACC.commerceorg.bindCancelDeselectButton();

            $.colorbox({
                inline: true,
                height: false,
                width: 300,
                href: "#deselect_budget_warning",
                overlayClose: false,
                onComplete: function ()
                {
                    $(this).colorbox.resize();
                },
                onClosed: function()
                {
                    $('#confirm_deselect').off('click');
                }
            });
        });
    },

    bindConfirmDeselectButton: function(url)
    {
        $(document).on('click','#confirm_deselect',function(){
            $.postJSON(url,{}, ACC.commerceorg.deselectionCallback);
            $.colorbox.close();
        });
    },

    bindCancelDeselectButton: function()
    {
        $(document).on('click','#cancel_deselect',function(){
            $.colorbox.close();
        });
    },

    selectionCallback: function(budget)
    {
        $('#row-' + budget.normalizedCode).addClass("selected");
        $('#span-' + budget.normalizedCode).html($('#enableDisableLinksTemplate').tmpl(budget));
    },

    deselectionCallback: function(budget)
    {
        $('#row-' + budget.normalizedCode).removeClass("selected");
        $('#span-' + budget.normalizedCode).html($('#enableDisableLinksTemplate').tmpl(budget));
    },


    selectDeselectUserInit: function()
    {
        ACC.commerceorg.bindToSelectUser();
        ACC.commerceorg.bindToDeselectUser();
    },

    bindToSelectUser: function()
    {
        $(document).on('click','.selectUser',function(){
            $.postJSON(this.getAttribute('url'), {}, ACC.commerceorg.selectionUserCallback);
            return false;
        });
    },

    bindToDeselectUser: function()
    {
        $(document).on('click','.deselectUser',function(){
            $.postJSON(this.getAttribute('url'), {}, ACC.commerceorg.deselectionUserCallback);
            return false;
        });
    },

    selectionUserCallback: function(user)
    {
        if( typeof user.normalizedUid != 'undefined')
        {
            $('#row-' + user.normalizedUid).addClass("selected");
            $('#selection-' + user.normalizedUid).html($('#enableDisableLinksTemplate').tmpl(user));
            $('#roles-' + user.normalizedUid).html($('#userRolesTemplate').tmpl(user));
        }else{
            $('#row-' + user.normalizedCode).addClass("selected");
            $('#selection-' + user.normalizedCode).html($('#enableDisableLinksTemplate').tmpl(user));
            $('#roles-' + user.normalizedCode).html($('#userRolesTemplate').tmpl(user));
        }
    },

    deselectionUserCallback: function(user)
    {
        if( typeof user.normalizedUid != 'undefined')
        {
            $('#row-' + user.normalizedUid).removeClass("selected");
            $('#selection-' + user.normalizedUid).html($('#enableDisableLinksTemplate').tmpl(user));
            $('#roles-' + user.normalizedUid).html($('#userRolesTemplate').tmpl(user));
        }else{
            $('#row-' + user.normalizedCode).removeClass("selected");
            $('#selection-' + user.normalizedCode).html($('#enableDisableLinksTemplate').tmpl(user));
            $('#roles-' + user.normalizedCode).html($('#userRolesTemplate').tmpl(user));
        }
    },

    selectDeselectLinkInit: function()
    {
        ACC.commerceorg.bindToSelectLink();
        ACC.commerceorg.bindToDeselectLink();
    },

    bindToSelectLink: function()
    {
        $(document).on('click','.selectionLink',function(){
            $.postJSON(this.getAttribute('url'), {}, ACC.commerceorg.selectionCallback);
            return false;
        });
    },

    bindToDeselectLink: function()
    {
        $(document).on('click','.deselectionLink',function(){
            $.postJSON(this.getAttribute('url'), {}, ACC.commerceorg.deselectionCallback);
            return false;
        });
    },


};

$(document).ready(function()
{
    ACC.commerceorg.bindAll();
});



