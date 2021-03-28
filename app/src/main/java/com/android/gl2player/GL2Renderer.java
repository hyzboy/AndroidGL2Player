package com.android.gl2player;

import javax.microedition.khronos.egl.EGLConfig;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.Buffer;

import javax.microedition.khronos.opengles.GL10;

public class GL2Renderer implements GLSurfaceView.Renderer
{
    private boolean update_video = false;

    private final int MAX_DRAW_OBJECT=4;

    private int screen_width,screen_height;

    private Context sv_context;
    private DrawObject draw_object[]={null,null,null,null};
    private int draw_index[];

    GL2Renderer(Context c)
    {
        sv_context=c;

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
        screen_width=width;
        screen_height=height;

        //创建测试图片
        final int size=256;

        Bitmap bmp=Bitmap.createBitmap(size,size,Bitmap.Config.RGB_565);

        for(int row=0;row<size;row++)
            for(int col=0;col<size;col++)
                bmp.setPixel(col,row,((row&1)==(col&1))?Color.WHITE:Color.BLACK);

        setBitmap(0,bmp,0);
    }

    public int GetScreenWidth(){return screen_width;}
    public int GetScreenHeight(){return screen_height;}

    public void GetImage(Buffer buf)
    {
        GLES20.glReadPixels(0,0,screen_width,screen_height,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,buf);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {
        // Do nothing.
        synchronized (this) {
            update_video = false;
        }
    }

    public boolean setBitmap(int index,Bitmap bmp,int rotate)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return(false);

        DrawObject obj=draw_object[index];

        if(obj==null)
        {
            DrawBitmap obj_bmp=new DrawBitmap();
            obj_bmp.render_layout.set(0,0,1,1);       //设定为全屏

            draw_object[index]=obj_bmp;
            obj_bmp.update(bmp,rotate);
        }
        else
        {
            if(obj.GetObjectType()!=DrawObject.ObjectType.Bitmap)
                return(false);

            DrawBitmap obj_bmp= (DrawBitmap) obj;
            obj_bmp.update(bmp,rotate);
        }

        return(true);
    }

    public void setPlay(int index, MediaPlayer mp)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return;

        DrawObject obj=draw_object[index];

        if(obj==null)
        {
            DrawVideo video=new DrawVideo(mp);

            video.play();
        }
        else
        {
            if (obj.GetObjectType() != DrawObject.ObjectType.Video) return;

            DrawVideo video = (DrawVideo) obj;
            video.play();
        }
    }

    public void setStop(int index)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return;

        DrawObject obj=draw_object[index];

        if(obj==null)
            return;

        if(obj.GetObjectType()!=DrawObject.ObjectType.Video)return;

        DrawVideo video= (DrawVideo) obj;

        video.stop();
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

        obj.render_layout.set(left,top,width,height);
        return(true);
    }
}
