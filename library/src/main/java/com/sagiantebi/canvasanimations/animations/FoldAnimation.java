/*
 * FoldAnimation.java
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
 * A simple fold animation, segmented into vertical parts
 */
public class FoldAnimation extends CanvasAnimation {

    /**
     * The default number of folds to use
     */
    private final static int NUMBER_OF_FOLDS = 4;
    /**
     * The number of folds for this instance
     */
    private final int mNumberOfFolds;

    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();

    /**
     * creates this animation with the default amount of folds
     */
    public FoldAnimation() {
        this(NUMBER_OF_FOLDS);
    }

    /**
     * creates this animation with the supplied amount of folds
     * @param numberOfFolds the number of folds to use with this animation
     */
    public FoldAnimation(int numberOfFolds) {
        super();
        this.mNumberOfFolds = numberOfFolds;
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
        for (int i = 0; i < mNumberOfFolds; i++) {
            int count = canvas.save();
            mCamera.save();
            mCamera.setLocation(0, 0, -18);
            float stepInFold = Math.max(0f, Math.min(1f, (step * 4) - i));
            if (stepInFold > 0f) {
                float top = i * (h / mNumberOfFolds);
                float bottom = (i + 1) * (h / mNumberOfFolds);
                mCamera.rotateX(-89f * (1f-stepInFold));
                mCamera.rotateY(0f);
                mCamera.rotateZ(-0f);
                mCamera.getMatrix(mMatrix);
                mMatrix.preTranslate(-w / 2f, -bottom);
                mMatrix.postTranslate(w / 2f, bottom);
                canvas.concat(mMatrix);
                canvas.clipRect(0, top, w, bottom);
                getLayout().commitDraw(canvas);
                canvas.restoreToCount(count);
                mCamera.restore();
            }

        }
    }
}
