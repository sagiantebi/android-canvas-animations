/*
 * CanvasAnimationFrameLayout.java
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

package com.sagiantebi.canvasanimations.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.sagiantebi.canvasanimations.animations.CanvasAnimation;

/**
 * A {@link FrameLayout} implementing {@link CanvasLayout}<br/>
 * This implementation can be used as a baseline for implementing {@link CanvasLayout} in other {@link View} types
 */
public class CanvasAnimationFrameLayout extends FrameLayout implements CanvasLayout {

    /**
     * The current animation
     */
    private CanvasAnimation mCanvasAnimation = null;

    /**
     * The current animation step
     */
    private float mAnimationStep = 1f;

    /**
     * {@inheritDoc}
     */
    public CanvasAnimationFrameLayout(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     */
    public CanvasAnimationFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     */
    public CanvasAnimationFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * {@inheritDoc}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CanvasAnimationFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mCanvasAnimation != null) {
            mCanvasAnimation.draw(canvas, mAnimationStep);
        } else {
            commitDraw(canvas);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commitDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void requestInvalidation(CanvasAnimation animation, float step) {
        mAnimationStep = step;
        if (mCanvasAnimation != animation) {
            mCanvasAnimation = animation;
        }
        invalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadAnimation(CanvasAnimation animation) {
        mCanvasAnimation = animation;
        animation.setLayout(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CanvasAnimation getCurrentAnimation() {
        return mCanvasAnimation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getContentView() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getAnimationStep() {
        return mAnimationStep;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAnimationStep(float animationStep) {
        mAnimationStep = animationStep;
        invalidate();
    }
}
