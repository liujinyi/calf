package com.calf.frame.log;

/**
 * @author JinYi Liu
 */
public final class Logger {

    private static Type mType = Type.RELEASE;
    private static Log mLog = ReleaseLog.getInstance();

    public static void changeLog(Type type) {
        if (type != null && type != mType) {
            mType = type;
            mLog = createLogByType(type);
        }
    }

    public static void setFileLogRootDir(String path) {
        FileLog.getInstance().setRootDir(path);
    }

    public static void v(String tag, String message) {
        mLog.v(tag, message);
    }

    public static void d(String tag, String message) {
        mLog.d(tag, message);
    }

    public static void i(String tag, String message) {
        mLog.i(tag, message);
    }

    public static void w(String tag, String message) {
        mLog.w(tag, message);
    }

    public static void e(String tag, String message) {
        mLog.e(tag, message);
    }

    public static void printStackTrace(Throwable e) {
        mLog.printStackTrace(e);
    }

    private static Log createLogByType(Type type) {
        switch (type) {
            case FILE:
                return FileLog.getInstance();
            case DEBUG:
                return DebugLog.getInstance();
            case RELEASE:
                return ReleaseLog.getInstance();
            case FILE_DEBUG:
                return FileDebugLog.getInstance();
            default:
                return ReleaseLog.getInstance();
        }
    }

    public enum Type {
        FILE, DEBUG, RELEASE, FILE_DEBUG
    }

}
