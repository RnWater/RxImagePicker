package com.rx.img.activity;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.rx.img.R;
import com.rx.img.activity.fragment.PickerFragment;
import com.rx.img.bean.Image;
import com.rx.img.manager.RxImagePickerManager;
import java.util.ArrayList;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
public class RxImagePickerActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private static final int READ_STORAGE_PERMISSION = 0x001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_rx_image_picker);
        requestPermission();
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            setupFragment();
        } else {
            EasyPermissions.requestPermissions(this, "需要读取相册权限",
                    READ_STORAGE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(RxImagePickerManager.MEDIA_RESULT, new ArrayList<Image>());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        setupFragment();//此处只申请一个权限所以不做判断。
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        new AppSettingsDialog.Builder(this).
                setTitle("权限提醒").
                setRationale("请开启相册权限,否则将无法为您提供服务").
                setNegativeButton("取消").
                setPositiveButton("去开启").
                build().show();
    }
    private void setupFragment() {
        String tag = PickerFragment.class.getSimpleName();
        PickerFragment fragment = (PickerFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = PickerFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commitAllowingStateLoss();
    }
}
