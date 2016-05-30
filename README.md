# Android头像上传之图片裁剪，实现仿QQ、微信两种效果
###  
### 功能描述：
  头像上传在APP中是很常见的功能，但是关于头像上传前的图片裁剪方式，如果使用系统提供的裁剪方式达不到预期效果，所以在这里提供一个类似qq、微信头像裁剪的示例程序。示例程序主要提供调用系统相册、调用系统相机后进行图片裁剪，可以设置类似qq的圆形裁剪框，类似微信的矩形裁剪框。裁剪控件提供移动、缩放、边界检测、大图压缩处理功能。

### 效果图：
![示例1](https://github.com/wsy858/AndroidHeadImageCliper/blob/master/pic/demo1.gif)  


![示例1](https://github.com/wsy858/AndroidHeadImageCliper/blob/master/pic/demo2.gif)

### 代码说明：
   ClipView为一个继承自View的自定义控件，通过Canvas画出裁剪框，中间透过添加Layer层，镂空裁剪框，裁剪框为透明区域，裁剪框四周为半透明效果。ClipViewLayout为一个继承自RelativeLayout的自定义容器，主要组合裁剪框组件和原图ImageView，并提供了自定义属性，在xml文件中只需要添加ClipViewLayout即可。

- **clipBorderWidth**：裁剪框边框宽度
- **mHorizontalPadding**： 裁剪框水平方向的间距
- **clipType**： 裁剪框类型，取值(circle, rectangle)

### 调用示例：
    <!--圆形裁剪框 -->
    <evan.wang.view.ClipViewLayout
       android:id="@+id/clipViewLayout1"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:clipBorderWidth="2dp"
       app:clipType="circle"
       app:mHorizontalPadding="30dp" />
    
    <!--矩形裁剪框 -->
    <evan.wang.view.ClipViewLayout
       android:id="@+id/clipViewLayout2"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       app:clipBorderWidth="2dp"
       app:clipType="rectangle"
       app:mHorizontalPadding="30dp"
      />
    
    //ClipViewLayout设置原图，由于Activity直接传递BitMap在大图时候会报错，所以此处传递图片的URI地址，具体使用请参考demo 
    public void setImageSrc(final Uri uri)；

    //ClipViewLayout获取剪切图，对外提供裁剪的方法
    public Bitmap clip()；


