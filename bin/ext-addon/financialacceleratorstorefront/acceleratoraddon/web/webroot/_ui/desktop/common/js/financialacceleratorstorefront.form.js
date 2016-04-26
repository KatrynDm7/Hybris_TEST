ACC.insuranceform = {

    $continueBtn: $('#continueBtn'),
    
    bindAll: function()
    {
        this.triggerSave();
        this.bindProgressBarButtons();
        window.setInterval("ACC.insuranceform.lookForClassNames();",100);
        this.changeContinueBtnHref();
    },
    
    triggerSave: function()
    {
        this.$continueBtn.on('click', function() 
        {
            $('.fr-save-final-button button').trigger('click'); 
            return false; 
        });
    },
    
    bindProgressBarButtons: function()
    {
        var $checkoutProgressLinks = $('#checkoutProgress li.disabled').find('a');
        if (this.$continueBtn.length > 0)
        {
            $checkoutProgressLinks.each(function () {
                if ($(this).prop('href') != "") {
                    $(this).on('click', function () {
                        $('.fr-save-final-button button').trigger('click');
                        return false;
                    });
                }
            });
        }
    },

    lookForClassNames: function() {
        var forms = document.getElementsByTagName("form");
        for(i=0;i<forms.length;i++) {
            if(forms[i].className.indexOf("xforms-help-appearance-dialog")>-1) {
                var divs = forms[i].getElementsByTagName("div");
                for(j=0;j<divs.length;j++) {
                    if(divs[j].className.indexOf("yui-dialog")>-1) {
                        var labels = divs[j].getElementsByTagName("label");
                        for(k=0;k<labels.length;k++) {
                            if(labels[k].innerHTML.indexOf("errorMessage")>-1) {
                                var labelHTML = labels[k].innerHTML.replace(/&gt;/gi,">");
                                labels[k].innerHTML = labelHTML.replace(/&lt;/gi,"<");
                            }
                        }
                    }
                }
            }
        }
    },
    changeContinueBtnHref: function()
    {
        $(".pageType-CategoryPage #checkoutProgress a").on('click', function(e){
            ACC.insuranceform.$continueBtn.attr('href',$(this).attr('href') );

        })
    }
    
}

$(document).ready(function()
{
    with(ACC.insuranceform)
    {
        ACC.insuranceform.bindAll();
    }
});

function formPageNextPage() {
    window.location = ACC.insuranceform.$continueBtn.attr('href');
}
