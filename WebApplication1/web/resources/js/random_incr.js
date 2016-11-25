/* 
 * Author : Julien SIE
 */

var d = delay = 5;
var counter = document.getElementById('form1:counter');
counter.innerHTML = delay;

var random_generator = function () {

    counter.innerHTML = --d;

    if (d === 0) {
        var incr = document.getElementById('form1:incr');
        var bound = document.getElementById('form1:bound');
        incr.value = Math.floor((Math.random() * bound.value) + 1);
        /*
         * Fire a change event and trigger the onChange() method provided with AJAX (the listener of the JSF component is a backend bean method.
         * */
        incr.onchange();
        d = delay;
        counter.innerHTML = delay;
    }
};

setInterval(random_generator, 1000);


