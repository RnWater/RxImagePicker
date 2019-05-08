package com.rx.img.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rx.img.R;
import com.rx.img.bean.Image;
import com.rx.img.manager.RxEvent;
import com.rx.img.manager.RxImagePickerManager;
import com.rx.img.manager.RxPickerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 2019/5/6.
 */

public class PickerFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int CAMERA_TYPE = 0;
    private static final int NORMAL_TYPE = 1;

    private View.OnClickListener clickListener;

    private int imageWidth;
    private RxPickerConfig config;

    private List<Image> datas;//数据源
    private List<Image> checkImage;//选中数据

    public PickerFragmentAdapter(int imageWidth) {
        config = RxImagePickerManager.getInstance().getConfig();
        this.imageWidth = imageWidth;
        checkImage = new ArrayList<>();
    }
    public void setData(List<Image> data){
        this.datas = data;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (CAMERA_TYPE == viewType) {
            return new CameraViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false));
        } else {
            return new PickerHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picker, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CameraViewHolder) {
            viewHolder.itemView.setOnClickListener(clickListener);
            return;
        }
        int dataPosition = config.isShowCamera() ? position - 1 : position;//当前数据源游标减一

        final Image imageItem = datas.get(dataPosition);
        PickerHolder pickerViewHolder = (PickerHolder) viewHolder;
        pickerViewHolder.bind(imageItem);

        pickerViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (config.isSingle()) {
                    RxEvent.singleton().post(imageItem);
                } else {
                    int maxValue = config.getMaxValue();
                    if (checkImage.size() == maxValue && !checkImage.contains(imageItem)) {
                        Toast.makeText(viewHolder.itemView.getContext(), viewHolder.itemView.getContext().getString(R.string.max_select, ""+config.getMaxValue()), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (checkImage.contains(imageItem)){
                        checkImage.remove(imageItem);
                    }else {
                        checkImage.add(imageItem);
                    }
                    notifyItemChanged(viewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (datas != null && config.isShowCamera()) {
            return datas.size() + 1;
        } else if (datas != null) {
            return datas.size();
        }
        return 0;
    }
    @Override public int getItemViewType(int position) {
        if (config.isShowCamera() && position == 0) {
            return CAMERA_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }


    public void setCameraClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
    public List<Image> getCheckImage() {
        return checkImage;
    }
    public class PickerHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private AppCompatCheckBox cbCheck;

        private PickerHolder(View itemView) {
            super(itemView);
            imageView =  itemView.findViewById(R.id.iv_image);
            cbCheck =  itemView.findViewById(R.id.cb_check);
        }
        private void bind(Image imageItem) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = imageWidth;
            layoutParams.height = imageWidth;
            imageView.setLayoutParams(layoutParams);

            RxImagePickerManager.getInstance().display(imageView, imageItem.path, imageWidth, imageWidth);
            cbCheck.setVisibility(config.isSingle() ? View.GONE : View.VISIBLE);
            cbCheck.setChecked(checkImage.contains(imageItem));
        }
    }
    private class CameraViewHolder extends RecyclerView.ViewHolder {
        private CameraViewHolder(View itemView) {
            super(itemView);
        }
    }
}
