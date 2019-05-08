package com.rx.img.activity.adapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rx.img.bean.Image;
import com.rx.img.manager.RxImagePickerManager;
import com.rx.img.utils.DensityUtil;
import java.util.List;
/**
 * Created by henry on 2019/5/6.
 */
public class PreviewAdapter extends PagerAdapter {

    private List<Image> data;

    public PreviewAdapter(List<Image> data) {
        this.data = data;
    }

    @Override public int getCount() {
        return data.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        AppCompatImageView imageView = new AppCompatImageView(container.getContext());
        int deviceWidth = DensityUtil.getScreenWidth(container.getContext());
        int deviceHeight = DensityUtil.getScreenHeight(container.getContext());
        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        layoutParams.height = deviceHeight;
        layoutParams.width = deviceWidth;
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Image image = data.get(position);
        container.addView(imageView);
        RxImagePickerManager.getInstance()
                .display(imageView, image.path, deviceWidth, deviceHeight);
        return imageView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
