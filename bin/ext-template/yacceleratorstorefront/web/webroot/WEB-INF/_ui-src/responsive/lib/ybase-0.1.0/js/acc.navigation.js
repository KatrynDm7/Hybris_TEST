ACC.navigation = {

	_autoload: [
		"offcanvasNavigation",
		"myAccountNavigation"
	],

	offcanvasNavigation: function(){

		enquire.register("screen and (max-width:"+screenSmMax+")", {

			match : function() {

				$(document).on("click",".js-enquire-offcanvas-navigation .js-enquire-has-sub > a",function(e){
					e.preventDefault();
					$(".js-enquire-offcanvas-navigation > ul").addClass("active");
					$(".js-enquire-offcanvas-navigation .js-enquire-has-sub").removeClass("active");
					$(this).parent(".js-enquire-has-sub").addClass("active");
				})


				$(document).on("click",".js-enquire-offcanvas-navigation .js-enquire-sub-close",function(e){
					e.preventDefault();
					$(".js-enquire-offcanvas-navigation > ul").removeClass("active");
					$(".js-enquire-offcanvas-navigation .js-enquire-has-sub").removeClass("active");
				})



			},      
			                            
			unmatch : function() {

				$(".js-enquire-offcanvas-navigation > ul").removeClass("active");
				$(".js-enquire-offcanvas-navigation .js-enquire-has-sub").removeClass("active");

				$(document).off("click",".js-enquire-offcanvas-navigation .js-enquire-has-sub > a");
				$(document).off("click",".js-enquire-offcanvas-navigation .js-enquire-sub-close");


			}  
		
		  
		});

	},

	myAccountNavigation: function(){

        var oDoc = document;
        var aAcctData = [];
        var sSignBtn = "";

        //my account items
        var oMyAccountData = $(".accNavComponent");

        //if(oMyAccountData && oMyAccountData.length > 0){
            //the my Account hook for the desktop
            var oMMainNavDesktop = $(".accNavComponentDesktop > ul");

            //offcanvas menu for tablet/mobile
            var oMainNav = $(".main-navigation > ul.nav.nav-pills");

            if(oMyAccountData){
                var aLinks = oMyAccountData.find("a");
                for(var i = 0; i < aLinks.length; i++){
                    aAcctData.push({link: aLinks[i].href, text: aLinks[i].title});
                }
            }

            //create Welcome User + expand/collapse
            var oUserInfo = $(".md-secondary-navigation ul li.logged_in")
            if(oUserInfo && oUserInfo.length === 1){
                var sUserBtn = '<li class=\"auto hidden-md hidden-lg\"><div class=\"userGroup\">';
                sUserBtn += '<span class="glyphicon glyphicon-user myAcctUserIcon"></span><div class=\"userName\">' + oUserInfo[0].innerHTML + '</div>';
                if(aAcctData.length > 0){
                    sUserBtn += '<a class=\"collapsed\" data-toggle=\"collapse\" data-target=\".offcanvasGroup1\"><span class="glyphicon glyphicon-chevron-up myAcctExp"></span></a>';
                }
                sUserBtn += '</div></li>';
                $(sUserBtn).insertBefore($(oMainNav.children()[0]));
            }

            //create a My Account Top link for desktop - in case more components come then more parameters need to be passed from the backend
            var myAccountHook = $('<a class=\"myAccountLinksHeader collapsed\" data-toggle=\"collapse\" data-target=\"#accNavComponentDesktop\">' + oMyAccountData.data("title") + '</a>' );
            myAccountHook.insertBefore(oMyAccountData);

            //offcanvas items
            for(var i = aAcctData.length - 1; i >= 0; i--){
                var oLink = oDoc.createElement("a");
                oLink.title = aAcctData[i].text;
                oLink.href = aAcctData[i].link;
                oLink.innerHTML = aAcctData[i].text;

                var oListItem = oDoc.createElement("li");
                oListItem.appendChild(oLink);
                oListItem = $(oListItem)
                oListItem.addClass("auto offcanvasGroup1 offcanvasNoBorder hidden-md hidden-lg collapse in");
                oListItem.insertAfter($(oMainNav.children()[0]));
            }

            //create Sign In/Sign Out Button
            if($(".liOffcanvas a") && $(".liOffcanvas a").length > 0){
                sSignBtn += '<li class=\"auto hidden-md hidden-lg liUserSign\" ><a class=\"userSign\" href=\"' + $(".liOffcanvas a")[0].href + '\">' + $(".liOffcanvas a")[0].innerHTML + '</a></li>';
                if(oUserInfo && oUserInfo.length === 1){
                    $(sSignBtn).insertAfter($(oMainNav.children()[aAcctData.length]));
                } else {
                    $(sSignBtn).insertBefore($(oMainNav.children()[0]));
                }
            }

            //desktop
            for(var i = 0; i < aAcctData.length; i++){
                var oLink = oDoc.createElement("a");
                oLink.title = aAcctData[i].text;
                oLink.href = aAcctData[i].link;
                oLink.innerHTML = aAcctData[i].text;

                var oListItem = oDoc.createElement("li");
                oListItem.appendChild(oLink);
                oListItem = $(oListItem)
                oListItem.addClass("auto col-md-4");
                oMMainNavDesktop.get(0).appendChild(oListItem.get(0));
            }

            // collapsible logic for my account
            $('.offcanvasGroup1').on('hidden.bs.collapse', function(){
                $('.offcanvasGroup1 a').hide();
                $('.userGroup span.myAcctExp').removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
            });
            $('.offcanvasGroup1').on('show.bs.collapse', function(){
                $('.offcanvasGroup1 a').show();
                $('.userGroup span.myAcctExp').removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
            });
            $('.offcanvasGroup1').collapse();
            if(oUserInfo && oUserInfo.length === 1 && aAcctData.length > 0){

                $('.userGroup').on("click", function(){
                    var aCollapsibleElements = $('.offcanvasGroup1');
                    if(aCollapsibleElements && $('.offcanvasGroup1').length > 0){
                        if($(aCollapsibleElements[0]).hasClass('in')){
                            aCollapsibleElements.addClass('offcanvasNoBorder').removeClass('offcanvasBorderColor');
                            aCollapsibleElements.collapse('hide');
                        } else {
                            aCollapsibleElements.collapse('show');
                            aCollapsibleElements.removeClass('offcanvasNoBorder').addClass('offcanvasBorderColor');
                        }
                    }
                });
            }
            // collapsible logic for my account

        }


	//}

};