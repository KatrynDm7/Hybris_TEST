function addRecommendation(recoId) {
	return function(data){
		if (data !== ''){
			$("#" + recoId ).append(data);
			jQuery('#recommendationUL'+recoId).jcarousel({
				vertical: false
			});
		}	
		else {
			$("#" + recoId ).removeClass();
		}
	}
};
