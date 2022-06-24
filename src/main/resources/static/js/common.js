var index = 0;
$(document).ready(function () {
    $('.menuItem').each(function(){
        if(index == $('#active').val()) {
            $(this).addClass('active');
        }
        index++;
    });
});
(function () {
  'use strict'
  feather.replace({ 'aria-hidden': 'true' });
})();

