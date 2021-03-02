package com.android.gl2player;

import javax.microedition.khronos.egl.EGLConfig;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

public class GL2Renderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener
{
    private FullscreenQuadUV mQuadUV=new FullscreenQuadUV();

    private boolean update_video = false;



    GL2Renderer()
    {
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        synchronized (this) {
            if (update_video) {

                //更新视频画面

                update_video = false;
            }
        }

        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        // Do nothing.
        synchronized (this) {
            update_video = false;
        }
    }

    @Override
    public synchronized void onFrameAvailable(SurfaceTexture surface) {
        update_video = true;
    }

    public void setBitmap(Bitmap bmp,int rotate)
    {
    }

    public void setPlay(int index, MediaPlayer mp)
    {

    }

    public void setLayout(int index,float left,float top,float width,float height)
    {

    }
}
