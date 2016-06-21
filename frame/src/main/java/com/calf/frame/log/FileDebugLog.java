package com.calf.frame.log;

/**
 * @author JinYi Liu
 */
public class FileDebugLog implements Log {

    private static FileDebugLog mLog;

    private Log mFileLog;
    private Log mDebugLog;

    private FileDebugLog() {
        mFileLog = FileLog.getInstance();
        mDebugLog = DebugLog.getInstance();
    }

    @Override
    public void printStackTrace(Throwable e) {
        mFileLog.printStackTrace(e);
        mDebugLog.printStackTrace(e);
    }

    @Override
    public void d(String tag, String message) {
        mFileLog.d(tag, message);
        mDebugLog.d(tag, message);
    }

    @Override
    public void i(String tag, String message) {
        mFileLog.i(tag, message);
        mDebugLog.i(tag, message);
    }

    @Override
    public void v(String tag, String message) {
        mFileLog.v(tag, message);
        mDebugLog.v(tag, message);
    }

    @Override
    public void w(String tag, String message) {
        mFileLog.w(tag, message);
        mDebugLog.w(tag, message);
    }

    @Override
    public void e(String tag, String message) {
        mFileLog.e(tag, message);
        mDebugLog.e(tag, message);
    }

    static Log getInstance() {
        if (mLog == null) {
            synchronized (FileDebugLog.class) {
                if (mLog == null) {
                    mLog = new FileDebugLog();
                }
            }
        }
        return mLog;
    }

}
