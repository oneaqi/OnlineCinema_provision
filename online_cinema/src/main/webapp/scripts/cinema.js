$(document).ready(function(){
	$('.popup-trailer').magnificPopup({ 
		disableOn: 700,
        type: 'iframe',
        mainClass: 'mfp-fade',
        removalDelay: 160,
        preloader: false,
        fixedContentPos: false
	});
	
	$('.main .movie:nth-of-type(odd) .movieImage').addClass('animatedOdd');
	$('.main .movie:nth-of-type(even) .movieImage').addClass('animatedEven');
});