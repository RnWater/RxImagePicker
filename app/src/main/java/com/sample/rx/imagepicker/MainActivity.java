package com.sample.rx.imagepicker;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rx.img.RxImagePicker;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Button single;
    private Button multi;
    private Button camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        imageView.setImageBitmap(BitmapFactory.decodeFile("/storage/0000-0000/DCIM/camera/IMG_20190508_211839.jpg"));
        multi = findViewById(R.id.multi);
        camera = findViewById(R.id.camera);
        single = findViewById(R.id.single);
        //lambda表达式
        multi.setOnClickListener(v->
                RxImagePicker.with()
                        .single(false)
                        .minAndMax(1,5)
                        .showCamera(true)
                        .start(this).subscribe(images->{
                    if (images != null&&images.size()!=0) {
                        Glide.with(imageView.getContext()).load(images.get(0).path).asBitmap().into(imageView);
                    }
                })
        );
        single.setOnClickListener(v->
                RxImagePicker.with()
                        .single(true)
                        .showCamera(true)
                        .start(this).subscribe(images->{
                    if (images != null&&images.size()!=0) {
                        Glide.with(imageView.getContext()).load(images.get(0).path).asBitmap().into(imageView);
                    }
                })
        );
        camera.setOnClickListener(v ->
                RxImagePicker.with().startCamera(this).subscribe(image -> {
                    Log.e("我的执行", "执行回调" + (image == null)+"-->"+image.path);
                    if (image != null) {
                        Glide.with(imageView.getContext()).load(image.path).asBitmap().into(imageView);
                    }
                })
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
