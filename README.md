## android-canvas-animations

A library for creating canvas centric animations (revolving mainly on clipping) directly on Views or ViewGroups.

### Quick demo

![demo](demo.gif?raw=true "Animations in action")

### Maven

jcenter -
```groovy
implementation 'com.sagiantebi:android-canvas-animations:1.0'
```

### Usage
 
The target elements must belong to a View/ViewGroup which implement `CanvasLayout`.

For most situations the library supplies a `FrameLayout` which already implements `CanvasLayout`.
Using this layout you can simply wrap your existing views.

Here is a short code snippet which animates the above layout -
```java
CanvasAnimationFrameLayout cafl = findViewById(R.id.layout);
CanvasAnimation ca = new RevealAnimation();
cafl.loadAnimation(ca);
ca.newAnimatorInstance(0f, 1f).setDuration(1800).start();
```

### Available animations
* FoldAnimation
* RevealAnimation
* RevealFromBottomAnimation
* RevealFromCenterAnimation
* RevealFromTopAnimation
* ShadesAnimation

New types of animations can be easily created by subclassing `CanvasAnimation` and implementing it's `dispatchDraw` abstract method.

### license

```text
Copyright 2019 Sagi Antebi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```