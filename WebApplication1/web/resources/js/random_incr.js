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
    /*
     * Generate a random incremental value between 0 and myJSFBean.bound.
     * Random date selection.
     * 
     */

    init: function () {
        var dates = ["1982-11-28", "1983-10-25", "2007-03-17", "2011-04-16", "2015-11-06"];

        var date = new Date();
        var today = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();

        dates.push(today);

        return dates;
    },

    start: function () {
        var d = ri.delay;
        var dates = ri.init();
        var counter = document.getElementById('form1:counter');

        counter.innerHTML = d;

        var random_generator = function () {

            var counter = document.getElementById('form1:counter');
            counter.innerHTML = --d;

            if (d === 0) {
                var incr = document.getElementById('form1:incr');
                /*The hidden data in the Facelet...*/
                var bound = document.getElementById('form1:bound');

                var date = document.getElementById('form1:date');

                incr.value = Math.floor((Math.random() * bound.value) + 1);
                date.value = dates[Math.floor((Math.random() * dates.length))];

                /*
                 * Fire a change event and trigger the onChange() method provided with AJAX (the listener of the JSF component is a backend bean method.
                 * */
                incr.onchange();
                date.onchange();

                d = ri.delay;
                counter.innerHTML = d;
            }
        };

        ri.timerID = setInterval(random_generator, 1000);
    },

    stop: function () {
        console.log("Random Generator stopped");
        clearInterval(ri.timerID);
    },

    error_handler: function (e) {
        var details = msg = '';

        var status = "status : " + e.status;
        var responseCode = "responseCode : " + e.responseCode;
        var description = "description : " + e.description;
        var errorMessage = "errorMessage : " + e.errorMessage;

        var error_details = [status, responseCode, description, errorMessage];

        ri.stop();

        for (var i = 0; i < error_details.length; i++) {
            details += (error_details[i] ? (error_details[i] + '\n') : '');
        }

        msg = "/!\\ ERROR : " + e.status + " - " + e.responseCode + " /!\\\n";
        msg += "Get details?";

        if (confirm(msg)) {
            alert(details);
        }
    }
};

ri.delay = 10;
ri.start();
