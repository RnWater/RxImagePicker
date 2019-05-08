package com.rx.img.manager;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by henry on 2019/5/5.
 */

public class RxPickerConfig {
    private int minValue = 1;//最少选择一张图片
    private int maxValue = 9;//最多选择9张图片
    private boolean showCamera = true;

    public static final int SINGLE_IMG = 0x001;
    public static final int MULTIPLE_IMG = 0x002;
    private int mode = SINGLE_IMG;

    @IntDef({ SINGLE_IMG, MULTIPLE_IMG })
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {
    }

    public int getMode() {
        return mode;
    }

    public void setMode(@Mode int mode) {
        this.mode = mode;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setLimit(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isSingle() {
        return mode == SINGLE_IMG;
    }
}
