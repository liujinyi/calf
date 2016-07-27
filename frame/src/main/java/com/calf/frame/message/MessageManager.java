package com.calf.frame.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.calf.frame.tool.Assert;

/**
 * Created by JinYi Liu on 16-7-25.
 */
public final class MessageManager {

    private static final long EXECUTE_TIME = 150;
    private static final Object TOKEN = new Object();

    private static boolean mInit;
    private static Looper mLooper;
    private static Handler mHandler;
    private static long mMainThreadID;

    public static void init() {
        checkHasInitRepeatedly();
        mLooper = Looper.getMainLooper();
        mHandler = new Handler(mLooper);
        mMainThreadID = mLooper.getThread().getId();
        mInit = true;
    }

    /**
     * Runnable 中的代码会在主线程中执行,请不要执行耗时操作
     */
    public static boolean post(Runnable r) {
        return postDelayed(0, r);
    }

    /**
     * Runnable 中的代码会在主线程中执行,请不要执行耗时操作
     *
     * @param delayMillis 延时多少毫秒后执行
     */
    public static boolean postDelayed(long delayMillis, Runnable r) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return sendMessageAtTime(SystemClock.uptimeMillis() + delayMillis, r, true);
    }

    /**
     * Runnable 中的代码会在主线程中执行,请不要执行耗时操作
     */
    public static boolean asyncPost(Runnable r) {
        return sendMessageAtTime(SystemClock.uptimeMillis(), r, false);
    }

    private static boolean sendMessageAtTime(long uptimeMillis, Runnable r, boolean sync) {
        checkHasInit();
        boolean flag = true;
        long start = SystemClock.uptimeMillis();
        if (isMainThread() && sync && uptimeMillis - SystemClock.uptimeMillis() == 0) {
            r.run();
        } else {
            Message message = Message.obtain(mHandler, r);
            message.obj = TOKEN;
            flag = mHandler.sendMessageAtTime(message, uptimeMillis);
        }
        long end = SystemClock.uptimeMillis();
        if (end - start > EXECUTE_TIME) {
            Assert.classAssert(false, "sendMessageAtTime execute surpass " + EXECUTE_TIME);
        }
        return flag;
    }

    public static void removeCallbacks(Runnable r) {
        checkHasInit();
        mHandler.removeCallbacks(r);
    }

    public static void removeAllCallbacks() {
        checkHasInit();
        mHandler.removeCallbacksAndMessages(TOKEN);
    }

    private static boolean isMainThread() {
        return mMainThreadID == Thread.currentThread().getId();
    }

    private static void checkHasInit() {
        if (!mInit) {
            throw new IllegalArgumentException("MessageManager [init] not execute");
        }
    }

    private static void checkHasInitRepeatedly() {
        if (mInit) {
            throw new IllegalArgumentException("MessageManager [init] has execute");
        }
    }

}
