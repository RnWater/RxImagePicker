package com.sample.rx.imagepicker;

import android.app.Application;

import com.rx.img.RxImagePicker;

/**
 * Created by henry on 2019/5/7.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxImagePicker.init(new GlideImageLoader());
    }
}
