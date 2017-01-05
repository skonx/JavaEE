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

    var fileInput = document.querySelector('#file');
    var progress = document.querySelector('#progress');
    $scope.uploading = false;

    fileInput.addEventListener('change', function () {

        var xhr = new XMLHttpRequest();

        /* $scope.uploading is not accessible from javascript event handler.
         * This function will switch the uploading value in order to 
         * display (or not) a progress bar
         * */
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
                if (xhr.status < 400)
                    alert('Upload is over ! ');
                else {
                    var errmsg = '[ Error ] Upload failed\n';
                    errmsg += 'Status : ' + xhr.status + ' ' + xhr.statusText;
                    alert(errmsg);
                }

                switch_uploading();
            }
        };

        /*var form = new FormData();
         form.append('file', fileInput.files[0]);*/

        xhr.send(fileInput.files[0]);
    });
});