package com.rx.img.base;

import com.rx.img.activity.mvp.ShowImageView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by henry on 2019/5/6.
 */

public abstract class BasePresenter<V extends ShowImageView> {

    public V view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    void attachModelView(V v) {
        this.view = v;
    }

    void detachView() {
        this.view = null;
        if (compositeDisposable != null && compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public Disposable add(Disposable disposable) {
        compositeDisposable.add(disposable);
        return disposable;
    }
}
