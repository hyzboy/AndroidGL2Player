package com.android.gl2player.drawobject;

import com.android.gl2player.gl.GLClass;
import com.android.gl2player.gl.QuadUV;
import com.android.gl2player.gl.RenderLayout;

/**
 * 绘制对象基类
 */
public abstract class DrawObject extends GLClass
{
    protected enum ObjectType
    {
        Bitmap,
        Video,
        Text,
    };

    private ObjectType type;

    protected static final QuadUV texture_uv =new QuadUV();

    protected RenderLayout render_layout=new RenderLayout();

    public DrawObject(ObjectType ot)
    {
        super("DrawObject:"+ot.name());
        type=ot;
        render_layout.init();
    }

    public final ObjectType GetObjectType()
    {
        return type;
    }

    public boolean isBitmap(){return type==ObjectType.Bitmap;}
    public boolean isVideo(){return type==ObjectType.Video;}
    public boolean isText(){return type==ObjectType.Text;}

    public void SetLayout(float l,float t,float w,float h)
    {
        render_layout.set(l,t,w,h);
    }

    public abstract void update();
    public abstract void draw();
}
