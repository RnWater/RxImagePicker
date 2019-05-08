package com.rx.img.manager;
import android.content.Intent;
import android.widget.ImageView;
import com.rx.img.bean.Image;
import com.rx.img.display.RxImagePickerLoader;
import java.util.List;
/**
 * Created by henry on 2019/5/5.
 * 连接Config和显示
 */
public class RxImagePickerManager {
    private RxPickerConfig config;
    private RxImagePickerLoader imageLoader;
    private static RxImagePickerManager manager;
    public static final String MEDIA_RESULT = "MEDIA_RESULT";

    //线程安全
    public static RxImagePickerManager getInstance() {
        if (manager == null) {
            synchronized (RxImagePickerManager.class) {
                if (manager == null) {
                    manager = new RxImagePickerManager();
                }
            }
        }
        return manager;
    }
    private RxImagePickerManager() {
    }

    public RxImagePickerManager setConfig(RxPickerConfig config) {
        this.config = config;
        return this;
    }

    public RxPickerConfig getConfig() {
        return config;
    }
    public void init(RxImagePickerLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
    public void setMode(int mode) {
        config.setMode(mode);
    }
    public void showCamera(boolean showCamera) {
        config.setShowCamera(showCamera);
    }

    public void limit(int minValue, int maxValue) {
        config.setLimit(minValue, maxValue);
    }

    public  void display(ImageView imageView, String path, int width, int height) {
        if (imageLoader == null) {
            throw new NullPointerException("You must fist of all call 'RxImagePicker.init()' to initialize");
        }
        imageLoader.displayImage(imageView, path, width, height);
    }
    /**
     * 获取回调结果
     * @param intent
     * @return
     */
    public List<Image> getResult(Intent intent) {
        return intent.getParcelableArrayListExtra(MEDIA_RESULT);
    }
    /**
     * 获取回调结果
     * @param intent
     * @return
     */
    public Image getSingleResult(Intent intent) {
        return intent.getParcelableExtra(MEDIA_RESULT);
    }
}
