package com.rx.img;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.util.Log;

import com.rx.img.activity.RxImagePickerActivity;
import com.rx.img.activity.RxTranslucentActivity;
import com.rx.img.activity.fragment.HandlerResultFragment;
import com.rx.img.bean.Image;
import com.rx.img.display.RxImagePickerLoader;
import com.rx.img.manager.CameraHelper;
import com.rx.img.manager.RxImagePickerManager;
import com.rx.img.manager.RxPickerConfig;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by henry on 2019/5/5.
 */

public class RxImagePicker {
    private static final int CAMERA_PERMISSION = 0x12345;

    private RxImagePicker(RxPickerConfig config) {
        RxImagePickerManager.getInstance().setConfig(config);
    }
    /**
     * init RxPicker
     */
    public static void init(RxImagePickerLoader imageLoader) {
        RxImagePickerManager.getInstance().init(imageLoader);
    }


    /**
     * Using the custom config
     */

    static RxImagePicker with(RxPickerConfig config) {
        return new RxImagePicker(config);
    }

    /**
     * Using the default config
     */
    public static RxImagePicker with() {
        return with(new RxPickerConfig());
    }
    /**
     * Set the selection mode 默认多选
     */
    public RxImagePicker single(boolean single) {
        RxImagePickerManager.getInstance().setMode(single ? RxPickerConfig.SINGLE_IMG : RxPickerConfig.MULTIPLE_IMG);
        return this;
    }

    /**
     * Set the show  Taking pictures;
     */
    public RxImagePicker showCamera(boolean showCamera) {
        RxImagePickerManager.getInstance().showCamera(showCamera);
        return this;
    }

    /**
     * Set the select  image limit
     */
    public RxImagePicker minAndMax(int minValue, int maxValue) {
        RxImagePickerManager.getInstance().limit(minValue, maxValue);
        return this;
    }

    /**
     * start picker from activity
     */
    public Observable<List<Image>> start(AppCompatActivity activity) {
        return start(activity.getSupportFragmentManager());
    }

    /**
     * start picker from fragment
     */
    public Observable<List<Image>> start(Fragment fragment) {
        return start(fragment.getFragmentManager());
    }

    /**
     * start picker from fragment
     */
    private Observable<List<Image>> start(FragmentManager fragmentManager) {
        HandlerResultFragment fragment = (HandlerResultFragment) fragmentManager.findFragmentByTag(HandlerResultFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = HandlerResultFragment.newInstance();
            fragmentManager.beginTransaction().add(fragment, fragment.getClass().getSimpleName()).commit();
        } else if (fragment.isDetached()) {
            fragmentManager.beginTransaction().attach(fragment).commit();
        }
        return getListItem(fragment);
    }
    /**
     * start camera
     */
    public Observable<Image> startCamera(Fragment fragment) {
        return getCameraPhoto(fragment.getActivity());
    }
    /**
     * start camera
     */
    public Observable<Image> startCamera(FragmentActivity activity) {
        return getCameraPhoto(activity);
    }
    private Observable<Image> getCameraPhoto(final FragmentActivity activity) {
        activity.startActivity(new Intent(activity, RxTranslucentActivity.class));
        return RxTranslucentActivity.attachSubject.filter(new Predicate<Boolean>() {
            @Override
            public boolean test(@NonNull Boolean aBoolean) throws Exception {
                return aBoolean;
            }
        }).flatMap(new Function<Boolean, ObservableSource<Image>>() {
            @Override
            public ObservableSource<Image> apply(@NonNull Boolean aBoolean) throws Exception {
                Log.e("我的执行", "------>");
                return RxTranslucentActivity.resultSingle;

            }
        }).take(1);
    }
    private Observable<List<Image>> getListItem(final HandlerResultFragment finalFragment) {
        return finalFragment.getAttachSubject().filter(new Predicate<Boolean>() {
            @Override
            public boolean test(@NonNull Boolean aBoolean) throws Exception {
                Log.e("我的执行", "getListItem");
                return aBoolean;
            }
        }).flatMap(new Function<Boolean, ObservableSource<List<Image>>>() {
            @Override
            public ObservableSource<List<Image>> apply(@NonNull Boolean aBoolean) throws Exception {
                Log.e("我的执行", "flatMap");
                Intent intent = new Intent(finalFragment.getActivity(), RxImagePickerActivity.class);
                finalFragment.startActivityForResult(intent, HandlerResultFragment.REQUEST_CODE);
                return finalFragment.getResultSubject();

            }
        }).take(1);
    }
}
