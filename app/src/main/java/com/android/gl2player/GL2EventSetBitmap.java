package com.android.gl2player;

import android.graphics.Bitmap;

public class GL2EventSetBitmap extends GL2Event
{
    private Bitmap bmp;
    private int rotate;

    GL2EventSetBitmap(DrawObject obj,Bitmap b,int r)
    {
        super(obj);

        bmp=b;
        rotate=r;
    }

    @Override
    public void run() {
        ((DrawBitmap)draw_object).update(bmp,rotate);
    }
}
