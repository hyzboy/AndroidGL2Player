package com.android.gl2player;

public class QuadUV extends GL2FloatBuffer{

    private static final float QuadUVData[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f};

    QuadUV()
    {
        init(QuadUVData);
    }
}
