package com.rx.img.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.rx.img.R;
import com.rx.img.activity.adapter.PreviewAdapter;
import com.rx.img.bean.Image;

import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager vpPreview;
    private PreviewAdapter vpAdapter;

    private List<Image> data;

    public static void start(Context context, List<Image> data) {
        final ArrayList<Image> images = (ArrayList<Image>) data;
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra("preview_list", images);
        context.startActivity(intent);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_preview);
        handleData();
        setupToolbar();
        vpPreview = findViewById(R.id.vp_preview);
        vpAdapter = new PreviewAdapter(data);
        vpPreview.setAdapter(vpAdapter);
        vpPreview.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                toolbar.setTitle(position + 1 + "/" + data.size());
            }
        });
    }

    private void handleData() {
        data = (List<Image>) getIntent().getSerializableExtra("preview_list");
    }

    private void setupToolbar() {
        toolbar =  findViewById(R.id.nav_top_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("1/" + data.size());
    }
}
