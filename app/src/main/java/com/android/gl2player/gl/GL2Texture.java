package com.android.gl2player.gl;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class GL2Texture {
    protected int texture_id=-1;
    private int texture_type=GLES20.GL_TEXTURE_2D;
    private int width=0,height=0;

    private void ClearGLError()
    {
        GLES20.glGetError();
    }

    private void CheckGLError(String funcname)
    {
        int no=GLES20.glGetError();

        if(no!=GLES20.GL_NO_ERROR)
            Log.e("GL2Texture","func["+funcname+"] error: "+String.valueOf(no));
    }

    private void CreateTexture()
    {
        int[] textures=new int[1];

        ClearGLError();
        GLES20.glGenTextures(1,textures,0);
        CheckGLError("glGenTextures");

        texture_id =textures[0];

        bind();

        GLES20.glTexParameterf(texture_type, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(texture_type, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
    }

    public void init(int tt)
    {
        texture_type=tt;
        CreateTexture();
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
        int[] old_textures = new int[1];
        old_textures[0] = texture_id;

        CreateTexture();
        GLUtils.texImage2D(texture_type,0,bmp,0);
        CheckGLError("texImage2D");
        width=bmp.getWidth();
        height=bmp.getHeight();

        if(old_textures[0]!=-1)
            GLES20.glDeleteTextures(1, old_textures, 0);
    }
}
