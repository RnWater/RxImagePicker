package com.sample.rx.imagepicker;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rx.img.display.RxImagePickerLoader;

/**
 * Created by henry on 2019/5/7.
 */

public class GlideImageLoader implements RxImagePickerLoader {
    @Override
    public void displayImage(ImageView iv, String filePath, int width, int height) {
        Glide.with(iv.getContext()).load(filePath).centerCrop().override(width,height).into(iv);
    }
}
