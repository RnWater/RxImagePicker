package com.rx.img.activity.mvp;

import com.rx.img.bean.ImageFolder;

import java.util.List;

/**
 * Created by henry on 2019/5/6.
 */

public interface PickView {
    void showAllImage(List<ImageFolder> folders);
}
