var app = angular.module("pictTest", []);

app.controller("ImgSrcCtrl", function ($scope) {
    $scope.uploading = false;
    $scope.pict = '';
    var path = "http://localhost:8080/WebApplication1/webresources/images/";

    /**
     * 
     * If the path include the query parameter cache, 
     * the picture will be cached on the client side
     */
    $scope.setUrl = function () {
        if ($scope.pict)
            $scope.picturl = path + $scope.pict;
    };
});

app.directive('trdvUploader', [function () {
        return {
            restrict: 'E',
            transclude: true,
            scope: false,
            templateUrl: 'resources/template/uploader.html',
            link: function (scope, element, attrs, controller, transcludeFn) {
                scope.uploading = true;
                scope.pict = '488d1709-3394-4de0-8c65-7521d796001d.jpg';
                scope.setUrl();
            }
        };
    }]);

/*var fileInput = document.querySelector('#file');
 var progress = document.querySelector('#progress');
 
 fileInput.addEventListener('change', function () {
 
 var xhr = new XMLHttpRequest();
 var switch_uploading = function () {
 var upload = document.getElementById('upload');
 var scope = angular.element(upload).scope();
 scope.$apply(scope.uploading = !scope.uploading);
 };
 
 switch_uploading();
 
 xhr.open('POST', path);
 
 xhr.upload.addEventListener('progress', function (e) {
 progress.value = e.loaded;
 progress.max = e.total;
 });
 
 xhr.addEventListener('abort', function () {
 switch_uploading();
 alert('Upload aborted !');
 });
 
 xhr.onreadystatechange = function () {
 if (xhr.readyState === XMLHttpRequest.DONE) {
 if (xhr.status < 400) {
 alert('Upload is over ! ');
 console.log("xhr.responseText = " + xhr.responseText);
 $scope.pict = xhr.responseText;
 } else {
 var errmsg = '[ Error ] Upload failed\n';
 errmsg += 'Status : ' + xhr.status + ' ' + xhr.statusText;
 alert(errmsg);
 }
 
 switch_uploading();
 }
 };
 
 xhr.send(fileInput.files[0]);
 });*/