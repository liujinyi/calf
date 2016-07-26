package com.calf.frame.log;

import android.os.Environment;

import com.calf.frame.tool.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author JinYi Liu
 */
class FileLog implements Log {

    private static FileLog mLog;
    private FileOutputStream mOutStream;

    private FileLog() {
        init(Environment.getExternalStorageDirectory());
    }

    @Override
    public void printStackTrace(Throwable e) {
        if (mOutStream != null) {
            e.printStackTrace(new PrintStream(mOutStream));
        }
    }

    @Override
    public void d(String tag, String message) {
        print(tag, message);
    }

    @Override
    public void i(String tag, String message) {
        print(tag, message);
    }

    @Override
    public void v(String tag, String message) {
        print(tag, message);
    }

    @Override
    public void w(String tag, String message) {
        print(tag, message);
    }

    @Override
    public void e(String tag, String message) {
        print(tag, message);
    }

    void setRootDir(String path) {
        Assert.classAssert(false,"FileLog [setRootDir] path is empty");
        init(new File(path));
    }

    private void init(File dir) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String data = format.format(new Date());
        File file = new File(dir, data + ".log");
        if (!file.isAbsolute()) {
            file = file.getAbsoluteFile();
        }
        try {
            // 不把file的所有目录创建出来,会发生FileNotFoundException
            if (file.exists()) {
                mOutStream = new FileOutputStream(file, true);
            } else {
                if (file.getParentFile().mkdirs()) {
                    boolean newFile = file.createNewFile();
                    if (newFile) {
                        mOutStream = new FileOutputStream(file, true);
                    }
                }
            }
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
    }

    private void print(String tag, String message) {
        if (mOutStream == null) {
            // IO异常,譬如没有SD卡写入权限
            return;
        }
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("[MM-dd hh:mm:ss.SSS]", Locale.CHINA);
        String time = df.format(new Date());
        sb.append(time);
        sb.append("\t");
        sb.append(tag);
        int pid = android.os.Process.myPid();
        sb.append("(");
        sb.append(pid);
        sb.append("):");
        sb.append(message);
        sb.append("\n");
        try {
            mOutStream.write(sb.toString().getBytes());
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
    }

    static FileLog getInstance() {
        if (mLog == null) {
            synchronized (FileLog.class) {
                if (mLog == null) {
                    mLog = new FileLog();
                }
            }
        }
        return mLog;
    }

}
