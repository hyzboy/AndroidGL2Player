/*
 * Copyright (c) 2020. Engineer-Jsp
 *
 * Any document of this project is owned by Engineer-Jsp;
 * Without the permission of the company, it is forbidden to send any
 * documents of this project to anyone who has nothing to do with the project.
 * About the project author , You can visit he's other open source projects or he's blog
 *
 * CSDN   : https://blog.csdn.net/jspping
 * GitHub : https://github.com/Mr-Jiang
 *
 * Once again explanation, it is forbidden to disclose any documents of the project
 * to anyone who has nothing to do with the project without the permission of
 * the Engineer-Jsp otherwise legal liability will be pursued according to law.
 */
package com.android.gl2player;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

/**
 * mp4 player.
 *
 * @author : Engineer-Jsp
 * @date : Created by 2020/10/12 15:50:47
 */
public class VideoPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final String TAG = "VideoPlayer";

    /**
     * {@link Context} instance object
     */
    private Context context;

    /**
     * surface view id
     */
    private int surfaceId;

    /**
     * {@link MediaPlayer} instance object
     */
    private MediaPlayer mMediaPlayer;

    /**
     * {@link VideoPlayerStateListener} instance object
     */
    private VideoPlayerStateListener videoPlayerStateListener;

    /**
     * {@link MediaPlayerStateListener} instance object
     */
    private MediaPlayerStateListener mediaPlayerStateListener;

    /**
     * {@link VideoView} instance object
     */
    private FullScreenVideoView videoView;

    /**
     * video file absolute path
     */
    private String path;

    /**
     * construction method , initialization {@link MediaPlayer} instance object
     *
     * @param context   {@link Context} instance object
     * @param surfaceId surface view id
     */
    public VideoPlayer(Context context, final int surfaceId) {
        this.context = context;
        this.surfaceId = surfaceId;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    /**
     * set video player state listener
     *
     * @param videoPlayerStateListener {@link VideoPlayerStateListener}
     */
    public void setOnVideoPlayerStateListener(VideoPlayerStateListener videoPlayerStateListener) {
        this.videoPlayerStateListener = videoPlayerStateListener;
    }

    /**
     * set media player state listener
     *
     * @param mediaPlayerStateListener {@link MediaPlayerStateListener}
     */
    public void setOnMediaPlayerStateListener(MediaPlayerStateListener mediaPlayerStateListener) {
        this.mediaPlayerStateListener = mediaPlayerStateListener;
    }

    /**
     * set data source
     *
     * @param path video file absolute path
     */
    public void setDataSource(String path) {
        Log.d(TAG, "setDataSource path = " + path);
        this.path = path;
        if (isUrlValid()) {
            if (mediaPlayerStateListener != null) {
                mediaPlayerStateListener.onPlayStart(path);
            }
        }
    }

    /**
     * determine whether it is a valid url
     *
     * @return true means video file valid
     */
    private boolean isUrlValid() {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File videoFile = new File(path);
        if (!videoFile.exists() || videoFile.isDirectory() || !videoFile.canRead()) {
            return false;
        }
        return true;
    }

    /**
     * start video view play
     */
    public void start() {
        stop();
        if (isUrlValid()) {
            videoView = new FullScreenVideoView(context);
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.start();
                }
            });
            videoView.setVideoPath(path);
            videoView.start();
            if (videoPlayerStateListener != null) {
                videoPlayerStateListener.onPlayStart(videoView, surfaceId);
            }
        }
    }

    /**
     * stop video view play
     */
    private void stop() {
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
            if (videoPlayerStateListener != null) {
                videoPlayerStateListener.onPlayStop(surfaceId);
            }
        }
    }

    /**
     * resume video view play
     */
    public void resume() {
        if (videoView != null) {
            videoView.start();
        }
    }

    /**
     * pause video view play
     */
    public void pause() {
        if (videoView != null) {
            videoView.pause();
        }
    }

    /**
     * configure mp4 player parameter
     *
     * @param surface {@link Surface} instance object
     */
    public void configure(Surface surface) {
        if (mMediaPlayer != null) {
            Log.d(TAG, "configure...");
            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setScreenOnWhilePlaying(true);
        }
    }

    /**
     * media player prepare
     */
    public void prepare() {
        if (isUrlValid() && mMediaPlayer != null) {
            Log.d(TAG, "prepare...");
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                Log.e(TAG, "IOException : " + e.toString());
            }
        }
    }

    /**
     * start play
     */
    public void startPlay() {
        if (isUrlValid() && mMediaPlayer != null) {
            Log.d(TAG, "startPlay...");
            mMediaPlayer.start();
        }
    }

    /**
     * stop play
     */
    public void stopPlay() {
        Log.d(TAG, "stopPlay...");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            if (mediaPlayerStateListener != null) {
                mediaPlayerStateListener.onPlayStop();
            }
        }
        stop();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletion...");
        if (mediaPlayerStateListener != null) {
            mediaPlayerStateListener.onPlayCompleted();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Log.e(TAG, "onError...");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        return true;
    }

    /**
     * video player state listener define.
     */
    public interface VideoPlayerStateListener {

        /**
         * on play start state call back
         *
         * @param view      {@link VideoView} instance object
         * @param surfaceId surface view id
         */
        void onPlayStart(VideoView view, int surfaceId);

        /**
         * on play stop state call back
         *
         * @param surfaceId surface view id
         */
        void onPlayStop(int surfaceId);
    }

    /**
     * media player state listener define.
     */
    public interface MediaPlayerStateListener {

        /**
         * on play start state call back
         *
         * @param path {@link String} video file path
         */
        void onPlayStart(String path);

        /**
         * play completed state call back
         */
        void onPlayCompleted();

        /**
         * on play stop state call back
         */
        void onPlayStop();
    }

}