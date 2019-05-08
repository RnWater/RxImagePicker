package com.rx.img.manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.rx.img.R;
import com.rx.img.activity.adapter.PickerFolderAdapter;
import com.rx.img.bean.ImageFolder;
import com.rx.img.utils.DensityUtil;

import java.util.List;

/**
 * Created by henry on 2019/5/6.
 */

public class PopWindowManager {
    private PopupWindow mAlbumPopWindow;
    private PickerFolderAdapter adapter;

    public void init(final TextView title, final List<ImageFolder> data) {
        adapter = new PickerFolderAdapter(data, DensityUtil.dp2px(title.getContext(), 80));
        adapter.setListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismissAlbumWindow();
            }
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showPopWindow(v,adapter);
            }
        });
    }

    private void showPopWindow(View v,PickerFolderAdapter adapter) {
        if (mAlbumPopWindow == null) {
            int height = DensityUtil.dp2px(v.getContext(), 300);
            View windowView = createWindowView(v, adapter);
            mAlbumPopWindow =
                    new PopupWindow(windowView, ViewGroup.LayoutParams.MATCH_PARENT, height, true);
            mAlbumPopWindow.setAnimationStyle(R.style.RxImage_PopupAnimation);
            mAlbumPopWindow.setContentView(windowView);
            mAlbumPopWindow.setOutsideTouchable(true);
        }
        mAlbumPopWindow.showAsDropDown(v, 0, 0);
    }

    @NonNull
    private View createWindowView(View clickView, PickerFolderAdapter albumAdapter) {
        View view =
                LayoutInflater.from(clickView.getContext()).inflate(R.layout.item_popwindow_album, null);
        RecyclerView recyclerView = view.findViewById(R.id.album_recycleview);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        View albumShadowLayout = view.findViewById(R.id.album_shadow);
        albumShadowLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dismissAlbumWindow();
            }
        });
        recyclerView.setAdapter(albumAdapter);
        return view;
    }

    private void dismissAlbumWindow() {
        if (mAlbumPopWindow != null && mAlbumPopWindow.isShowing()) {
            mAlbumPopWindow.dismiss();
        }
    }
}
