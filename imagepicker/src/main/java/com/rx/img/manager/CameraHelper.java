package com.rx.img.manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Log;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by henry on 2019/5/6.
 * 拍摄图片
 */
public class CameraHelper {
    private static File takeImageFile;
    public static void take(Fragment fragment, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
            takeImageFile = createFile(takeImageFile, "IMG_", ".jpg");
            Uri uri;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                String authorities = ProvideManager.getFileProviderName(fragment.getActivity());
                uri = FileProvider.getUriForFile(fragment.getActivity(), authorities, takeImageFile);
            } else {
                uri = Uri.fromFile(takeImageFile);
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        fragment.startActivityForResult(takePictureIntent, requestCode);
    }

    public static Intent take(Fragment fragment) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
            takeImageFile = createFile(takeImageFile, "IMG_", ".jpg");
            Uri uri;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                String authorities = ProvideManager.getFileProviderName(fragment.getContext());
                uri = FileProvider.getUriForFile(fragment.getContext(), authorities, takeImageFile);
            } else {
                uri = Uri.fromFile(takeImageFile);
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        return takePictureIntent;
    }

    public static File getTakeImageFile() {
        return takeImageFile;
    }

    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    public static void scanPic(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
