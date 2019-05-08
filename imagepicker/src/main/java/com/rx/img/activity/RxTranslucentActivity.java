package com.rx.img.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rx.img.R;
import com.rx.img.bean.Image;
import com.rx.img.manager.CameraHelper;

import java.io.File;
import java.util.List;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 主要做为权限处理的透明界面
 */
public class RxTranslucentActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    public static BehaviorSubject<Boolean> attachSubject = BehaviorSubject.create();
    public static PublishSubject<Image> resultSingle = PublishSubject.create();
    public static final int CAMERA_REQUEST_CODE = 0x00200;
    public static final int WRITE_REQUEST_CODE = 0x00300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_translucent);
        requestPermission();
    }
    private  int requestCount = 0;
    /**
     * 申请权限
     */
    private void requestPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)
                &&EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            attachSubject.onNext(true);
            CameraHelper.take(this, CAMERA_REQUEST_CODE);
        } else {
            if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA) && !EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                EasyPermissions.requestPermissions(this, "需要使用相机和存储权限",
                        CAMERA_REQUEST_CODE, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);
                requestCount=2;
            }else {
                requestCount=1;
                if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
                    EasyPermissions.requestPermissions(this, "需要使用相机权限",
                            CAMERA_REQUEST_CODE, Manifest.permission.CAMERA);
                } else if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    EasyPermissions.requestPermissions(this, "需要使用存储照片权限",
                            WRITE_REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        }
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCount == perms.size()) {
            attachSubject.onNext(true);
            CameraHelper.take(this, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            setDescriptionDialog("相机使用");
        } else {
            setDescriptionDialog("照片保存");
        }
    }

    /**
     * 设置权限提醒
     * @param description
     */
    public void setDescriptionDialog(String description){
        AppSettingsDialog build = new AppSettingsDialog.Builder(this).
                setTitle("权限提醒").
                setRationale("请开启"+description+"权限,否则将无法为您提供服务").
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).
                setPositiveButton("去开启").
                build();
        build.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                File file = CameraHelper.getTakeImageFile();
                CameraHelper.scanPic(this, file);
                Image item = new Image();
                item.id=0;
                item.path=file.getAbsolutePath();
                item.name=file.getName();
                item.addTime=System.currentTimeMillis();
                resultSingle.onNext(item);
            }
        }
        finish();
    }
}
