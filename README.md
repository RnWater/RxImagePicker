# RxImagePicker
基于RxJava的图片选择框架

  1:支持直接调取相机获取拍照返回
	2:支持调取相册
	3:支持相册中是否展示相机按钮
	4:支持自定义图片加载框架
	
使用

1：继承 RxImagePickerLoader 创建自定义的图片加载，实现以下的方法   
	
		void displayImage(ImageView iv, String filePath, int width, int height);
			
此处使用的Glide加载的图片显示，可以自己选择
			
	public class GlideImageLoader implements RxImagePickerLoader {
		@Override
		public void displayImage(ImageView iv, String filePath, int width, int height) {
			Glide.with(iv.getContext()).load(filePath).centerCrop().override(width,height).into(iv);
		}
	}
2：在Activity或者Application中初始化操作

	public class BaseApplication extends Application {
		@Override
		public void onCreate() {
			super.onCreate();
			RxImagePicker.init(new GlideImageLoader());
		}
	}
3：可以配合Lambda表达式使用更方便

//lambda表达式
//多图片选择
	
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
//单图片选择
	
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
//直接调取相机
 
        camera.setOnClickListener(v ->
                RxImagePicker.with().startCamera(this).subscribe(image -> {
                  
                    if (image != null) {
                        Glide.with(imageView.getContext()).load(image.path).asBitmap().into(imageView);
                    }
                })
        );
#如有疑问，或需要提意见的请留言
