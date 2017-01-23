var video = document.getElementById("video");

window.addEventListener("load", function () {
    //video.muted = true;
    video.src = "webresources/videos?d=" + Date.now();
    video.type = "video/x-m4v";
    //video.preload = "auto";

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
        /*range between lastpos & laspos+1.5s */
        if (video.currentTime > lastpos + 1.5 || video.currentTime < lastpos) {
            console.log('timeupdate');
            console.log("lastpos=" + lastpos);
            console.log("video.currentTime=" + video.currentTime);
            video.currentTime = lastpos;
        } else
            lastpos = video.currentTime;
    });
});