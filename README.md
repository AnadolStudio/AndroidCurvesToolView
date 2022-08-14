[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html) [![](https://jitpack.io/v/AnadolStudio/AndroidCurvesToolView.svg)](https://jitpack.io/#AnadolStudio/AndroidCurvesToolView)
# AndroidCurvesToolView
#### Curves tool view like in Photoshop


## Preview

![Screenshot_1660412189](https://user-images.githubusercontent.com/74777850/184519996-8d94e0fc-b7ec-4304-a5aa-eba4ded2b65e.png) ![Screenshot_1660412002](https://user-images.githubusercontent.com/74777850/184520001-1f966ff0-24f9-4d72-bc60-831b842d2926.png) ![Screenshot_1660442756](https://user-images.githubusercontent.com/74777850/184520008-4c35091e-ae83-4c3e-8bac-31c59f43586d.png)

![ScreenRecorderProject7 (1)](https://user-images.githubusercontent.com/74777850/184520985-42a40fb3-848f-48d3-bb93-c3e3482ceb35.gif)

<br>

## 1. How to start

```gradle
  repositories {
      maven { url 'https://jitpack.io' }
  }
	
  dependencies {
      implementation 'com.github.AnadolStudio:AndroidCurvesToolView:v1.0.4'
  }

```

<br>

## 2. Add to your layout

```xml
    <com.anadolstudio.library.curvestool.view.CurvesView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@drawable/yourbackground"

        app:borderFillColor="@color/yourColor"
        app:borderStrokeColor="@color/yourColor"
        app:borderStrokeWidth="@dimens/yourWidth"
        app:curveStrokeWidth="@dimens/yourWidth"
        app:pointSide="@dimens/yourWidth"
        app:pointStrokeWidth="@dimens/yourWidth" />
```
<br>

## 3. Attributes list

| Attr. name          |                Explain                                            |    Default |
|---------------------|-------------------------------------------------------------------|------------|
| borderFillColor 		| Color in border						                                        | Transparent|
| borderStrokeColor 	| Color border   					                                          | Dark gray  |
| borderStrokeWidth 	| Width of border line	                                            | White      |
| curveStrokeWidth 		| Width of curve line			                                          | 2dp        |
| pointSide 	        | Size of point	                                                    | 10dp       |
| pointStrokeWidth 		| Stroke width in selected point			                              | 2dp	       |

<br>

## 4. Color overriding

```xml
    <color name="pointSelectedWhiteFillColor">#454545</color>
    <color name="pointSelectedRedFillColor">#FFFFFF</color>
    <color name="pointSelectedGreenFillColor">#FFFFFF</color>
    <color name="pointSelectedBlueFillColor">#FFFFFF</color>

    <color name="whiteCurveColor">#FFFFFF</color>
    <color name="redCurveColor">#CC0000</color>
    <color name="greenCurveColor">#669900</color>
    <color name="blueCurveColor">#0099CC</color>

```

<br>

License
--------

    The MIT License (MIT)

    Copyright (c) 2022 Terehov Bogdan
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
