package com.android.gl2player.gl;

import android.opengl.GLES20;
import android.util.Log;

public abstract class ShaderModule extends GLClass
{
    private final String TAG = "ShaderModule";

    private final String mVertexShader =
                      "attribute vec4 aPosition;\n"
                    + "attribute vec4 aTextureCoord;\n"
                    + "varying vec2 vTextureCoord;\n"
                    + "void main() {\n"
                    + "  gl_Position = aPosition;\n"
                    + "  vTextureCoord = aTextureCoord.xy;\n"
                    + "}\n";

    protected int mProgram = -1;

    public int maPositionHandle;
    public int maTexCoordHandle;

    public ShaderModule(String tag)
    {
        super(tag);
    }

    private int loadShader(int shaderType, String source) {
        GLES20.glGetError();
        
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0)
        {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);

            int[] compiled = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

            if (compiled[0] == 0)
            {
                Log.e(TAG, "Could not compile shader " + shaderType + ":");
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        else
            CheckGLError("CreateShader");

        return shader;
    }

    private int createProgram(String vertexSource, String fragmentSource) {
        int vertex_shader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertex_shader == 0) {
            return 0;
        }
        int fragment_shader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragment_shader == 0) {
            return 0;
        }

        int program = GLES20.glCreateProgram();
        if (program != 0)
        {
            GLES20.glAttachShader(program, vertex_shader);
            CheckGLError("glAttachShader vertex");
            GLES20.glAttachShader(program, fragment_shader);
            CheckGLError("glAttachShader fragment");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "Could not link program: ");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public boolean init(String mFragmentShader) {
        mProgram = createProgram(mVertexShader, mFragmentShader);
        if (mProgram == 0) return (false);

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        CheckGLError("glGetAttribLocation aPosition");
        if (maPositionHandle == -1) {
            return (false);
        }

        maTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        CheckGLError("glGetAttribLocation aTextureCoord");
        if (maTexCoordHandle == -1) {
            return (false);
        }

        return (true);
    }

    public abstract boolean init();

    public void begin()
    {
        ClearGLError();
        GLES20.glUseProgram(mProgram);
        CheckGLError("after glUseProgram()");
    }

    public void end() {
        GLES20.glUseProgram(0);
    }
}
