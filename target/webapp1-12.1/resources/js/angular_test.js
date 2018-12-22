var app = angular.module("myWebApp1", ['ngAnimate']);

app.controller("ValueCtrl", function ($scope) {
    console.warn("# Controller ValueCtrl : loaded #");

    /* Called when $scope.incr is changed */
    function check() {
        console.warn("Angular CHECK() - Value incr changed : " + $scope.incr + ' and Bound is still ' + $scope.bound);

        /* the background-color of the fourth progress bar will be overriden 
         * and smoothly colored using the incr computed value*/
        var incrset = document.getElementById('form1:pbar4:bar');

        var red = Math.floor(($scope.incr * 255) / $scope.bound) % 256;
        angular.element(incrset).css("background-color", "rgb(" + red + ",0,0)");

    }

    /* Called when $scope.incr is changed 
     * BUT won't be called if the value is changed 
     * from outside of the scope (using $apply for exemple) !!! */
    $scope.check_onchange = function () {
        console.warn("Angular CHECK_ONCHANGE() - Value incr changed : " + $scope.incr);
    };

    $scope.reset = function () {
        console.warn("Angular RESET() - Value incr  = " + $scope.incr);
        $scope.incr = 0;
        console.warn("Angular RESET() - Value incr  has been reset to " + $scope.incr);
    };

    $scope.$watch('incr', check);

    function check_scroll_position(elmt_name) {
        var elmt = document.getElementById(elmt_name);

        if (elmt) {
            var elmt_top = elmt.getBoundingClientRect().top;

            /* will check if the top of the scrolling window is in the 
             * range of the top/bottom of the element. */
            if (((-elmt.offsetHeight) <= elmt_top) && (elmt_top <= 0))
                console.log("You're scrolling ON " + elmt_name);
            else
                console.log("You're scrolling OUT of " + elmt_name);

            /*console.log("SCROLLING ; top of " + elmt_name + ": " + elmt_top);
             console.log("SCROLLING ; elmt.offsetHeight = " + elmt.offsetHeight);
             console.log("SCROLLING; window.scrollY = " + window.scrollY);*/
        }
    }

    /* Will check for a specified set of elements 
     * if the top of the scrolling window is over them or not*/
    window.addEventListener('scroll', function () {
        var elmts = ['form1:intro', 'form1:pbar1:bar', 'form1:pbar2:bar', 'form1:pbar3:bar'];
        elmts.forEach(check_scroll_position);
    });

});