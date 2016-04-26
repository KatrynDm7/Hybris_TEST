ACC.paymentdetails = {

    bindAll: function ()
    {
        $('#selectall').prop('checked', true);

        $('#selectall').click(function(event) {  //on click for the selectall checkbox

             $('input:checkbox').not(this).prop('checked', this.checked);

          });

        $('input:checkbox').click(function(event) {  //on click for any checkbox other than the selectall checkbox

            if( $('#selectall').prop("checked") && $(this).prop("checked")==false)
            {
                 $('#selectall').prop('checked', false);
            }

          if($('input:checkbox').not($('#selectall')).not(':checked').length == 0)
            {
             $('#selectall').prop('checked', true);
            }

        });
    }
};

$(document).ready(function ()
{
    ACC.paymentdetails.bindAll();
});

function switchChangeButton()
    {
        var button = document.getElementById("buttonChangeTo");
        var pm = document.getElementById("id_changeToPM").value;
        if(pm == "") {
            button.disabled = true;
        } else {
            button.disabled = false;
        }
    }  