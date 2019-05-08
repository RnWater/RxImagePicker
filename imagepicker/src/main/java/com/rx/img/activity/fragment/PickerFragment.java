package com.rx.img.activity.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.rx.img.R;
import com.rx.img.activity.PreviewActivity;
import com.rx.img.activity.adapter.PickerFragmentAdapter;
import com.rx.img.activity.mvp.PickerFragmentPresenter;
import com.rx.img.activity.mvp.ShowImageView;
import com.rx.img.base.BaseFragment;
import com.rx.img.bean.FolderClickEvent;
import com.rx.img.bean.Image;
import com.rx.img.bean.ImageFolder;
import com.rx.img.manager.CameraHelper;
import com.rx.img.manager.PopWindowManager;
import com.rx.img.manager.RxEvent;
import com.rx.img.manager.RxImagePickerManager;
import com.rx.img.manager.RxPickerConfig;
import com.rx.img.utils.DensityUtil;
import com.rx.img.view.DividerGridItemDecoration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.AppSettingsDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by henry on 2019/5/6.
 * 真正加载图片的界面
 */
public class PickerFragment extends BaseFragment<PickerFragmentPresenter> implements ShowImageView, View.OnClickListener {

    public static final int DEFAULT_SPAN_COUNT = 3;
    public static final int CAMERA_REQUEST = 0x0011;
    private static final int CAMERA_PERMISSION = 0x0022;
    private TextView title;
    private RecyclerView recyclerView;
    private LinearLayout ivSelectPreview;
    private TextView tvSelectOk;
    private RelativeLayout rlBottom;

    private PickerFragmentAdapter adapter;
    private List<ImageFolder> allFolder;

    private RxPickerConfig config;
    private Disposable folderSubscribe;
    private Disposable imageSubscribe;

    public static PickerFragment newInstance() {
        return new PickerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picker;
    }

    @Override
    protected void initView(View view) {
        config = RxImagePickerManager.getInstance().getConfig();
        recyclerView =  view.findViewById(R.id.recyclerView);
        title =  view.findViewById(R.id.title);
        ivSelectPreview =  view.findViewById(R.id.iv_select_preview);
        ivSelectPreview.setOnClickListener(this);
        tvSelectOk =  view.findViewById(R.id.iv_select_ok);
        tvSelectOk.setOnClickListener(this);
        rlBottom =  view.findViewById(R.id.rl_bottom);
        rlBottom.setVisibility(config.isSingle() ? View.GONE : View.VISIBLE);
        initToolbar(view);
        initRecycler();
        initObservable();
        loadData();
    }

    private void initToolbar(View view) {
        Toolbar toolbar =  view.findViewById(R.id.nav_top_bar);
        final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appCompatActivity.onBackPressed();
            }
        });
    }

    private void initObservable() {
        folderSubscribe = RxEvent.singleton().toObservable(FolderClickEvent.class).subscribe(new Consumer<FolderClickEvent>() {
            @Override
            public void accept(@NonNull FolderClickEvent folderClickEvent) throws Exception {
                String folderName = folderClickEvent.getFolder().folderName;
                title.setText(folderName);
                refreshData(allFolder.get(folderClickEvent.getPosition()));
            }
        });

        imageSubscribe = RxEvent.singleton().toObservable(Image.class).subscribe(new Consumer<Image>() {
            @Override
            public void accept(@NonNull Image imageItem) throws Exception {
                ArrayList<Image> data = new ArrayList<>();
                data.add(imageItem);
                handleResult(data);
            }
        });
    }

    private void loadData() {
        presenter.loadAllImage(getActivity());
    }

    private void refreshData(ImageFolder folder) {
        adapter.setData(folder.images);
        adapter.notifyDataSetChanged();
    }

    private void initPopWindow(List<ImageFolder> data) {
        try {
            new PopWindowManager().init(title, data);
        } catch (Exception e) {

        }
    }

    private void initRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), DEFAULT_SPAN_COUNT);
        recyclerView.setLayoutManager(layoutManager);

        final DividerGridItemDecoration decoration = new DividerGridItemDecoration(getActivity());
        Drawable divider = decoration.getDivider();
        int imageWidth = DensityUtil.getScreenWidth(getActivity()) / DEFAULT_SPAN_COUNT + divider.getIntrinsicWidth() * DEFAULT_SPAN_COUNT - 1;

        adapter = new PickerFragmentAdapter(imageWidth);
        adapter.setCameraClickListener(new CameraClickListener());
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                tvSelectOk.setText(getString(R.string.select_confirm, adapter.getCheckImage().size(), config.getMaxValue()));
            }
        });

        tvSelectOk.setText(getString(R.string.select_confirm, adapter.getCheckImage().size(), config.getMaxValue()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            handleCameraResult();
        }
    }
    private void handleCameraResult() {
        try {
            File file = CameraHelper.getTakeImageFile();
            CameraHelper.scanPic(getActivity(), file);
            for (ImageFolder imageFolder : allFolder) {
                imageFolder.isChecked=false;
            }
            ImageFolder allImageFolder = allFolder.get(0);
            allImageFolder.isChecked=true;
            Image item = new Image();
            item.id=0;
            item.path=file.getAbsolutePath();
            item.name=file.getName();
            item.addTime=System.currentTimeMillis();
            allImageFolder.images.add(0, item);
            RxEvent.singleton().post(new FolderClickEvent(0, allImageFolder));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "照片存储出错,请重新尝试", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 单选图片传递
     * @param data
     */
    private void handleResult(List<Image> data) {
        final ArrayList<Image> images = (ArrayList<Image>) data;
        Intent intent = new Intent();
        intent.putExtra(RxImagePickerManager.MEDIA_RESULT, images);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!folderSubscribe.isDisposed()) {
            folderSubscribe.dispose();
        }

        if (!imageSubscribe.isDisposed()) {
            imageSubscribe.dispose();
        }
    }

    @Override
    public void onClick(View v) {
        if (tvSelectOk == v) {
            int minValue = config.getMinValue();
            List<Image> checkImage = adapter.getCheckImage();
            if (checkImage.size() < minValue) {
                Toast.makeText(getActivity(), getString(R.string.min_image, minValue), Toast.LENGTH_SHORT).show();
                return;
            }
            handleResult(checkImage);
        } else if (ivSelectPreview == v) {
            List<Image> checkImage = adapter.getCheckImage();
            if (checkImage.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.select_one_image), Toast.LENGTH_SHORT).show();
                return;
            }
            PreviewActivity.start(getActivity(), checkImage);
        }
    }

    @TargetApi(23)
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            takePictures();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictures();
            } else {
                new AppSettingsDialog.Builder(this).
                        setTitle("权限提醒").
                        setRationale("请开启拍照权限,否则将无法为您提供服务").
                        setNegativeButton("取消").
                        setPositiveButton("去开启").
                        build().show();
            }
        }
    }
    private void takePictures() {
        CameraHelper.take(PickerFragment.this, CAMERA_REQUEST);
    }

    @Override
    public void showAllImage(List<ImageFolder> imageFolders) {
        try {
            allFolder = imageFolders;
            adapter.setData(imageFolders.get(0).images);
            adapter.notifyDataSetChanged();
            initPopWindow(imageFolders);
        } catch (Exception e) {

        }
    }

    private class CameraClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermission();
            } else {
                takePictures();
            }
        }
    }
}
