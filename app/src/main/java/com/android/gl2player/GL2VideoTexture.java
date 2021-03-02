package com.android.gl2player;

import android.graphics.SurfaceTexture;
import android.view.Surface;

public class GL2VideoTexture extends GL2Texture implements VideoPlayer.MediaPlayerStateListener{

    private static int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    private VideoPlayer player;
    public SurfaceTexture surfaceTexture;

    public void init(SurfaceTexture.OnFrameAvailableListener listener)
    {
        init(GL_TEXTURE_EXTERNAL_OES);

        surfaceTexture = new SurfaceTexture(texture_id);
        surfaceTexture.setOnFrameAvailableListener(listener);

        Surface surface = new Surface(surfaceTexture);
        player.configure(surface);
        player.prepare();
        surface.release();
    }

    @Override
    public void onPlayStart(String path) {
        if (player != null) {
            player.prepare();
            player.startPlay();
        }
    }

    @Override
    public void onPlayCompleted() {
        if (player != null) {
            player.startPlay();
        }
    }

    @Override
    public void onPlayStop() {

    }
}
