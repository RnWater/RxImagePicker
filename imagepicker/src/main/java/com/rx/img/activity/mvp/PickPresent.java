package com.rx.img.activity.mvp;
import android.content.Context;
import com.rx.img.base.BasePresenter;

/**
 * Created by henry on 2019/5/6.
 */

public abstract class PickPresent extends BasePresenter{
    public abstract void loadAllImage(Context context);
}
