package com.calf.frame.storage;

import android.text.TextUtils;
import android.util.Base64;

import com.calf.frame.tool.Assert;
import com.calf.frame.log.Logger;
import com.calf.frame.utils.DateUtils;
import com.calf.frame.utils.FileUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JinYi Liu
 */
public class CacheManager {

    public static final long MILLISECOND = 1;
    public static final long SECOND = 1000 * MILLISECOND;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;

    private static final String SEPARATOR = File.separator;
    private static final String CLASS_NAME = CacheManager.class.getSimpleName();
    private static CacheManager mInstance = new CacheManager();

    private List<String> mSecondDirs;

    private CacheManager() {
        mSecondDirs = new ArrayList<String>();
    }

    public static CacheManager getInstance() {
        return mInstance;
    }

    public boolean put(String dir, String key, byte[] value) {
        return put(dir, 0, key, value);
    }

    public boolean put(String dir, long millis, String key, byte[] value) {
        boolean flag = false;
        if (TextUtils.isEmpty(dir) || TextUtils.isEmpty(key) || value == null) {
            Assert.classAssert(false, CLASS_NAME + " [put] parameter is Empty");
            return flag;
        }
        BufferedOutputStream bos = null;
        String path = createCacheFilePath(dir, key);
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            if (!file.canWrite()) {
                Logger.v(CLASS_NAME, CLASS_NAME + " [put] can't have write permission");
                return flag;
            }
            if (file.getFreeSpace() < value.length) {
                Logger.v(CLASS_NAME, CLASS_NAME + " [put] freeSpace less than value length");
                return flag;
            }
            if (!file.isAbsolute()) {
                file = file.getAbsoluteFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            String cacheHead = null;
            if (millis > 0) {
                cacheHead = createCacheHead(millis);
            }
            if (!TextUtils.isEmpty(cacheHead)) {
                fos.write(cacheHead.getBytes());
            }
            fos.write(value);
            flag = true;
        } catch (Exception e) {
            Logger.printStackTrace(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Logger.printStackTrace(e);
                    flag = false;
                }
            }
            if (!flag) {
                try {
                    FileUtils.delete(file);
                } catch (IOException e) {
                    Logger.printStackTrace(e);
                }
            }
        }
        return flag;
    }

    public byte[] get(String dir, String key) {
        return get(dir, key, false);
    }

    public byte[] get(String dir, String key, boolean useExpiredCache) {
        byte[] bytes = null;
        if (TextUtils.isEmpty(dir) || TextUtils.isEmpty(key)) {
            Assert.classAssert(false, CLASS_NAME + " [get] parameter is Empty");
            return bytes;
        }
        String path = createCacheFilePath(dir, key);
        File file = new File(path);
        if (!file.exists()) {
            Logger.v(CLASS_NAME, CLASS_NAME + " [get] file not exist");
            return bytes;
        }
        if (!file.canRead()) {
            Logger.v(CLASS_NAME, CLASS_NAME + " [get] can't have read permission");
            return bytes;
        }
        FileInputStream fis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            boolean expire = true;
            String firstReadStr = null;
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 4];
            int length = fis.available();
            if (length > 30) {
                fis.read(buffer, 0, 30);
                String str = new String(buffer, 0, 30);
                String regex = "[S][0-9]{14}[S][0-9]{13}[S]";
                boolean match = str.matches(regex);
                if (match) {
                    String[] arr = str.split("S");
                    long cacheMillis = Long.parseLong(arr[1]);
                    long expiredMillis = Long.parseLong(arr[2]);
                    expire = DateUtils.isApartMills(cacheMillis, expiredMillis);
                } else {
                    firstReadStr = str;
                }
            } else {
                fis.read(buffer, 0, length);
                firstReadStr = new String(buffer, 0, length);
            }
            if (!expire || (useExpiredCache && expire)) {
                if (!TextUtils.isEmpty(firstReadStr)) {
                    bos.write(firstReadStr.getBytes());
                }
                while ((length = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, length);
                }
                bytes = bos.toByteArray();
            }
        } catch (Exception e) {
            Logger.printStackTrace(e);
        } catch (OutOfMemoryError e) {
            Logger.printStackTrace(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Logger.printStackTrace(e);
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Logger.printStackTrace(e);
                }
            }
        }
        return bytes;
    }

    public boolean remove(String dir, String key) {
        boolean flag = false;
        String path = createCacheFilePath(dir, key);
        try {
            flag = FileUtils.delete(path);
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
        return flag;
    }

    public boolean isExist(String dir, String key) {
        if (TextUtils.isEmpty(dir) || TextUtils.isEmpty(key)) {
            Assert.classAssert(false, CLASS_NAME + " [isExist] parameter is Empty");
            return false;
        }
        return new File(createCacheFilePath(dir, key)).exists();
    }

    public boolean isExpired(String dir, String key) {
        boolean flag = true;
        if (TextUtils.isEmpty(dir) || TextUtils.isEmpty(key)) {
            Assert.classAssert(false, CLASS_NAME + " [isExpired] parameter is Empty");
            return flag;
        }
        File file = new File(createCacheFilePath(dir, key));
        if (file.exists()) {
            if (file.canRead()) {
                FileInputStream fis = null;
                try {
                    byte[] buffer = new byte[1024 * 4];
                    fis = new FileInputStream(file);
                    fis.read(buffer, 0, 30);
                    String cacheHead = new String(buffer);
                    String regex = "[S][0-9]{14}[S][0-9]{13}[S]";
                    boolean match = cacheHead.matches(regex);
                    if (match) {
                        String[] arr = cacheHead.split("S");
                        long cacheMillis = Integer.parseInt(arr[1]);
                        long expiredMillis = Integer.parseInt(arr[2]);
                        flag = DateUtils.isApartMills(cacheMillis, expiredMillis);
                    }
                } catch (Exception e) {
                    Logger.printStackTrace(e);
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            Logger.printStackTrace(e);
                        }
                    }
                }
            } else {
                Logger.v(CLASS_NAME, CLASS_NAME + " [isExpired] can't have read permission");
            }
        } else {
            Logger.v(CLASS_NAME, CLASS_NAME + " [isExpired] file not exist");
        }
        return flag;
    }

    public void clean(String dir) {
        try {
            FileUtils.delete(dir);
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
    }

    public void useSecondDir(String dir) {
        if (!dir.endsWith(SEPARATOR)) {
            dir = dir + SEPARATOR;
        }
        if (!mSecondDirs.contains(dir)) {
            mSecondDirs.add(dir);
        }
    }

    private String createCacheHead(long millis) {
        // S缓存时间S过期时间S
        String cacheMillis = String.valueOf(System.currentTimeMillis());
        while (cacheMillis.length() < 14) {
            cacheMillis = "0" + cacheMillis;
        }
        String expiredMillis = String.valueOf(millis);
        while (expiredMillis.length() < 13) {
            expiredMillis = "0" + expiredMillis;
        }
        StringBuilder sb = new StringBuilder("S");
        sb.append(cacheMillis).append("S").append(expiredMillis).append("S");
        return sb.toString();
    }

    private String createCacheFilePath(String dir, String key) {
        if (!dir.endsWith(SEPARATOR)) {
            dir = dir + SEPARATOR;
        }
        if (mSecondDirs.contains(dir)) {
            dir = dir + Math.abs(key.hashCode()) % 30;
        }
        String base64Key = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
        return (dir + SEPARATOR + base64Key).trim();
    }

}
