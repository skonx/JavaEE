var app = angular.module("pictTest", []);

app.controller("ImgSrcCtrl", function ($scope) {
    $scope.pict = 'photo-profil-2016.jpg';
    var path = "http://localhost:8080/WebApplication1/webresources/images/";

    /**
     * By default, the picture will be cached on the client side.
     */
    $scope.picturl = path + $scope.pict;

    /**
     * 
     * If the path include the query parameter cache, 
     * the picture will be cached on the client side
     */
    $scope.setUrl = function () {
        $scope.picturl = path + $scope.pict;
    };
});