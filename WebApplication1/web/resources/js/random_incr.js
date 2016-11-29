/* 
 * Author : Julien SIE
 */
/**
 * 
 * TODO : stop the generator if the system is done
 */
var ri = {
    delay: 5,

    timerID: 0,

    start: function () {
        var d = ri.delay;
        var counter = document.getElementById('form1:counter');
        counter.innerHTML = d;

        var random_generator = function () {

            var counter = document.getElementById('form1:counter');
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

        ri.timerID = setInterval(random_generator, 1000);
    },

    stop: function () {
        console.log("Random Generator stopped");
        clearInterval(ri.timerID);
    }
};

ri.delay = 10;
ri.start();

function error_handler(e) {
    ri.stop();
    /*TODO : display the responseCode (chap 13.5 & 13.6 from Java EE 7 Tutorial */
    alert(e.description);
}
