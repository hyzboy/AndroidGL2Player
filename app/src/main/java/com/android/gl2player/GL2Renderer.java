package com.android.gl2player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.android.gl2player.async.GL2Event;
import com.android.gl2player.async.GL2EventSetBitmap;
import com.android.gl2player.async.GL2EventSetLayout;
import com.android.gl2player.drawobject.DrawBitmap;
import com.android.gl2player.drawobject.DrawObject;
import com.android.gl2player.drawobject.DrawText;
import com.android.gl2player.drawobject.DrawVideo;

import java.nio.Buffer;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GL2Renderer implements GLSurfaceView.Renderer
{
    private boolean update_video = false;

    private final int MAX_DRAW_OBJECT=4;

    private int screen_width,screen_height;

    private Context sv_context;
    private DrawObject draw_object[]={null,null,null,null};
    private int draw_index[];

//    private Vector<DrawText> draw_text_list=new Vector<DrawText>();
    DrawText dt=null;

    private Queue<GL2Event> event_queue=new LinkedList<GL2Event>();

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

    private void RunAsyncEvent()
    {
        if(event_queue.size()==0)return;

        for(GL2Event e:event_queue)
            e.run();

        event_queue.clear();
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        RunAsyncEvent();

        GLES20.glGetError();        //清空错误

        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        int index;

        for(int i=0;i<MAX_DRAW_OBJECT;i++)
        {
            index=draw_index[i];

            if(index<0||index>MAX_DRAW_OBJECT) {
                continue;
            }
            if(draw_object[index]!=null){
                draw_object[index].draw();
            }
        }

        if(dt!=null)
            dt.draw();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        screen_width=width;
        screen_height=height;

        Bitmap bitmap=drawableToBitamp(sv_context.getDrawable(R.drawable.tran),screen_width,screen_height);
        setBitmap(0,bitmap,0);

        //创建文字绘制测试对象
        {
            dt=new DrawText();

            dt.setColor(1,1,0,1);
            dt.setPosition(10,10);              //设置绘制位置
            dt.setSize(24);                             //设置字符大小
            dt.setText("Hello,World! 你好，世界！");      //设置文本

            dt.refresh();                               //刷新内容
        }
    }

    private Bitmap drawableToBitamp(Drawable drawable,int w,int h)
    {
        Bitmap bitmap = null;
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config = drawable.getOpacity()
                != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(w,h,config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
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

    public void AsyncSetBitmap(int index,Bitmap bmp,int rotate)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return;

        DrawObject obj=draw_object[index];

        if(obj==null)return;

        event_queue.add(new GL2EventSetBitmap(obj,bmp,rotate));
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

    public void AsyncSetLayout(int index,float left,float top,float width,float height)
    {
        if(index<0||index>=MAX_DRAW_OBJECT)return;
        DrawObject obj=draw_object[index];
        if(obj==null)return;

        event_queue.add(new GL2EventSetLayout(obj,left,top,width,height));
    }
}
