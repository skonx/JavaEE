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
            controller: function ($scope) {

            },
            scope: {
                path: '=', /*server url*/
                seturl: '&'/*the url update function*/
            },
            templateUrl: 'resources/template/preview.html',
            //link: function (scope, element, attrs, controller, transcludeFn)
            link: function (scope, element) {
                var input = {"list": ["0876d8b2-2d28-40bc-8f7f-f7854851aa6a.jpg", "08eec9c2-2561-470c-a79e-fe3e6d1889ea.jpg", "0a05fb76-3ff0-4775-8504-cd359b14e2b2.jpg", "1c0b2202-562e-436f-8b0a-8cb8c19f7bc5.jpg", "1e1e3892-1d41-4772-9e54-93ca69b68216.jpg", "441430c1-0d8b-4ea1-910e-2df5390d92d1.jpg", "49653527-4cb6-441c-be85-f65ff53284ef.jpg", "51839831-c4cc-49c7-b95f-48c480ebfec9.jpg", "5c2d5cc2-9c84-4af6-8543-55ca623ef19a.jpg", "5cf40897-e28d-4484-9368-b63715ac5ef5.jpg", "652d0dd8-5e66-43f2-87dd-5c52849552d3.png", "681a0e19-1dc2-4e62-8d2a-1ca74b58f98e.jpg", "6a51b2ab-adb1-4280-ba1d-23eeb8adc2d5.jpg", "6ca779cd-169f-46e8-973f-eb5ff8c428df.jpg", "8490ee36-31e0-4dd3-87b9-c545b52155b1.jpg", "9535d7fd-67c4-4018-9d36-7c306660cbae.jpg", "9ccfdf08-c580-47e9-bc6b-b5116a9d2d13.jpg", "a306a14e-f211-4828-8b4a-d78675d667be.jpg", "a83652a8-642b-47fd-91fb-71d2f195078f.jpg", "a8a331a9-f3b4-41a3-84ef-5679ade21f72.jpg", "aa5ff488-73cb-4875-8c3b-712d978c5af8.jpg", "b098dcaf-cbad-455f-9ae5-49fc523bceba.jpg", "c02d9812-a4d0-47bd-a276-5be57bf31954.jpg", "c2ba0348-aed9-4772-8674-d6ca563c0274.jpg", "c3a5e575-f3b4-4c8b-897f-351f90688cf4.jpg", "c8f725d0-8d37-450a-a52d-ada9237b813c.jpg", "d321644f-1e68-4008-8942-c2b5e9b95175.jpg", "d81b2694-10f6-4cb5-97e5-c7b1449853ba.jpg", "dbb45499-9828-4509-b00f-983c05dce2ae.jpg", "df82b5df-b83c-44a1-8d14-f46ca99dc1d3.jpg", "e97256a8-cb36-4b15-9c3a-dc88370fdddd.jpg", "fa3bb917-b512-48a9-bec7-d49de1e9748b.jpg", "fb36ddcb-07d2-484c-ab3a-2b1527257072.jpg", "fc3d6e15-bb81-4a2a-94a3-ce56eb116a35.jpg", "fdda8521-0b5a-456f-9802-47f3a7d1c88c.jpg"]};
                scope.pictures = input.list;
            }
        };
    }]);
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
