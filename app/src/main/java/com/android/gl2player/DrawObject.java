package com.android.gl2player;

/**
 * 绘制对象基类
 */
class DrawObject
{
    protected static final QuadUV texture_uv =new QuadUV();

    public RenderLayout render_layout=new RenderLayout();

    public DrawObject()
    {
        render_layout.init();
    }

    void draw(){};
}
