/*
 * ImageFragment.java
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

package com.sagiantebi.canvasanimations.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.sagiantebi.canvasanimations.animations.CanvasAnimation;
import com.sagiantebi.canvasanimations.animations.RevealAnimation;
import com.sagiantebi.canvasanimations.layout.CanvasAnimationFrameLayout;
import com.sagiantebi.canvasanimations.R;

/**
 * a crude & simple fragment which starts a canvas animation in it's onStart() method
 */
public class ImageFragment extends Fragment {

    /**
     * constant for the image resource bundle key
     */
    private static final String ARGUMENT_IMAGE_RES = "Fragment.ImageRes";

    /**
     * constant for the animation bundle key
     */
    private static final String ARGUMENT_ANIMATION_CLASS = "Fragment.AnimationClass";

    private CanvasAnimation mCanvasAnimation;

    /**
     * Creates a new instance of this fragment using the supplied image resource
     * @param imageResourceId an image resource
     * @return an instance of ImageFragment ready to be added with the given arguments
     */
    public static ImageFragment newInstance(@DrawableRes int imageResourceId, Class<? extends CanvasAnimation> animationClass) {
        Bundle args = new Bundle();
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        args.putInt(ARGUMENT_IMAGE_RES, imageResourceId);
        args.putSerializable(ARGUMENT_ANIMATION_CLASS, animationClass);
        return fragment;
    }


    /**
     * An example of how to use a {@link CanvasAnimation} for animating something out
     * @param listener a listener for the animation, used above for removing the fragment via activity callback
     */
    private void animateOut(@NonNull Animator.AnimatorListener listener) {
        View view = getView();
        if (view instanceof CanvasAnimationFrameLayout) {
            CanvasAnimationFrameLayout cafl = (CanvasAnimationFrameLayout) view;
            cafl.loadAnimation(mCanvasAnimation);
            Animator animator = mCanvasAnimation.newAnimatorInstance(1f, 0f).setDuration(1800);
            animator.addListener(listener);
            animator.start();
        }
    }

    /**
     * An example of how to use {@link CanvasAnimation} for animating something in
     */
    private void animateIn() {
        View view = getView();
        if (view instanceof CanvasAnimationFrameLayout) {
            CanvasAnimationFrameLayout cafl = (CanvasAnimationFrameLayout) view;
            cafl.loadAnimation(mCanvasAnimation);
            mCanvasAnimation.newAnimatorInstance(0f, 1f).setDuration(1800).start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment, container, false);
        Bundle b = savedInstanceState == null ? getArguments() : savedInstanceState;
        if (b != null) {
            int imageRes = b.getInt(ARGUMENT_IMAGE_RES, -1);
            if (imageRes != -1) {
                ImageView imageView = v.findViewById(R.id.first_image);
                imageView.setImageResource(imageRes);
            }
            Class clz = (Class) b.getSerializable(ARGUMENT_ANIMATION_CLASS);
            if (clz != null) {
                try {
                    mCanvasAnimation = (CanvasAnimation) clz.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

        if (mCanvasAnimation == null) {
            mCanvasAnimation = new RevealAnimation();
        }

        TextView tv = v.findViewById(R.id.textView);
        tv.setText(mCanvasAnimation.getClass().getSimpleName());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateOut(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Activity activity = getActivity();
                        if (activity instanceof OnImageFragmentPopListener) {
                            ((OnImageFragmentPopListener) activity).onImageFragmentPop(ImageFragment.this);
                        }
                    }
                });
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        animateIn();
    }

    /**
     * Interface which should be implemented by the hosting activity.
     */
    public interface OnImageFragmentPopListener {
        /**
         * Notifies when the fragment has finished it's work and asks to be removed.
         * @param fragment the fragment which wants to be removed.
         */
        void onImageFragmentPop(ImageFragment fragment);
    }

}
