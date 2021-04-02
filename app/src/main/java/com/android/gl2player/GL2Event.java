package com.android.gl2player;

import android.graphics.Bitmap;

public class GL2Event {

    protected DrawObject draw_object;

    public GL2Event(DrawObject obj)
    {
        draw_object=obj;
    }

    public void run(){}
};

