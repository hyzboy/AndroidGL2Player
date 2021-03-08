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

    private final int MAX_DRAW_OBJECT=4;

    private DrawObject draw_object[]={null,null,null,null};
    private int draw_index[];

    GL2Renderer()
    {
        draw_index=new int[MAX_DRAW_OBJECT];

        for(int i=0;i<MAX_DRAW_OBJECT;i++)
            draw_index[i]=i;
    }

    public final int GetMaxDrawObject()
    {
        return MAX_DRAW_OBJECT;
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        synchronized (this) {
            if (update_video) {

                //更新视频画面

                update_video = false;
                for(int i=0;i<MAX_DRAW_OBJECT;i++)
                    if(draw_object[i]!=null)
                        draw_object[i].update();
            }
        }

        GLES20.glGetError();        //清空错误

        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        int index;

        for(int i=0;i<MAX_DRAW_OBJECT;i++)
        {
            index=draw_index[i];

            if(index<0||index>MAX_DRAW_OBJECT)continue;

            draw_object[index].draw();
        }
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

    public boolean setBitmap(int index,Bitmap bmp,int rotate)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return(false);

        DrawObject obj=draw_object[index];

        if(obj==null)return(false);

        if(camera_bitmap==null) {
            camera_bitmap = new DrawBitmap();
            camera_bitmap.render_layout.set(0,0,1,1);       //设定为全屏
        }

        camera_bitmap.update(bmp,rotate);
    }

    public void setPlay(int index, MediaPlayer mp)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return(false);

    }

    /**
     * 设置绘制顺序
     * @param order
     */
    public void setDrawOrder(int[] order)
    {
        for(int i=0;i<MAX_DRAW_OBJECT;i++)
            draw_index[i]=order[i];
    }

    public boolean setLayout(int index,float left,float top,float width,float height)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return(false);

        DrawObject obj=draw_object[index];

        if(obj==null)return(false);

        if(index==0)    //摄像机
        {
            camera_bitmap.render_layout.set(left,top,width,height);
        }
        else            //其它
        {

        }
    }
}
