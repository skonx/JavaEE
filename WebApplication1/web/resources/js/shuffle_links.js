/* 
 * Author : Julien SIE
 */

function display_elts(selector) {

    var qsa = document.querySelectorAll(selector);
    var c = qsa.length;

    if (!c) {
        alert("No element with CSS class :  " + selector + " !!!");
    } else {
        console.log("Will find all elements with CSS class : " + selector);
        console.log(c + " element" + ((c > 1) ? "s" : ""));

        for (var i = 0; i < c; i++) {
            console.log("Element " + i + " : " + qsa[i].innerHTML + " ==> " + qsa[i].href);
        }
    }
}

/*
 * Random-shuffle the different links. Display the links before and after the shuffle.
 */
var random_shuffle = function () {
    var selector = 'a';
    var links = document.querySelectorAll(selector);

    //display_elts(selector);

    var count = links.length;

    if (count > 1) {
        var parent = links[0].parentNode;

        var newlinks = new Array(count);

        /*
         * Remove each element, put them in a tmp array... 
         */
        for (var i = 0; i < count; i++) {
            var link = parent.removeChild(links[i]);
            //provide a number between 0 and (count-1).
            var index = Math.floor(((Math.random() * (count - 1)) + 1));
            //if the calculated position is not free, try to provide the next index.
            while (newlinks[index]) {
                console.log("index = " + index);
                index = (index + 1) % count;
                console.log("new index = " + index);
            }
            console.log("set " + link.innerHTML + " in index " + index);
            newlinks[index] = link;
        }

        /*
         * ... And then, add them from the tmp array to the parent element.
         */
        for (var i = 0; i < count; i++)
            parent.appendChild(newlinks[i]);

        // display_elts(selector);
    }



};

setInterval(random_shuffle, 3000);

var div = document.getElementById('div');
var pos = document.getElementById('pos');
var MSG = " - ; - ";
pos.innerHTML = MSG;

window.addEventListener('mousemove', function (e) {
    pos.innerHTML = "X = " + e.clientX + "px ; Y = " + e.clientY + "px";
}, false);

window.addEventListener('mouseout', function (e) {
    pos.innerHTML = MSG;
}, false);
