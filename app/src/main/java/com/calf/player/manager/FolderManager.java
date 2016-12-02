package com.calf.player.manager;

import android.content.Context;

import com.calf.frame.utils.FileUtils;

import java.io.File;

/**
 * @author JinYi Liu
 */
public class FolderManager {

    public static final String SEPARATOR = File.separator;
    public static final int TYPE_ROOT = 0;
    public static final int TYPE_FILES = 1;
    public static final int TYPE_TEMPORARY = 2;
    public static final int TYPE_MUSIC_DOWN = 3;
    public static final int TYPE_IMAGE_CACHE = 4;
    public static final int TYPE_LIBRARY_CACHE = 5;

    private static boolean mInit;
    private static String mRootPath;
    private static String mCachePathRoot;

    public static void init(Context context) {
        if (context == null) {
            throw new NullPointerException("FolderManager [init] context is null");
        }
        if (mInit) {
            return;
        }
        if (FileUtils.isExternalStorageReadable()) {
            mRootPath = context.getExternalCacheDir().getParent();
            mCachePathRoot = mRootPath + SEPARATOR + "cache";
            mInit = true;
        }
    }

    public static String getCachePathRoot() {
        if (!mInit) {
            throw new IllegalStateException("FolderManager [getCachePathRoot] mInit is false ");
        }
        return mCachePathRoot;
    }

    public static String getFolderPath(int folderType) {
        if (!mInit) {
            throw new IllegalStateException("FolderManager [getFolderPath] mInit is false ");
        }
        String path = null;
        switch (folderType) {
            case TYPE_ROOT:
                path = mRootPath;
                break;
            case TYPE_FILES:
                path = mRootPath + SEPARATOR + "files";
                break;
            case TYPE_TEMPORARY:
                path = mRootPath + SEPARATOR + ".temporary";
                break;
            case TYPE_IMAGE_CACHE:
                path = mCachePathRoot + SEPARATOR + ".image";
                break;
            case TYPE_LIBRARY_CACHE:
                path = mCachePathRoot + SEPARATOR + ".library";
                break;
            case TYPE_MUSIC_DOWN:
                path = FileUtils.getExternalStorageMusicDirectory().getAbsolutePath();
                break;
            default:
                throw new RuntimeException("FolderManager [getFolderPath] folderType undefined");
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

}
