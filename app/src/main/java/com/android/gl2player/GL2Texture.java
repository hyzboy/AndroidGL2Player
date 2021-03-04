package com.android.gl2player;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class GL2Texture {
    protected int texture_id=-1;
    private int texture_type=GLES20.GL_TEXTURE_2D;
    private int width=0,height=0;

    public void init(int tt)
    {
        int[] textures=new int[1];

        GLES20.glGenTextures(1,textures,0);
        texture_id =textures[0];
        texture_type=tt;

        GLES20.glBindTexture(texture_type, texture_id);

        GLES20.glTexParameterf(texture_type, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(texture_type, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    }

    public void bind()
    {
        GLES20.glEnable(texture_type);
        GLES20.glBindTexture(texture_type, texture_id);
    }

    public void bind(int index)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+index);
        bind();
    }

    public int getID() {return texture_id;}
    public int getType(){return texture_type;}

    public void set(Bitmap bmp)
    {
        bind();

        GLES20.glGetError();

        if(width==0||height==0)
        {
            GLUtils.texImage2D(texture_type,0,bmp,0);
            width=bmp.getWidth();
            height=bmp.getHeight();
        }
        else if(width==bmp.getWidth()
              &&height==bmp.getHeight())
        {
            GLUtils.texSubImage2D(texture_type,0,0,0,bmp);
        }
        else
        {
            ///
        }

        int no=GLES20.glGetError();
        Log.e("GL2Texture","set result="+String.valueOf(no));
    }
}
