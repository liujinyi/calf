package com.calf.frame.log;

/**
 * @author JinYi Liu
 */
class DebugLog implements Log {

    private static Log mLog;

    private DebugLog() {
    }

    @Override
    public void printStackTrace(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void d(String tag, String message) {
        android.util.Log.d(tag, message);
    }

    @Override
    public void i(String tag, String message) {
        android.util.Log.i(tag, message);
    }

    @Override
    public void v(String tag, String message) {
        android.util.Log.v(tag, message);
    }

    @Override
    public void w(String tag, String message) {
        android.util.Log.w(tag, message);
    }

    @Override
    public void e(String tag, String message) {
        android.util.Log.e(tag, message);
    }

    static Log getInstance() {
        if (mLog == null) {
            synchronized (DebugLog.class) {
                if (mLog == null) {
                    mLog = new DebugLog();
                }
            }
        }
        return mLog;
    }

}
