package com.android.gl2player.drawobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.opengl.GLES20;

import com.android.gl2player.gl.GL2Texture;

public class DrawText extends DrawObject
{
    private Bitmap text_bmp=null;
    private Canvas text_canvas=null;
    private Drawable text_drawable=null;
    private Paint text_paint=null;

    private GL2Texture texture=new GL2Texture();
    private ShaderText shader=new ShaderText();

    public DrawText()
    {
        super(ObjectType.Text);
        texture.init(GLES20.GL_TEXTURE_2D);
    }

    @Override
    public void update(){}

    @Override
    public void draw()
    {

    }
}
