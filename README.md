# RegularPolygonView
尖尖朝上的正多边形ImageView


# 截图预览（Screen Recrod）
<img  width = "450" src = "https://github.com/bigdongdong/RegularPolygonView/blob/master/preview/preview.jpg"></img></br>

# 项目配置

```
  allprojects {
      repositories {
          ...
          maven { url 'https://jitpack.io' }  //添加jitpack仓库
      }
  }
  
  dependencies {
	  implementation 'com.github.bigdongdong:RegularPolygonView:1.0' //添加依赖
  }
```

# 使用方法
```xml
<com.cxd.regularpolygonview.RegularPolygonView
            xmlns:rpv="http://schemas.android.com/apk/res-auto"
            rpv:sides="11"
            rpv:border_width="3dp"
            rpv:border_color="#FF2D2D"
            rpv:round_radius="8dp"
            android:src="@mipmap/ic_launcher"
            android:layout_width="110dp"
            android:layout_height="110dp"
            />
```

# 属性说明
|属性名称|解释|
|:---|:---|
|rpv:sides|边数|
|rpv:border_width|边框宽度|
|rpv:border_color|边框颜色|
|rpv:round_radius|圆角半径|
|rpv:arrow_width|箭头的宽度|

# 特殊说明
该ImageView 内部强制使用ScaleType.CENTER_CROP作为适配方案
