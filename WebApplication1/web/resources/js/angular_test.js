var app = angular.module("myWebApp1", []);

app.controller("ValueCtrl", function ($scope) {
    $scope.incr = 0;
    console.warn("Controller ValueCtrl : loaded");

    function check() {
        console.warn("Angular CHECK() - Value incr changed : " + $scope.incr);
    }

    $scope.check_onchange = function () {
        console.warn("Angular CHECK_ONCHANGE() - Value incr changed : " + $scope.incr);
    };

    $scope.$watch('incr', check);
});