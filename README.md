# fadingIndicator
A simple indicator with fading animation for ViewPager in Android.
<br>
Most of the existing indicators don’t provide a smooth transition, this library fades indicators in and out when pager flipped.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-fadingIndicator-green.svg?style=true)](https://android-arsenal.com/details/1/3505)

<H2>Images</H2>
<img width="270px" height="480" src="/images/1.gif" />
<img width="270px" height="480" src="/images/2.png" />
<br>

<H2>Usage</H2>
Gradle Import:

Add the specific repository to your build file:
```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```
Add the dependency in your build file (do not forget to specify the correct qualifier, usually 'aar'):
```groovy
dependencies {
    compile 'com.github.ugurtekbas:fadingIndicator:623a5c62ff'
}
```

```xml

<com.ugurtekbas.fadingindicatorlibrary.FadingIndicator
        android:id="@+id/circleIndicator"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="10dp"
        />
        
<!-- or with some attributes in xml -->        
<com.ugurtekbas.fadingindicatorlibrary.FadingIndicator
        android:id="@+id/circleIndicator"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true"
        android:padding="10dp"
        app:radius="10dp"
        app:fillColor="#56B6BF"
        app:strokeColor="#FC823B"
        app:shape="rectangle"
        />
```

```java
FadingIndicator indicator = (FadingIndicator) findViewById(R.id.indicator);
ViewPager viewpagerDefault = (ViewPager) findViewById(R.id.viewpager);
//assigning indicator to viewpager
indicator.setViewPager(viewpagerDefault);

//Set fill color
indicator.setFillColor(Color.RED);
//Set stroke color
indicator.setStrokeColor(Color.CYAN);
//Set radius of indicator
indicator.setRadius(40f);
//Set shape of indicator
indicator.setShape("rectangle");
//Set page changed listener
indicator.setPageListener(this);

//Invokes when user slides between pages
public void onPageFlipped(int pageIndex) {

}

```
<H2>License</H2>
    Copyright 2017 Ugur Tekbas

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
