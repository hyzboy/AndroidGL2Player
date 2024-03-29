package com.android.gl2player.async;

import com.android.gl2player.drawobject.DrawObject;

public class GL2EventSetLayout extends GL2Event{

    private float left,top,width,height;

    public GL2EventSetLayout(DrawObject obj, float l, float t, float w, float h)
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
        draw_object.SetLayout(left,top,width,height);
    }
}
