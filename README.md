# CircularProgressBar-android

## Latest version [![](https://jitpack.io/v/JeetR/CircularProgressBar-android.svg)](https://jitpack.io/#JeetR/CircularProgressBar-android)

### How To install

##### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
##### Step 2. Add the dependency

```
	dependencies {
	        implementation 'com.github.JeetR:CircularProgressBar-android:<latest_version>'
	}
```

### How to use
 
 **From XML**
 
 Provided below is a dummy use case showing usage of all the parameters
 
 ``` 
 <com.jeet.circularprogressbar.CircularProgressBar
        android:id="@+id/customRoundProgressBar"
        android:layout_width="100dp"
        android:layout_height="100dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:maxProgressLimit="60"
        app:progress="50"
        app:maxAnimationDuration="2500"
        app:animationInterpolator="AccelerateDecelerateInterpolator"
        app:showProgressText="true"
        app:progressTextSize="16sp"
        app:progressTextColor="@color/black"
        app:progressBarColor="@android:color/holo_red_dark" />
 
 ```
 ```
 maxProgressLimit is 100 by default you can also set minProgressLimit
 ```
 
 #### Result
 
 ![Result Image](https://github.com/JeetR/CircularProgressBar-android/blob/master/resultImage.png "Circular Progress bar")
 
 
 
 #### you can access this view using findViewById(R.id.customRoundProgressBar) programatically and setup these parameters from the code
 
 #### Project executed with Determinant Studios (hey[at]determinantstudios.com)
 
