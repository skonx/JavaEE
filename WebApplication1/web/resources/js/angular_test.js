var app = angular.module("myWebApp1", []);

app.controller("ValueCtrl", function ($scope) {
    console.warn("# Controller ValueCtrl : loaded #");

    function check() {
        console.warn("Angular CHECK() - Value incr changed : " + $scope.incr);
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