package com.jarvis.jarvisvideoplayer;

/**
 * @author Jarvis
 * @version 1.0
 * @title VideoPlayer
 * @description 该类主要功能描述
 * @company 北京奔流网络技术有限公司
 * @create 2017/6/1 下午1:18
 * @changeRecord [修改记录] <br/>
 */

public class VideoPlayerManager {
    private VideoPlayer mVideoPlayer;

    private VideoPlayerManager() {

    }

    private static VideoPlayerManager sInstance;

    public static synchronized VideoPlayerManager getInstance() {
        if (sInstance == null) {
            sInstance = new VideoPlayerManager();
        }
        return sInstance;
    }

    public void setCurrentVideoPlayer(VideoPlayer videoPlayer) {
        this.mVideoPlayer = videoPlayer;
    }

    public void releaseVideoPlayer() {
        if (mVideoPlayer != null) {
            if (mVideoPlayer.isPlaying() || mVideoPlayer.isPaused()) {
                mVideoPlayer.exitTinyWindow();
            }
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    public boolean onBackPressed() {
        if (mVideoPlayer != null) {
            if (mVideoPlayer.isFullScreen()) {
                return mVideoPlayer.exitFullScreen();
            } else if (mVideoPlayer.isTinyWindow()) {
                return mVideoPlayer.exitTinyWindow();
            } else {
                mVideoPlayer.release();
                return false;
            }
        }
        return false;
    }
}
