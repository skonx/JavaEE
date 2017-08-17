/* 
 * Author : Julien SIE
 */

if (typeof sl === 'undefined') {
    var sl = {start: function () {

            var pos = document.getElementById('pos');
            var MSG = " - ; - ";
            pos.innerHTML = MSG;

            window.addEventListener('mousemove', function (e) {
                pos.innerHTML = "X = " + e.clientX + "px ; Y = " + e.clientY + "px";
            }, false);

            window.addEventListener('mouseout', function (e) {
                pos.innerHTML = MSG;
            }, false);
        }
    };
} else {
    alert("ERROR : 'sl' namespace in shuffle_links.js already exists !");
}

sl.start();