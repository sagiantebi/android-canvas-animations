/*
 * SampleActivity.java
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

package com.sagiantebi.canvasanimations.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.sagiantebi.canvasanimations.animations.*;
import com.sagiantebi.canvasanimations.R;
import com.sagiantebi.canvasanimations.fragments.ImageFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A sample activity for the android canvas animations library. uses Fragments to show case the animations.
 */
public class SampleActivity extends AppCompatActivity implements ImageFragment.OnImageFragmentPopListener {

    /**
     * a list of image resources we can use in the fragments
     */
    private final List<Integer> mImageResources = new ArrayList<>();
    /**
     * a list of all the animations we want to showcase
     */
    private final List<Class<? extends CanvasAnimation>> mAnimations = Arrays.asList(FoldAnimation.class,
            RevealAnimation.class, RevealFromBottomAnimation.class, RevealFromCenterAnimation.class, RevealFromTopAnimation.class, ShadesAnimation.class);
    /**
     * a simple counter - use a different animation each time it advances.
     */
    private int mCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        char s = 'a';
        Resources res = getResources();
        while (s <= 'h') {
            int resId = res.getIdentifier("image_" + s, "drawable", getPackageName());
            mImageResources.add(resId);
            s++;
        }

        setContentView(R.layout.activity_sample);
        View button = findViewById(R.id.button_add_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment();
            }
        });
    }

    /**
     * adds a fragment with the current image and animation in the stack
     */
    private void addFragment() {
        final ImageFragment imageFragment = ImageFragment.newInstance(mImageResources.get(mCounter%mImageResources.size()), mAnimations.get(mCounter%mAnimations.size()));
        getSupportFragmentManager().beginTransaction().add(R.id.container, imageFragment).commitAllowingStateLoss();
        mCounter++;
    }

    /**
     * Callback from the image fragment, triggered when the animation has ended
     * @param fragment the fragment which wants out
     */
    @Override
    public void onImageFragmentPop(ImageFragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
    }
}
