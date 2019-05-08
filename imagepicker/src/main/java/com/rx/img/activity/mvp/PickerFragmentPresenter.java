package com.rx.img.activity.mvp;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.rx.img.R;
import com.rx.img.bean.Image;
import com.rx.img.bean.ImageFolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by henry on 2019/5/6.
 */

public class PickerFragmentPresenter extends PickPresent{
    /**
     * Media attribute.媒体库标准参数
     */
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID, // image id.
            MediaStore.Images.Media.DATA, // image absolute path.
            MediaStore.Images.Media.DISPLAY_NAME, // image name.
            MediaStore.Images.Media.DATE_ADDED, // The time to be added to the library.
            MediaStore.Images.Media.BUCKET_ID, // folder id.
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // folder name.
    };
    @Override
    public void loadAllImage(final Context context) {
        loadAllFolder(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override public void accept(@NonNull Disposable disposable) throws Exception {
                        view.showWaitDialog();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override public void run() throws Exception {
                        view.hideWaitDialog();
                    }
                })
                .subscribe(new Consumer<List<ImageFolder>>() {
                    @Override public void accept(@NonNull List<ImageFolder> imageFolders) throws Exception {
                        view.showAllImage(imageFolders);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(@NonNull Throwable throwable) throws Exception {
                        Toast.makeText(context, context.getString(R.string.load_image_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /**
     * Scan the list of pictures in the library.
     */
    private Observable<List<ImageFolder>> loadAllFolder(final Context context) {
        return Observable.just(true).map(new Function<Boolean, List<ImageFolder>>() {
            @Override
            public List<ImageFolder> apply(@NonNull Boolean aBoolean) throws Exception {

                Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
                Map<String, ImageFolder> albumFolderMap = new HashMap<>();

                ImageFolder allImageImageFolder = new ImageFolder();
                allImageImageFolder.isChecked=true;
                allImageImageFolder.folderName=context.getString(R.string.all_phone_folder);
                while (cursor.moveToNext()) {
                    int imageId = cursor.getInt(0);
                    String imagePath = cursor.getString(1);
                    String imageName = cursor.getString(2);
                    long addTime = cursor.getLong(3);

                    int bucketId = cursor.getInt(4);
                    String bucketName = cursor.getString(5);
                    //根据查询到的图片创建一个图片对象然后加入到相应的文件夹中
                    Image imageItem = new Image();
                    imageItem.id = imageId;
                    imageItem.path = imagePath;
                    imageItem.name = imageName;
                    imageItem.addTime = addTime;
                    allImageImageFolder.images.add(imageItem);
                    //将所有图片都添加到全部相册里面，然后再分类
                    ImageFolder imageFolder = albumFolderMap.get(bucketName);
                    if (imageFolder != null) {
                        imageFolder.images.add(imageItem);
                    } else {
                        imageFolder = new ImageFolder();
                        imageFolder.id=bucketId;
                        imageFolder.folderName = bucketName;
                        imageFolder.images.add(imageItem);

                        albumFolderMap.put(bucketName, imageFolder);
                    }
                }
                cursor.close();
                List<ImageFolder> imageFolders = new ArrayList<>();

                Collections.sort(allImageImageFolder.images);//对所有图片进行排序
                imageFolders.add(allImageImageFolder);

                for (Map.Entry<String, ImageFolder> folderEntry : albumFolderMap.entrySet()) {
                    ImageFolder imageFolder = folderEntry.getValue();
                    Collections.sort(imageFolder.images);
                    imageFolders.add(imageFolder);
                }
                return imageFolders;
            }
        });
    }
}
