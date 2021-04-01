/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gl2player;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class GL2PlayerActivity extends Activity {

    GL2PlayerView mView;
    private Bitmap mBitmap=null;
    float w=0.4f;
    float h=0.4f;
    @Override protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        mView=findViewById(R.id.GL2PlayerView);

        findViewById(R.id.test_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        if(mBitmap!=null && !mBitmap.isRecycled()){
                            mBitmap.recycle();
                        }
                        mView.getGL2Renderer().setLayout(0,0.5f,0.5f,w,h);
                        mBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.rxkt_demo1);
                        mView.getGL2Renderer().setBitmap(0,mBitmap,0);
                        w+=0.05;
                        h+=0.05;
                    }
                }.start();

            }
        });
    }

    @Override protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        mView.onResume();
    }
}
