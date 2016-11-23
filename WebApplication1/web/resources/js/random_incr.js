/* 
 * Author : Julien SIE
 */

var random_generator = function () {
    var incr = document.getElementById('form1:incr');
    var bound = document.getElementById('form1:bound');
    //TODO : use #{myBeanJSF.bound} instead of a hardcoded value 200.
    incr.value = Math.floor((Math.random() * bound.value) + 1);
    //Fire a change event and trigger the onChange() method provided with AJAX (the listener of the JSF component is a backend bean method.
    incr.onchange();
};

setInterval(random_generator, 5000);


