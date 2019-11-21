/*
 * CanvasLayout.java
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

import android.graphics.Canvas;
import android.view.View;
import com.sagiantebi.canvasanimations.animations.CanvasAnimation;

/**
 * A interface describing a layout which can be canvas animated using {@link CanvasAnimation}
 */
public interface CanvasLayout {

    /**
     * Constant describing the name of the property being used by {@link android.animation.ObjectAnimator}<br/>
     * Exposed to the framework using the {@link CanvasLayout#setAnimationStep(float)} and {@link CanvasLayout#getAnimationStep()}
     */
    public final static String ANIMATION_STEP = "AnimationStep";

    /**
     * Tells the layout to draw itself normally. best practice would be to call the layout's {@link View#dispatchDraw(Canvas)}
     * @param canvas the canvas being drawn. should not be null.
     */
    public void commitDraw(Canvas canvas);

    /**
     * Tells the layout to prepare for starting the animation
     * @param animation the animation being loaded. should not be null.
     * @param step the initial step for the animation.
     */
    public void requestInvalidation(CanvasAnimation animation, float step);

    /**
     * Prepares the layout for performing the supplied animation.
     * @param animation the animation instance desired for this layout. may be null
     */
    public void loadAnimation(CanvasAnimation animation);

    /**
     *  Helper method for returning the current loaded animation
     * @return the current {@link CanvasAnimation} instance loaded to this layout, or null if none
     */
    public CanvasAnimation getCurrentAnimation();

    /**
     * Helper method for getting the view itself.
     * @return the {@link View} representing this layout
     */
    public View getContentView();

    /**
     * Setter for the current animation step. usually invoked by the OS when using {@link android.animation.ObjectAnimator}<br/>
     * This can also be useful for performing animations in scroll transition such as revealing a view in a {@link android.widget.ListView} or new methodologies.
     * @param animationStep the current animation step applied to this layout
     */
    public void setAnimationStep(float animationStep);

    /**
     * Getter for the current animation step. usually invoked by the OS when using {@link android.animation.ObjectAnimator}
     * @return the current animation step which was set by {@link #setAnimationStep(float)}
     */
    public float getAnimationStep();

}
