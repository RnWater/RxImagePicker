package com.rx.img.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rx.img.R;
import com.rx.img.bean.FolderClickEvent;
import com.rx.img.bean.ImageFolder;
import com.rx.img.manager.RxEvent;
import com.rx.img.manager.RxImagePickerManager;

import java.util.List;

/**
 * Created by henry on 2019/5/6.
 */

public class PickerFolderAdapter extends RecyclerView.Adapter<PickerFolderAdapter.ViewHolder> {
    private int imageWidth;
    private List<ImageFolder> folders;
    private int checkPosition = 0;

    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public PickerFolderAdapter(List<ImageFolder> folders, int width) {
        this.folders = folders;
        imageWidth = width;
    }
    @NonNull
    @Override
    public PickerFolderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickerFolderAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.bind(folders.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onClick(v);
                if (checkPosition == position) return;
                ImageFolder newFolder = folders.get(position);
                ImageFolder oldFolder = folders.get(checkPosition);
                oldFolder.isChecked=false;
                newFolder.isChecked=true;
                notifyItemChanged(checkPosition);
                notifyItemChanged(position);
                checkPosition = position;
                RxEvent.singleton().post(new FolderClickEvent(position, newFolder));
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivPreView;
        private ImageView ivCheck;

        private ViewHolder(View itemView) {
            super(itemView);
            tvName =  itemView.findViewById(R.id.tv_folder_name);
            ivPreView =  itemView.findViewById(R.id.iv_preview);
            ivCheck =  itemView.findViewById(R.id.iv_check);
        }
        private void bind(ImageFolder imageFolder) {
            tvName.setText(imageFolder.folderName);
            String path = imageFolder.images.get(0).path;
            RxImagePickerManager.getInstance().display(ivPreView, path, imageWidth, imageWidth);
            ivCheck.setVisibility(imageFolder.isChecked ? View.VISIBLE : View.GONE);
        }
    }
}
