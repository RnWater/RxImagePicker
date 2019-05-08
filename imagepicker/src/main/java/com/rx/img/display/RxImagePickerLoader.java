package com.rx.img.display;

import android.widget.ImageView;

/**
 * Created by henry on 2019/5/5.
 * 提供接口供开发者自己选择显示图片的框架
 * 实现此接口，在application中进行初始化操作。
 */

public interface RxImagePickerLoader {
    void displayImage(ImageView iv, String filePath, int width, int height);
}
