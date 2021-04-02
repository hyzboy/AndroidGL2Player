package com.android.gl2player;

public class GL2EventSetLayout extends GL2Event{

    private float left,top,width,height;

    GL2EventSetLayout(DrawObject obj, float l,float t,float w,float h)
    {
        super(obj);
        left=l;
        top=t;
        width=w;
        height=h;
    }

    @Override
    public void run()
    {
        draw_object.render_layout.set(left,top,width,height);
    }
}
