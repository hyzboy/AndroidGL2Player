package com.android.gl2player;

import android.graphics.Bitmap;
import android.opengl.GLES20;

/**
 * 绘制位图
 */
class DrawBitmap extends DrawObject
{
    private GL2Texture texture=new GL2Texture();
    private ShaderOpaque shader=new ShaderOpaque();

    public DrawBitmap()
    {
        super(ObjectType.Bitmap);

        texture.init(GLES20.GL_TEXTURE_2D);
    }

    public void update(Bitmap bmp, int rotate)
    {
        texture.set(bmp);
    }

    @Override
    public void draw()
    {
        GLES20.glDisable(GLES20.GL_BLEND);
        shader.begin();
            texture.bind(0);
            render_layout.bind(shader.maPositionHandle);
            texture_uv.bind(shader.maTexCoordHandle);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
        shader.end();
    }
}
