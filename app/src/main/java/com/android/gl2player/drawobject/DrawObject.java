package com.android.gl2player.drawobject;

import com.android.gl2player.gl.QuadUV;
import com.android.gl2player.gl.RenderLayout;

/**
 * 绘制对象基类
 */
public class DrawObject
{
    public enum ObjectType
    {
        Bitmap,
        Video,
    };

    private ObjectType type;

    protected static final QuadUV texture_uv =new QuadUV();

    public RenderLayout render_layout=new RenderLayout();

    public DrawObject(ObjectType ot)
    {
        type=ot;
        render_layout.init();
    }

    public final ObjectType GetObjectType()
    {
        return type;
    }

    public void update(){};
    public void draw(){};
}
