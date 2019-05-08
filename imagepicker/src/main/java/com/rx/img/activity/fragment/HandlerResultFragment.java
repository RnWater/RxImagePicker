package com.rx.img.activity.fragment;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.rx.img.bean.Image;
import com.rx.img.bean.ImageFolder;
import com.rx.img.manager.CameraHelper;
import com.rx.img.manager.RxImagePickerManager;

import java.io.File;
import java.util.List;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

/**
 * Created by henry on 2019/5/6.
 */
public class HandlerResultFragment extends Fragment{
    PublishSubject<List<Image>> resultSubject = PublishSubject.create();
    PublishSubject<Image> resultSingle = PublishSubject.create();
    BehaviorSubject<Boolean> attachSubject = BehaviorSubject.create();

    public static final int REQUEST_CODE = 0x00100;
    public static final int CAMERA_REQUEST_CODE = 0x00200;

    public static HandlerResultFragment newInstance() {
        return new HandlerResultFragment();
    }

    public PublishSubject<List<Image>> getResultSubject() {
        return resultSubject;
    }
    public PublishSubject<Image> getSingleResult() {
        return resultSingle;
    }

    public BehaviorSubject<Boolean> getAttachSubject() {
        return attachSubject;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE && data != null) {
                resultSubject.onNext(RxImagePickerManager.getInstance().getResult(data));
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                File file = CameraHelper.getTakeImageFile();
                CameraHelper.scanPic(getActivity(), file);
                Image item = new Image();
                item.id=0;
                item.path=file.getAbsolutePath();
                item.name=file.getName();
                item.addTime=System.currentTimeMillis();
                resultSingle.onNext(item);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attachSubject.onNext(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            attachSubject.onNext(true);
        }
    }
}
