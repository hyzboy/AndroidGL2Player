package com.android.gl2player.async;

import com.android.gl2player.drawobject.DrawObject;

public abstract class GL2Event {

    protected DrawObject draw_object;

    public GL2Event(DrawObject obj)
    {
        draw_object=obj;
    }

    public abstract void run();
};

