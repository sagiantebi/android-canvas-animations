/*
 * ShadesAnimation.java
 *
 * Copyright 2019 Sagi Antebi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sagiantebi.canvasanimations.animations;

import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * An animation simulating shades closing or opening
 */
public class ShadesAnimation extends CanvasAnimation {

    /**
     * The default number of shades
     */
    private final static int NUMBER_OF_SHADES = 8;
    /**
     * The number of shades used by this instance
     */
    private final int mNumberOfShades;

    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();

    /**
     * Creates this animation with the default amount of shades
     */
    public ShadesAnimation() {
        this(NUMBER_OF_SHADES);
    }

    /**
     * Creates this animation with the supplied amount of shades
     * @param numberOfShades the number of shades to use
     */
    public ShadesAnimation(int numberOfShades) {
        super();
        this.mNumberOfShades = numberOfShades;
    }

    /**
     * {@inheritDoc}
     * @param canvas The canvas of the layout
     * @param step The current animation step
     */
    @Override
    protected void dispatchDraw(Canvas canvas, float step) {
        float w = getLayout().getContentView().getWidth();
        float h = getLayout().getContentView().getHeight();
        //use a small increment in every shade to create a smooth top to bottom effect
        float increment = 0.01f;
        for (int i = 0; i < mNumberOfShades; i++) {
            int count = canvas.save();
            mCamera.save();
            mCamera.setLocation(0, 0, -18 * 2);
            float innerStep = Math.min(step+increment, 1f);
            mCamera.rotateX(-89f * (1f-innerStep));
            mCamera.rotateY(0f);
            mCamera.rotateZ(-0f);
            mCamera.getMatrix(mMatrix);
            float dy = -(i + 1) * (h / mNumberOfShades);
            mMatrix.preTranslate(-w / 2f, dy);
            float bottom = (i + 1) * (h / mNumberOfShades);
            mMatrix.postTranslate(w / 2f, bottom);
            canvas.concat(mMatrix);
            float top = i * (h / mNumberOfShades);
            canvas.clipRect(0, top, w, bottom);
            getLayout().commitDraw(canvas);
            canvas.restoreToCount(count);
            mCamera.restore();
            increment*=0.18f;
        }
    }
}
