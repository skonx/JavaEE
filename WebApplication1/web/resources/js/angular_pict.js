var app = angular.module("pictTest", []);

app.controller("ImgSrcCtrl", function ($scope) {
    $scope.pict = '';
    $scope.path = 'webresources/images/';
    $scope.picturl = '';

    $scope.setUrl = function (pict) {
        $scope.pict = pict;
        $scope.picturl = $scope.path + $scope.pict;
    };
});

app.directive('tdvUploader', ['$http', function ($http) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                path: '=',
                seturl: '&'
            },
            templateUrl: 'resources/template/uploader.html',
            //link: function (scope, element, attrs, controller, transcludeFn)
            link: function (scope, element) {
                scope.uploading = false;

                scope.switch_uploading = function () {
                    scope.uploading = !scope.uploading;
                };

                element.on('change', function () {
                    scope.$apply(function () {
                        scope.switch_uploading();
                    });

                    var fileInput = document.querySelector('#file');
                    var progressbar = document.querySelector('#progress');

                    var total = p = 0;

                    //compute the total size 
                    for (var i = 0; i < fileInput.files.length; i++) {
                        total += fileInput.files[i].size;
                    }

                    progressbar.max = total;

                    for (var i = 0; i < fileInput.files.length; i++) {
                        filename = fileInput.files[i].name;

                        $http({
                            method: 'POST',
                            url: scope.path,
                            headers: {//remove the default headers (application/json...)
                                'Content-Type': undefined
                            },
                            //update progress bar status
                            uploadEventHandlers: {
                                progress: (function (index) {
                                    return function (e) {

                                        console.log("[" + fileInput.files[index].name + "] - progress : " + ((e.loaded * 100) / e.total) + " % ");
                                        //p += (e.total - e.loaded);

                                        if (e.loaded === e.total) {
                                            p += e.total;
                                        }

                                        progressbar.value = e.loaded + p;

                                        //if all POST are done, hide the progress bar
                                        if (p === total)
                                            scope.switch_uploading();
                                    };
                                })(i)
                            },
                            data: fileInput.files[i]
                        }).then(
                                function (response) {
                                    scope.seturl({pict: response.data});
                                },
                                function (response) {
                                    scope.switch_uploading();
                                    var errmsg = '[ Error ] Upload failed : ' + fileInput.files[i].name + '\n';
                                    errmsg += 'Status : ' + response.status + ' ' + response.statusText;
                                    alert(errmsg);
                                }
                        );
                    }
                });
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
