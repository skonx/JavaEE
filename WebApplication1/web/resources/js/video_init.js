var app = angular.module('videoStreamApp', []);

app.controller('VideoStreamCtrl', ['$scope',
    function ($scope) {
    }
]);

app.directive('tdvVid', ['$document', function ($document) {
        return {
            restrict: 'AE',
            transclude: true,
            scope: {
                id: '@tagid',
                src: '@',
                type: '@'
            },
            link: function (scope, elmt) {
                //var video = document.getElementsByTagName('video')[scope.nbr];
                //try children function instead of find.
                var video = elmt.children('video')[0];
                console.log(video);
                if (video) {
                    console.log("$scope.id=" + scope.id);
                    console.log("$scope.src=" + scope.src);
                    console.log("$scope.type=" + scope.type);


                    /* Will check if a seek occured, if yes, another track should 
                     * be created and track 0 should be stopped */
                    video.addEventListener("ended", function () {
                        if ((video.duration !== video.played.end(0)) || video.played.start(0)) {
                            var msg = "\nStart from : " + video.played.start(0);
                            msg += "\nTo first track end : " + video.played.end(0);
                            console.error(msg);
                        } else
                            console.log("Thanks for watching ; )");
                    });
                    /* Duration is computed when metadata are loaded */
                    video.addEventListener('loadedmetadata', function () {
                        console.log("video duration = " + video.duration);
                    });
                    /* Will force the video to continue from the lastest recorded position 
                     * if the user skip to a previous or futur position */
                    var lastpos = 0;
                    video.addEventListener('timeupdate', function () {
                        /* seeking range : between lastpos & laspos+1.5s */
                        if (video.currentTime > lastpos + 1.5 || video.currentTime < lastpos) {
                            console.log('timeupdate');
                            console.log("lastpos=" + lastpos);
                            console.log("video.currentTime=" + video.currentTime);
                            video.currentTime = lastpos;
                        } else
                            lastpos = video.currentTime;
                    });

                    //video.muted = true;
                    video.src = scope.src + "?d=" + Date.now();
                    ;
                    video.type = scope.type;
                    //video.preload = "auto";

                } else {
                    console.error("Cannot load " + scope.id + " from the DOM");
                }
            },
            template: '<video id="{{id}}" width="320" height="400" controls ng-transclude></video>'
        };
    }
]);

