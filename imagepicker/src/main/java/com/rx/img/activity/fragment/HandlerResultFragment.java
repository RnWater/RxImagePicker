package com.rx.img.activity.fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import com.rx.img.bean.Image;
import com.rx.img.manager.RxImagePickerManager;
import java.util.List;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import static android.app.Activity.RESULT_OK;
/**
 * Created by henry on 2019/5/6.
 */
public class HandlerResultFragment extends Fragment{
    PublishSubject<List<Image>> resultSubject = PublishSubject.create();
    BehaviorSubject<Boolean> attachSubject = BehaviorSubject.create();

    public static final int REQUEST_CODE = 0x00100;

    public static HandlerResultFragment newInstance() {
        return new HandlerResultFragment();
    }

    public PublishSubject<List<Image>> getResultSubject() {
        return resultSubject;
    }

    public BehaviorSubject<Boolean> getAttachSubject() {
        return attachSubject;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE && data != null) {
                resultSubject.onNext(RxImagePickerManager.getInstance().getResult(data));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attachSubject.onNext(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            attachSubject.onNext(true);
        }
    }
}
