package com.android.gl2player;

import javax.microedition.khronos.egl.EGLConfig;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

public class GL2Renderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener
{
    private boolean update_video = false;

    private DrawBitmap camera_bitmap=null;

    @Override
    public void onDrawFrame(GL10 gl)
    {
        synchronized (this) {
            if (update_video) {

                //更新视频画面

                update_video = false;
            }
        }

        GLES20.glGetError();        //清空错误

        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        camera_bitmap.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        //创建测试图片
        final int size=256;

        Bitmap bmp=Bitmap.createBitmap(size,size,Bitmap.Config.RGB_565);

        for(int row=0;row<size;row++)
            for(int col=0;col<size;col++)
                bmp.setPixel(col,row,((row&1)==(col&1))?Color.WHITE:Color.BLACK);

        setBitmap(bmp,0);
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
        if(camera_bitmap==null) {
            camera_bitmap = new DrawBitmap();
            camera_bitmap.render_layout.set(0,0,1,1);       //设定为全屏
        }

        camera_bitmap.update(bmp,rotate);
    }

    public void setPlay(int index, MediaPlayer mp)
    {

    }

    public void setLayout(int index,float left,float top,float width,float height)
    {
        if(index==0)    //摄像机
        {
            camera_bitmap.render_layout.set(left,top,width,height);
        }
        else            //其它
        {

        }
    }
}
