# Android头像上传之图片裁剪，实现仿QQ、微信两种效果
###  
### 功能描述：
  头像上传功能在很多APP中都有，但是关于头像上传前的图片裁剪方式，使用系统的裁剪方式达不到预期效果，所以在这里提供一个类似qq、微信头像裁剪的示例程序。示例程序主要提供调用系统相册、调用系统相机后进行图片裁剪，可以设置类似qq的圆形裁剪框，类似微信的矩形裁剪框。裁剪控件提供移动、缩放，边界检测功能。

### 效果图：
![示例1](https://github.com/wsy858/AndroidHeadImageCliper/blob/master/pic/demo1.gif)

### 代码说明：
   图片裁剪页面为自定义控件，ClipView为一个继承自View的自定义控件，通过Canvas画出裁剪框，中间透过添加Layer层，镂空裁剪框，裁剪框为透明区域，裁剪框四周为半透明效果。ClipViewLayout为一个继承自RelativeLayout的自定义容器，主要组合裁剪框组件和原图ImageView，并提供了自定义属性，在xml文件中只需要添加ClipViewLayout即可。

- clipBorderWidth：裁剪框边框宽度
- mHorizontalPadding： 裁剪框水平方向的间距
- clipType:裁剪框类型： 取值(circle, rectangle)

### 调用示例：
    <evan.wang.view.ClipViewLayout
    android:id="@+id/clipViewLayout1"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_weight="1"
    app:clipBorderWidth="2dp"
    app:clipType="circle"
    app:mHorizontalPadding="30dp" />
    
    <evan.wang.view.ClipViewLayout
    android:id="@+id/clipViewLayout2"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_weight="1"
    app:clipBorderWidth="2dp"
    app:clipType="rectangle"
    app:mHorizontalPadding="30dp"
    android:visibility="gone"
    />


