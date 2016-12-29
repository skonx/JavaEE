var app = angular.module("myWebApp1", ['ngAnimate']);

app.controller("ValueCtrl", function ($scope) {
    console.warn("# Controller ValueCtrl : loaded #");

    function check() {
        console.warn("Angular CHECK() - Value incr changed : " + $scope.incr + ' and Bound is still ' + $scope.bound);

        //the background-color of the fourth progress bar will be overriden and fade using the incr computed value
        var incrset = document.getElementById('form1:pbar4:bar');

        var red = Math.floor(($scope.incr * 255) / $scope.bound) % 256;
        angular.element(incrset).css("background-color", "rgb(" + red + ",0,0)");

    }

    //won't be called if the value is changed from outside of the scope !
    $scope.check_onchange = function () {
        console.warn("Angular CHECK_ONCHANGE() - Value incr changed : " + $scope.incr);
    };

    $scope.reset = function () {
        console.warn("Angular RESET() - Value incr  = " + $scope.incr);
        $scope.incr = 0;
        console.warn("Angular RESET() - Value incr  has been reset to " + $scope.incr);
    };

    $scope.$watch('incr', check);
});