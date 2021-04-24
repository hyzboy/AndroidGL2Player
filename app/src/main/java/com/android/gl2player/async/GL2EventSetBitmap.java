package com.android.gl2player.async;

import android.graphics.Bitmap;

import com.android.gl2player.drawobject.DrawBitmap;
import com.android.gl2player.drawobject.DrawObject;

public class GL2EventSetBitmap extends GL2Event
{
    private Bitmap bmp;
    private int rotate;

    public GL2EventSetBitmap(DrawObject obj, Bitmap b, int r)
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
