package com.calf.fragments.base;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by JinYi Liu on 16-12-12.
 */

public abstract class BaseTask implements Runnable {

    private final AtomicBoolean mAlive;

    public BaseTask() {
        this.mAlive = new AtomicBoolean(true);
    }

    public final void cancel() {
        mAlive.set(false);
    }

    public final boolean isAlive() {
        return mAlive.get();
    }

}
