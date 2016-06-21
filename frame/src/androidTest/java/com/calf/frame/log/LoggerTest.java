package com.calf.frame.log;

import android.test.InstrumentationTestCase;

/**
 * @author JinYi Liu
 */
public class LoggerTest extends InstrumentationTestCase {

    private static final String TAG = "LoggerTest";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testDebugLog() throws Exception {
        Logger.changeLog(Logger.Type.DEBUG);
        printLog();
    }

    public void testReleaseLog() throws Exception {
        Logger.changeLog(Logger.Type.RELEASE);
        printLog();
    }

    public void testFileLog() throws Exception {
        Logger.changeLog(Logger.Type.FILE);
        printLog();
    }

    public void testFileDebugLog() throws Exception {
        Logger.changeLog(Logger.Type.FILE_DEBUG);
        printLog();
    }

    private void printLog() {
        Logger.d(TAG, "Test Logger.d('------------------')");
        Logger.i(TAG, "Test Logger.i('------------------')");
        Logger.v(TAG, "Test Logger.v('------------------')");
        Logger.w(TAG, "Test Logger.w('------------------')");
        Logger.e(TAG, "Test Logger.e('------------------')");
        try {
            Object obj = null;
            obj.toString();
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

}