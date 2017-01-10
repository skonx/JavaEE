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

app.directive('tdvUploader', ['$http', '$interval', function ($http, $interval) {
        return {
            restrict: 'E',
            transclude: true,
            controller: function ($scope) {
                $scope.uploading = false;

                $scope.switch_uploading = function () {
                    $scope.uploading = !$scope.uploading;
                };

                /*compute the total size and init the progress_status array*/
                $scope.init_status = function (fileInput, progress_status) {
                    var total = 0;
                    for (var i = 0; i < fileInput.files.length; i++) {
                        total += fileInput.files[i].size;
                        progress_status[i] = {
                            load: 0, /*delta between the previous and current event*/
                            loaded: 0, /*previous e.loaded value*/
                            total: fileInput.files[i].size,
                            name: fileInput.files[i].name
                        };
                    }
                    return total;/*total amount of data to POST*/
                };

            },
            scope: {
                path: '=', /*server url*/
                seturl: '&'/*the url update function*/
            },
            templateUrl: 'resources/template/uploader.html',
            //link: function (scope, element, attrs, controller, transcludeFn)
            link: function (scope, element) {

                element.on('change', function () {
                    scope.switch_uploading();

                    var fileInput = document.querySelector('#file');
                    var progressbar = document.querySelector('#progress');

                    /*Init the status properties*/
                    var overall_progress = 0;
                    var progress_status = [];
                    var total = scope.init_status(fileInput, progress_status);

                    /*Set the max value of the progress bar*/
                    console.log('total=' + total);
                    progressbar.max = total;
                    progressbar.value = 0;

                    var refresh_progressbar = function () {
                        console.log('progressbar REFRESH');
                        progressbar.value = overall_progress;
                    };

                    /*Refresh the progressbar every 200ms*/
                    var interval = $interval(refresh_progressbar, 200);

                    /*Asynchronous POST of each file */
                    for (var i = 0; i < fileInput.files.length; i++) {
                        //should be moved in  dedicated service...
                        $http({
                            method: 'POST',
                            url: scope.path,
                            headers: {
                                /*remove the default headers (application/json...)*/
                                'Content-Type': undefined
                            },
                            /*update progress bar status*/
                            uploadEventHandlers: {
                                progress: (function (index) {
                                    return function (e) {

                                        //console.log("[" + progress_status[index].name + "] - progress : " + ((e.loaded * 100) / e.total) + " % ");
                                        /*Compute the delta*/
                                        progress_status[index].load = e.loaded - progress_status[index].loaded;
                                        /*Save the last progression*/
                                        progress_status[index].loaded = e.loaded;
                                        /*Update the overall progression with the delta*/
                                        overall_progress += progress_status[index].load;

                                        //if all POST are done, hide the progress bar
                                        if (overall_progress === total) {
                                            scope.switch_uploading();//hide the progressbar
                                            $interval.cancel(interval);//stop the recurrent timer
                                        }
                                    };
                                })(i)/*Closure to protect i value*/
                            },
                            data: fileInput.files[i]
                        }).then(
                                function (response) {
                                    /* Set the URL if there is a single file to POST. 
                                     * Otherwise, lost GET requests should be expected... 
                                     * ... and broken pipes on the server*/
                                    if (fileInput.files.length === 1)
                                        scope.seturl({pict: response.data});
                                },
                                function (response) {/*Error callback*/
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

app.directive('tdvPreview', ['$http', '$interval', function ($http, $interval) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                path: '=', /*server url*/
                service: '@',
                seturl: '&'/*the url update function*/
            },
            //link: function (scope, element, attrs, controller, transcludeFn)
            link: function (scope, element) {
                var path = scope.path + scope.service;

                /*Gets the list of the pictures stored in the vault*/
                scope.refresh = function () {
                    $http({
                        method: 'GET',
                        url: path
                    }).then(
                            function (response) {
                                /*success callback*/
                                scope.pictures = response.data.list;
                                scope.total = response.data.list.length;
                            },
                            function (response) {
                                /*error callback*/
                                var errmsg = '[ Error downloading pictures list ]\n';
                                errmsg += 'Status : ' + response.status + ' ' + response.statusText;
                                console.log(errmsg);
                            });
                };
            },
            templateUrl: 'resources/template/preview.html'
        };
    }
]);
/*javascript code*/
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
