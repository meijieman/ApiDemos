/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.example.android.apis.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.android.apis.R;

public class Vertices extends GraphicsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));
    }

    private static class SampleView extends View {
        private final Paint mPaint = new Paint();
        private final float[] mVerts = new float[10];
        private final float[] mTexs = new float[10];
        private final short[] mIndices = {0, 1, 2, 3, 4, 1};

        private final Matrix mMatrix = new Matrix();
        private final Matrix mInverse = new Matrix();
        private float[] mPt;
        private Paint gridPaint;

        public SampleView(Context context) {
            super(context);
            setFocusable(true);

            Bitmap bm = BitmapFactory.decodeResource(getResources(),
                    R.drawable.beach).copy(Bitmap.Config.ARGB_8888, true);
            addGridLine(bm);
            Shader s = new BitmapShader(bm, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            mPaint.setShader(s);

            float w = bm.getWidth();
            float h = bm.getHeight();
            // construct our mesh
            setXY(mTexs, 0, w / 2, h / 2);
            setXY(mTexs, 1, 0, 0);
            setXY(mTexs, 2, w, 0);
            setXY(mTexs, 3, w, h);
            setXY(mTexs, 4, 0, h);

            setXY(mVerts, 0, w / 2, h / 2);
            setXY(mVerts, 1, 0, 0);
            setXY(mVerts, 2, w, 0);
            setXY(mVerts, 3, w, h);
            setXY(mVerts, 4, 0, h);

            mMatrix.setScale(0.8f, 0.8f);
            mMatrix.preTranslate(20, 20);
            mMatrix.invert(mInverse);
        }

        // 添加网格
        void addGridLine(Bitmap bitmap) {
            gridPaint = new Paint();
            gridPaint.setStrokeWidth(1);
            gridPaint.setColor(Color.RED);
            gridPaint.setAntiAlias(true);

            Canvas canvas = new Canvas(bitmap);
            int width = bitmap.getWidth();
            int count = 20;
            int distance = width / count;
            int height = bitmap.getHeight();
            int distanceY = height / count;
            for (int i = 0; i < count; i++) {
                canvas.drawLine(distance * i, 0, distance * i, height, gridPaint);
            }
            for (int i = 0; i < count; i++) {
                canvas.drawLine(0, distanceY * i, width, distanceY * i, gridPaint);
            }
        }

        private static void setXY(float[] array, int index, float x, float y) {
            array[index * 2 + 0] = x;
            array[index * 2 + 1] = y;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(0xFFCCCCCC);
            canvas.save();
            canvas.concat(mMatrix);

            canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 10, mVerts, 0,
                    mTexs, 0, null, 0, null, 0, 0, mPaint);

            canvas.translate(0, 640);
            canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 10, mVerts, 0,
                    mTexs, 0, null, 0, mIndices, 0, 6, mPaint);

            canvas.restore();

            if (mPt != null) {
                canvas.drawCircle(mPt[0], mPt[1], 20, gridPaint);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            mPt = new float[]{event.getX(), event.getY()};
            mInverse.mapPoints(mPt);
//            float[] tmp = new float[2];
//            tmp[0] = mPt[0] + 200;
//            tmp[1] = mPt[1] + 200;
//            mInverse.mapPoints(tmp);
            setXY(mVerts, 0, mPt[0], mPt[1]);
            invalidate();
            return true;
        }

    }
}

