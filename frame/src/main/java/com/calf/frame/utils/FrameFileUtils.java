package com.calf.frame.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.calf.frame.log.DebugAssert;
import com.calf.frame.log.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author JinYi Liu
 */
public class FrameFileUtils {

    private static final String TAG = "FrameUtils";
    private static final String CLASS_NAME = FrameFileUtils.class.getSimpleName();

    /**
     * 获取文件扩展名
     */
    public String getFileNameExtension(String fileName) {
        String extension = "";
        if (!TextUtils.isEmpty(fileName)) {
            int index = fileName.lastIndexOf(".");
            if (index != -1) {
                extension = fileName.substring(index + 1, fileName.length());
            }
        }
        Logger.v(TAG, fileName + " extension is " + extension);
        return extension;
    }

    /**
     * 获取除扩展名以外的部分
     */
    public static String getFileNameWithoutExtension(String fileName) {
        String fileNameWithoutExtension = "";
        if (!TextUtils.isEmpty(fileName)) {
            int index = fileName.lastIndexOf(".");
            if (index != -1) {
                fileNameWithoutExtension = fileName.substring(0, index);
            }
        }
        Logger.v(TAG, fileName + " fileNameWithoutExtension is " + fileNameWithoutExtension);
        return fileNameWithoutExtension;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File getExternalStorageMusicDirectory() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getExternalStorageDownloadsDirectory() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static boolean delete(String path) throws IOException {
        return delete(new File(path));
    }

    public static boolean delete(File file) throws IOException {
        DebugAssert.classAssert(file != null, CLASS_NAME + " [delete] file is null");
        boolean flag = false;
        if (file.exists()) {
            if (file.canWrite()) {
                if (file.isFile()) {
                    flag = deleteFile(file);
                } else if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File f : files) {
                        delete(file);
                    }
                    flag = deleteFile(file);
                } else {
                    // 文件存在,但是又不是文件,也不是文件夹,这种情况存在么?
                }
            } else {
                Logger.v(TAG, CLASS_NAME + " [delete] can't have write permission:" + file.getAbsolutePath());
                throw new IOException("file can't have write permission");
            }
        } else {
            Logger.v(TAG, CLASS_NAME + " [delete] file not exists:" + file.getAbsolutePath());
            throw new FileNotFoundException("file not exist");
        }
        return flag;
    }

    private static boolean deleteFile(File file) {
        boolean flag;
        flag = file.delete();
        if (!flag) {
            flag = file.renameTo(new File(String.valueOf(file.hashCode())));
        }
        return flag;
    }


}
