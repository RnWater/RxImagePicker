package com.rx.img.manager;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by henry on 2019/5/6.
 */

public class RxEvent {
    private final PublishSubject<Object> bus = PublishSubject.create();

    private static final RxEvent EVENT = new RxEvent();

    public static RxEvent singleton() {
        return EVENT;
    }
    public void post(Object o) {
        bus.onNext(o);
    }
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }
}
