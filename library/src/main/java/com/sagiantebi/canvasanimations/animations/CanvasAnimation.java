/*
 * CanvasAnimation.java
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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import com.sagiantebi.canvasanimations.layout.CanvasLayout;

/**
 * an abstract class for defining an animation which can be loaded into a {@link CanvasLayout}
 */
public abstract class CanvasAnimation {

    /**
     * The layout we are currently animating.
     */
    private CanvasLayout mLayout;
    /**
     * Optimization flag, makes sure we only invoke the animation drawing when the animator is running
     */
    private boolean mRunning = false;
    /**
     * Main thread handler for posting the change to {@link #mRunning} flag properly
     */
    private final Handler mMainThreadHandler;

    public CanvasAnimation() {
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Sets the {@link CanvasLayout} which this animation should draw
     * @param layout the layout this instance should animate
     */
    public void setLayout(CanvasLayout layout) {
        mLayout = layout;
    }

    /**
     * Getter for the {@link CanvasLayout} currently attached to this animation. read by implementations.
     * @return the current canvas layout or null if none is attached.
     */
    protected CanvasLayout getLayout() {
        return mLayout;
    }

    /**
     * Draws the animation using the supplied {@link Canvas} only when the animation is running
     * @param canvas The canvas to draw
     * @param step The current animation step
     */
    public void draw(Canvas canvas, float step) {
        if (isRunning()) {
            dispatchDraw(canvas, step);
        } else {
            mLayout.commitDraw(canvas);
        }
    }

    /**
     * Overridden by animations to perform the animation steps
     * @param canvas The canvas of the layout
     * @param step The current animation step
     */
    protected abstract void dispatchDraw(Canvas canvas, float step);

    /**
     * Returns if the animation is currently running
     * @return true if the animation is currently running, false otherwise
     */
    public boolean isRunning() {
        return mRunning;
    }

    /**
     * Setter for the running state. useful for situations where the animation is used outside of an animation context<br/>
     * A good example would be using the animation in a scroll of a recycled list.
     * @param running the desired state, true to make this animation draw, false otherwise.
     * @see #mRunning
     */
    public void setRunning(boolean running) {
        mRunning = running;
    }

    /**
     * An animator listener adapter which listens on the attached animator
     */
    private Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            setRunning(false);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //post a runnable for ending the animation, giving any other listeners an opportunity to do something important.
            mMainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    setRunning(false);
                }
            });
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            setRunning(true);
        }
    };

    /**
     * Creates a new {@link Animator} instance, attaching this canvas animation to it. <br/>
     * Please note - {@link #setLayout(CanvasLayout)} must be invoked beforehand. This is done to avoid allocations of this animation instance and to allow re-use.
     * @param start the value to start form i.e. 0f (when animating something in) or 1f (when animating something out)
     * @param end the value in which the animation should end.
     * @return an Animator instance bound to this animation.
     */
    public Animator newAnimatorInstance(float start, float end) {
        mLayout.requestInvalidation(this, start);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mLayout, CanvasLayout.ANIMATION_STEP, start, end);
        objectAnimator.addListener(mAnimatorListener);
        return objectAnimator;
    }

}
