/* 
 * Author : Julien SIE
 */

var ri = {
    delay: 5,

    start: function () {
        var d = ri.delay;
        var counter = document.getElementById('form1:counter');
        counter.innerHTML = d;

        var random_generator = function () {

            counter.innerHTML = --d;

            if (d === 0) {
                var incr = document.getElementById('form1:incr');
                /*The hidden data in the Facelet...*/
                var bound = document.getElementById('form1:bound');

                incr.value = Math.floor((Math.random() * bound.value) + 1);

                /*
                 * Fire a change event and trigger the onChange() method provided with AJAX (the listener of the JSF component is a backend bean method.
                 * */
                incr.onchange();

                d = ri.delay;
                counter.innerHTML = d;
            }
        };

        setInterval(random_generator, 1000);
    }
};

ri.delay = 10;
ri.start();



