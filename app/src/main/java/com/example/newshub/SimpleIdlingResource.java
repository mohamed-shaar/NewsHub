package com.example.newshub;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource {

    private final AtomicBoolean atomicBoolean = new AtomicBoolean(true);

    @Nullable
    private volatile ResourceCallback mCallback;


    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return atomicBoolean.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        atomicBoolean.set(isIdleNow);
        ResourceCallback callback = this.mCallback;
        if (isIdleNow && callback != null) {
            callback.onTransitionToIdle();
        }
    }
}
