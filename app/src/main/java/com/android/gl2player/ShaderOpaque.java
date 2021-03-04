package com.android.gl2player;

public class ShaderOpaque extends ShaderModule{

    private final String mFragmentShaderOpaque =
            "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "uniform sampler2D sTexture;\n"
                    + "void main() {\n"
                    + "  gl_FragColor = vec4(texture2D(sTexture, vTextureCoord).rgb,1.0);\n"
                    + "}\n";

    @Override
    public boolean init() {
        if (!super.init(mFragmentShaderOpaque)) {
            return (false);
        }

        return (true);
    }

    public ShaderOpaque()
    {
        init();
    }
}
