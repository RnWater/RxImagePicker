package com.rx.img.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rx.img.activity.mvp.ShowImageView;
import com.rx.img.utils.ClassUtils;

/**
 * Created by henry on 2019/5/6.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment
        implements ShowImageView {

    public P presenter;
    protected ProgressDialog waitDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        presenter = ClassUtils.getT(this, 0);
        presenter.attachModelView(this);
        initView(view);
        return view;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    public void showWaitDialog() {
        if (!Thread.currentThread().getName().equals("main")) {
            new Handler(Looper.getMainLooper()).post(new DialogRunnable());
        } else {
            new DialogRunnable().run();
        }
    }

    private class DialogRunnable implements Runnable {
        @Override
        public void run() {
            if (waitDialog == null) {
                waitDialog = new ProgressDialog(getActivity());
                waitDialog.setMessage("加载数据..");
            }
            waitDialog.show();
        }
    }

    public void hideWaitDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.detachView();
        }
    }

}
