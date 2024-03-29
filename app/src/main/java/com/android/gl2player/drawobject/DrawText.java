package com.android.gl2player.drawobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES20;

import com.android.gl2player.gl.GL2Texture;

public class DrawText extends DrawObject
{
    private final int TEXT_EDGE=4;          //文本边界

    private Bitmap text_bitmap=null;
    private Canvas text_canvas=null;
    private Paint text_paint=new Paint();

    private GL2Texture texture=null;
    private ShaderText shader=new ShaderText();

    private String text;
    private float size=0;

    private int text_height=-1;
    private int bmp_width =-1;
    private int bmp_height =-1;

    private float draw_left=0;
    private float draw_top=0;

    private float surface_width=-1;
    private float surface_height=-1;

    private float color[]={1,1,0,1};

    private int power_to_2(int value)
    {
        int i=1;

        while(i<value)
            i<<=1;

        return i;
    }

    public DrawText()
    {
        super(ObjectType.Text);

        text_paint.setAntiAlias(true);
        text_paint.setARGB(0xFF,0xFF,0xFF,0xFF);
    }

    public void setSurfaceSize(int w,int h)
    {
        surface_width=w;
        surface_height=h;
    }

    public void setPosition(int l,int t)
    {
        draw_left=l;
        draw_top=t;
    }

    public void setColor(float r,float g,float b,float a)
    {
        color[0]=r;
        color[1]=g;
        color[2]=b;
        color[3]=a;
    }
    public void setText(String str){text=str;}
    public void setSize(int s){size=s;text_paint.setTextSize(size);}
    public void refresh()
    {
        if(text.length()<=0||size<=0)
            return;

        Rect bounds=new Rect();
        text_paint.getTextBounds(text,0,text.length(),bounds);

        text_height=bounds.height();

        int w=power_to_2(bounds.width());
        int h=power_to_2(bounds.height());

        if(w> bmp_width ||h> bmp_height)
        {
            text_canvas=null;
            text_bitmap=null;
            texture=null;

            bmp_width =w;
            bmp_height =h;
        }

        if(text_bitmap==null)
            text_bitmap=Bitmap.createBitmap(bmp_width, bmp_height,Bitmap.Config.ALPHA_8);

        if(text_canvas==null)
            text_canvas=new Canvas(text_bitmap);

        text_bitmap.eraseColor(0);

        text_canvas.drawText(text,0, text_height-1,text_paint);

        texture=new GL2Texture();
        texture.init(GLES20.GL_TEXTURE_2D,text_bitmap);

        text_bitmap.recycle();

        render_layout.set(  draw_left/surface_width,
                            draw_top/surface_height,
                            bmp_width/surface_width,
                            bmp_height/surface_height);
    }

    @Override
    public void update(){}

    @Override
    public void draw()
    {
        if(texture==null)return;

        ClearGLError();
        GLES20.glEnable(GLES20.GL_BLEND);
        CheckGLError("glEnable(BLEND)");
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);
        CheckGLError("glBlendFunc(SRC_ALPHA,ONE_MINUS_SRC_ALPHA)");
        shader.begin();
            texture.bind(0);
            render_layout.bind(shader.maPositionHandle);
            texture_uv.bind(shader.maTexCoordHandle);
            shader.SetTextColor(color);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();
    }
}
