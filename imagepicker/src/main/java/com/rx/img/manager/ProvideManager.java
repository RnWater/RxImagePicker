package com.rx.img.manager;

import android.content.Context;

/**
 * Created by henry on 2019/5/5.
 */
public class ProvideManager {
    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".provider";
    }
}
