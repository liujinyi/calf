package com.calf.frame.log;

/**
 * @author JinYi Liu
 */
class ReleaseLog implements Log {

    private static Log mLog;

    private ReleaseLog() {
    }

    @Override
    public void printStackTrace(Throwable e) {
    }

    @Override
    public void d(String tag, String message) {
    }

    @Override
    public void i(String tag, String message) {
    }

    @Override
    public void v(String tag, String message) {
    }

    @Override
    public void w(String tag, String message) {
    }

    @Override
    public void e(String tag, String message) {
    }

    static Log getInstance() {
        if (mLog == null) {
            synchronized (ReleaseLog.class) {
                if (mLog == null) {
                    mLog = new ReleaseLog();
                }
            }
        }
        return mLog;
    }

}
