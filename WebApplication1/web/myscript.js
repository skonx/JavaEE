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

var shuffle_links = function () {
    var selector = 'a';
    var links = document.querySelectorAll(selector);
    var firstLink, parent, count;

    display_elts(selector);

    count = links.length;
    if (count > 1) {
        parent = links[0].parentNode;
        firstLink = parent.removeChild(links[0]);
        parent.appendChild(firstLink);
    }

    display_elts(selector);

};

var myVar = setInterval(shuffle_links, 3000);
