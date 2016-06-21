package com.calf.frame.log;

/**
 * @author JinYi Liu
 */
interface Log {

    void printStackTrace(Throwable e);

    void d(String tag, String message);

    void i(String tag, String message);

    void v(String tag, String message);

    void w(String tag, String message);

    void e(String tag, String message);

}