/* 
 * Author : Julien SIE
 */

var ri = {
    //the default delay 
    delay: 10,

    //the id of the started time interval
    timerID: 0,

    //the lock will be automatically set when start() is called and removed when stop() is called
    lock: false,

    /* The status of the two field form1:incr & form:date after a value has changed
     * When an ajax request is sent, status will be changed to false,
     * meaning the client will wait a success reply from the server. 
     * Otherwise, generator is stopped until value or manually changed.
     * */
    status: [true, true],

    //test the display data are uptodate
    isup2date: function () {
        return ri.status[0] && ri.status[1];
    },

    //flag the status for the fields form1:incr or form1:date
    setStatus: function (id, flag) {
        switch (id) {
            case 'form1:incr':
                ri.status[0] = flag;
                break;
            case 'form1:date':
                ri.status[1] = flag;
                break;
            default:
                console.log(id + " is not a valid element !");
                break;
        }
    },

    //test if all data are refreshed and if there is no lock (no time interval started)
    isReady: function () {
        return ri.isup2date() && !ri.lock;
    },

    //init a pool with somes dates
    init: function () {
        var dates = ["1982-11-28", "1983-10-25", "2007-03-17", "2011-04-16", "2015-11-06"];

        var date = new Date();
        var today = date.toISOString().split("T")[0];
        console.warn('today = ' + today);
        dates.push(today);

        return dates;
    },

    /*
     * Generate a random incremental value between 0 and myJSFBean.bound.
     * Random date selection.
     */
    start: function () {
        if (ri.isReady()) {
            ri.lock = true;
            var d = ri.delay;
            var dates = ri.init();
            var counter = document.getElementById('form1:counter');

            counter.innerHTML = d;

            var start_id = 'start_id_' + Math.floor((Math.random() * 10000000) + 1);

            console.warn("START [" + start_id + "]");

            var random_generator = function () {
                //if a reset occurs, counter must be found...
                counter = document.getElementById('form1:counter');
                counter.innerHTML = --d;
                //console.log(start_id + " , d=" + d);

                if (d === 0) {
                    var incr = document.getElementById('form1:incr');

                    /*The hidden data in the Facelet...*/
                    var bound = document.getElementById('form1:bound');

                    var date = document.getElementById('form1:date');

                    var textarea = document.getElementById('form1:textarea');

                    incr.value = Math.floor((Math.random() * bound.value) + 1);

                    /*access to the angular scope and change the value*/
                    var scope = angular.element(incr).scope();
                    scope.$apply(function () {
                        scope.incr = incr.value;
                    });

                    date.value = dates[incr.value % dates.length];

                    if (!date.value) {
                        console.error("date is null !!!");
                        console.error("dates[" + incr.value % dates.length + "] = " + dates[incr.value % dates.length]);
                        console.error("today = " + new Date());

                        for (var i = 0; i < dates.length; i++) {
                            console.log("dates[" + i + "] = " + dates[i]);
                        }
                    }

                    /*
                     * Fire a change event and trigger the onChange() method provided 
                     * with AJAX (the listener of the JSF component is a backend bean method.
                     * */
                    incr.onchange();
                    date.onchange();

                    textarea.value += (incr.value + " ");
                    textarea.onblur();

                    /* Change the value of the fourth jsf progressbar. 
                     * setValue() is a method provided during the JSF component rendering... 
                     * Shoud be really smarter to use an Angular directive instead. 
                     * But anyway, it's just for a test */
                    var pbar4 = document.getElementById('form1:pbar4:base');
                    pbar4.setValue(incr.value);

                    console.log(start_id + " incr = " + incr.value);
                    console.log(start_id + " date = " + date.value);

                    counter.innerHTML = '--';

                    ri.stop(start_id);

                }

                /* It's an issue, so it's not necessary to iterate*/
                if (d < 0) {
                    counter.innerHTML = '--';
                    ri.stop(start_id);
                }
            };

            ri.timerID = setInterval(random_generator, 1000);
        }

    },

    /**
     * Stop the running generator.
     * Set the refresh status to false (waiting a refresh from AJAX or from the user).
     * Unlock the generator.
     * @param {type} id the id of the running generator
     */
    stop: function (id) {
        console.warn("Random Generator [" + id + "] stopped");
        clearInterval(ri.timerID);
        ri.status[0] = ri.status[1] = false;
        ri.lock = false;
    },

    /**
     * Display an alert message if an error occurs. 
     * @param {type} e the event generated by the AJAX request if an error occurs
     * @returns {undefined}
     */
    error_handler: function (e) {
        var msg;
        var details = msg = '';

        var status = "status : " + e.status;
        var responseCode = "responseCode : " + e.responseCode;
        var description = "description : " + e.description;
        var errorMessage = "errorMessage : " + e.errorMessage;

        var error_details = [status, responseCode, description, errorMessage];

        //only if something is running
        if (ri.lock)
            ri.stop('error_handler');

        for (var i = 0; i < error_details.length; i++) {
            details += (error_details[i] ? (error_details[i] + '\n') : '');
        }

        msg = "/!\\ ERROR - SERVER CONNECTION LOST /!\\\n";
        msg += "Get details?";

        if (confirm(msg)) {
            alert(details);
        }
    },

    /**
     * Help to monitor the AJAX request. If the status is 'success', the generator can be restarted.
     * @param {type} e the event generated by the AJAX request
     * @returns {undefined}
     */
    monitor_status: function (e) {
        console.log("ajax response : " + e.status + " for element " + e.source.id);
        if (e.status === 'success') {
            ri.setStatus(e.source.id, true);
            ri.start();
        }
    }
};

window.addEventListener('load', ri.start);
