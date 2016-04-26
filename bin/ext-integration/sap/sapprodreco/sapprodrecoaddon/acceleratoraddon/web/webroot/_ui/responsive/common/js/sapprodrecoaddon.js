function addRecommendation(recoId) {
    return function (data) {
        var $recoComponent = $("#" + recoId);

        if (data !== '') {
            $recoComponent.append(data);
            jQuery('#recommendationUL' + recoId).owlCarousel(ACC.carousel.carouselConfig.default);
            $recoComponent.addClass('initialized');
        }
        else {
            $recoComponent.remove();
        }
    }
}

function loadRecommendation() {
    var divs = document.getElementsByClassName("sap-product-reco");

    for (var i = 0; i < divs.length; i++) {
        if (divs[i].id.search("reco") > -1) {
            var productCode = $("#" + divs[i].id).attr("data-prodcode");
            var componentId = $("#" + divs[i].id).attr("data-componentId");

            retrieveRecommendations(divs[i].id, productCode, componentId);
        }
    }
}

$(function(){
    loadRecommendation();
});
