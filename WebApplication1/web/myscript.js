/* 
 * Author : Julien SIE
 */

function display_elts(selector) {

    var qsa = document.querySelectorAll(selector);
    var c = qsa.length;
    var s = selector.split('.')[1];

    if (!c) {
        alert("No element with CSS class :  " + s + " !!!");
    } else {
        console.log("Will find all elements with CSS class : " + s);
        console.log(c + " element" + ((c > 1) ? "s" : ""));

        for (var i = 0; i < c; i++) {
            console.log("Element " + i + " : " + qsa[i].innerHTML + " ==> " + qsa[i].href);
        }
    }
}

display_elts(".lnk");

