package com.calf.fragments.base;

import android.os.Bundle;
import android.text.TextUtils;

import com.calf.frame.http.HttpSession;
import com.calf.frame.log.Logger;
import com.calf.frame.storage.CacheManager;
import com.calf.player.manager.FolderManager;
import com.calf.player.manager.NetworkManager;

import java.io.IOException;
import java.util.zip.Inflater;

import okhttp3.Response;

/**
 * Created by JinYi Liu on 2017/2/8.
 */

public abstract class HttpBehavior<T> implements BaseFragment.Behavior<T>, BaseFragment.BackgroundParser<T> {

    private static final int DEFAULT_START = 0;
    private static final int DEFAULT_COUNT = 30;

    private boolean mUseCache;
    private HttpTask<T> mHttpTask;
    private BaseFragment.CacheParameter mCacheParameter;
    private BaseFragment.HttpRequestStatistics mStatistics;

    protected abstract String giveMeUrl(int start, int count);

    public HttpBehavior() {
        this.mUseCache = true;
        String path = FolderManager.getFolderPath(FolderManager.TYPE_LIBRARY_CACHE);
        this.mCacheParameter = new BaseFragment.CacheParameter(CacheManager.DAY, path, new LibraryDecoder());
    }

    public void setUseCache(boolean useCache) {
        this.mUseCache = useCache;
    }

    public BaseFragment.CacheParameter getCacheParameter() {
        return mCacheParameter;
    }

    public BaseFragment.HttpRequestStatistics getHttpRequestStatistics() {
        return mStatistics;
    }

    @Override
    public final void doInBackground(Bundle savedInstanceState, BaseFragment.Callback<T> callback) {
        if (mHttpTask != null && !mHttpTask.isCancelOrDie()) {
            // is in background loading
            Logger.e(BaseFragment.TAG, "HttpBehavior [doInBackground] mHttpTask is loading");
        } else {
            String url = giveMeUrl(DEFAULT_START, DEFAULT_COUNT);
            mHttpTask = new HttpTask(url, this, callback);
            mHttpTask.setCacheParameter(mCacheParameter);
            mHttpTask.setSavedInstanceState(savedInstanceState).setUseCache(mUseCache);
            new Thread(mHttpTask).start();
        }
    }

    private static class HttpTask<T> extends BaseFragment.BaseTask {

        private String mUrl;
        private boolean mUseCache;
        private Bundle mSavedInstanceState;
        private BaseFragment.Callback<T> mCallback;
        private BaseFragment.CacheParameter mCacheParameter;
        private BaseFragment.HttpRequestStatistics mStatistics;
        private BaseFragment.BackgroundParser<T> mBackgroundParser;

        public HttpTask(String url, BaseFragment.BackgroundParser<T> backgroundParser, BaseFragment.Callback<T> callback) {
            this.mUrl = url;
            this.mCallback = callback;
            this.mBackgroundParser = backgroundParser;
        }

        public HttpTask setUseCache(boolean useCache) {
            this.mUseCache = useCache;
            return this;
        }

        public HttpTask setSavedInstanceState(Bundle savedInstanceState) {
            this.mSavedInstanceState = savedInstanceState;
            return this;
        }

        public HttpTask setStatistics(BaseFragment.HttpRequestStatistics statistics) {
            this.mStatistics = statistics;
            return this;
        }

        public HttpTask setCacheParameter(BaseFragment.CacheParameter cacheParameter) {
            this.mCacheParameter = cacheParameter;
            return this;
        }

        protected String readCacheData(String url, BaseFragment.CacheParameter parameter) {
            if (parameter == null || TextUtils.isEmpty(parameter.getCachePath())) {
                return null;
            }
            String dir = parameter.getCachePath();
            byte[] bytes = null;
            if (NetworkManager.isAvailable()) {
                bytes = CacheManager.getInstance().get(dir, url);
            } else {
                bytes = CacheManager.getInstance().get(dir, url, true);
            }
            return bytes == null ? null : new String(bytes);
        }

        protected void saveData(String url, String data, BaseFragment.CacheParameter parameter) {
            if (parameter == null || TextUtils.isEmpty(parameter.getCachePath()) || parameter.getCacheMillis() <= 0) {
                return;
            }
            CacheManager.getInstance().put(parameter.getCachePath(), parameter.getCacheMillis(), url, data.getBytes());
        }

        @Override
        public void run() {
            mCallback.onState(BaseFragment.State.LOADING, "HttpTask start loading");
            String cacheData = null;
            if (mUseCache) {
                cacheData = readCacheData(mUrl, mCacheParameter);
            }
            if (TextUtils.isEmpty(cacheData)) {
                if (NetworkManager.isAvailable()) {
                    long start = System.currentTimeMillis();
                    if (mStatistics != null) {
                        mStatistics.onHttpRequestStart(mUrl);
                    }
                    Response response = new HttpSession().get(mUrl);
                    long end = System.currentTimeMillis();
                    if (response != null && response.isSuccessful()) {
                        if (mStatistics != null) {
                            mStatistics.onHttpRequestSuccess(mUrl, end - start, response);
                        }
                        try {
                            byte[] body = response.body().bytes();
                            BaseFragment.Decoder decoder = mCacheParameter.getDecoder();
                            if (decoder != null) {
                                body = decoder.decode(body);
                            }
                            if (body == null) {
                                mCallback.onState(BaseFragment.State.FAILURE, "HttpTask decode error");
                            } else {
                                dealData(new String(body), true);
                            }
                        } catch (IOException e) {
                            Logger.printStackTrace(e);
                            mCallback.onState(BaseFragment.State.FAILURE, e.getMessage());
                        }
                    } else {
                        if (mStatistics != null) {
                            mStatistics.onHttpRequestFailure(mUrl, end - start, response);
                        }
                        String error = "HttpTask Response is null";
                        if (response != null) {
                            error = response.message();
                        }
                        mCallback.onState(BaseFragment.State.FAILURE, error);
                    }
                } else {
                    mCallback.onState(BaseFragment.State.NO_NET, "HttpTask no net");
                }
            } else {
                dealData(cacheData, false);
            }
            die();
        }

        private void dealData(String data, boolean isHttpDownload) {
            T t = null;
            String error = "HttpTask onBackgroundParser return null";
            try {
                t = mBackgroundParser.onBackgroundParser(data);
            } catch (Exception e) {
                Logger.printStackTrace(e);
                error = e.getMessage();
            }
            if (t == null) {
                mCallback.onState(BaseFragment.State.FAILURE, error);
            } else {
                mCallback.onSuccess(mSavedInstanceState, t);
                if (isHttpDownload) {
                    saveData(mUrl, data, mCacheParameter);
                }
            }
        }

    }

    private static class LibraryDecoder implements BaseFragment.Decoder {

        @Override
        public byte[] decode(byte[] bytes) {
            byte[] rawBytes = bytes;
            if (rawBytes == null || rawBytes.length <= 6) {
                Logger.e("calf", "rawBytes is null or length <= 6");
                return null;
            }

            String[] lines = new String(rawBytes).split("\r\n");
            String firstLine = lines[0].trim();
            // String retSig = null;
            if (!firstLine.startsWith("sig=")) {
                if ("TP=none".equalsIgnoreCase(firstLine)) {
                    return firstLine.getBytes();
                }
                Logger.e("calf", "firstLine not starsWith sig");
                return null;
            }
            // retSig = firstLine.substring(4).trim();
            int index = firstLine.getBytes().length + "\r\n".getBytes().length;
            if (index > rawBytes.length) {
                return null;
            }

            byte[] zip = new byte[4];
            zip[0] = rawBytes[0 + index];
            zip[1] = rawBytes[1 + index];
            zip[2] = rawBytes[2 + index];
            zip[3] = rawBytes[3 + index];

            int zipLength = parseInteger(zip, false);

            if (zipLength > rawBytes.length - index) {
                return null;
            }

            zip[0] = rawBytes[4 + index];
            zip[1] = rawBytes[5 + index];
            zip[2] = rawBytes[6 + index];
            zip[3] = rawBytes[7 + index];

            int rawLength = parseInteger(zip, false);
            zip = null;
            Inflater decompresser = new Inflater();
            decompresser.setInput(rawBytes, 8 + index, zipLength);
            byte[] byteResult = null;
            try {
                byteResult = new byte[rawLength];
            } catch (OutOfMemoryError e1) {
                Logger.e("calf", "ys:handleQukuResult|oom");
            }
            try {
                decompresser.inflate(byteResult);
            } catch (Exception e) {
                Logger.e("calf", "ys:handleQukuResult|数据解压失败");
                return null;
            } finally {
                decompresser.end();
            }
            String resultXml = null;
            try {
                resultXml = new String(byteResult).replaceAll("\r", "").replaceAll("\n", "");
            } catch (OutOfMemoryError e) {
                Logger.e("calf", "ys:handleQukuResult|replace oom");
                return null;
            }

            try {
                byteResult = resultXml.getBytes();
            } catch (OutOfMemoryError e) {
            }

            rawBytes = null;
            byte[] validxmlprefix = {'<', '?'};
            index = indexOf(byteResult, 0, validxmlprefix);
            if (index == -1) {
                Logger.e("calf", "ys:handleQukuResult|数据格式错误");
                return null;
            }
            if (index == 0) {
            } else {
                rawLength = byteResult.length - index;
                byte[] byteResultTrimed = null;
                try {
                    byteResultTrimed = new byte[rawLength];
                } catch (OutOfMemoryError e) {
                    Logger.printStackTrace(e);
                }
                System.arraycopy(byteResult, index, byteResultTrimed, 0, rawLength);
                byteResult = byteResultTrimed;
            }
            return byteResult;
        }

        /**
         * 从指定字节数组中查找某子字节数组的第一次出现的位置
         *
         * @param datas 指定数组
         * @param start 起始查询位置
         * @param t     待查询数组
         * @return 如果没找到，返回-1，否则返回索引
         */
        private static int indexOf(byte[] datas, int start, byte[] t) {

            if (datas == null || t == null) {
                throw new NullPointerException("source or target array is null!");
            }

            int index = -1;
            int len = datas.length;
            int tlen = t.length;

            if (start >= len || len - start < tlen) {
                return -1;
            }

            while (start <= len - tlen) {
                int i = 0;
                for (; i < tlen; i++) {
                    if (datas[start + i] != t[i]) {
                        break;
                    }
                }

                if (i == tlen) {
                    index = start;
                    break;
                }

                start++;
            }

            return index;
        }

        /**
         * 从一个字节数组中解析一个整数
         *
         * @param buf       字节数组
         * @param bigEndian 是否大字节序解析
         * @return 相应的整数
         */
        private static int parseInteger(byte[] buf, boolean bigEndian) {
            return (int) parseNumber(buf, 4, bigEndian);
        }

        /**
         * 从一个字节数组中解析一个长整数
         *
         * @param buf       字节数组
         * @param len       整数组成的字节数
         * @param bigEndian 是否大字节序解析
         * @return 相应的长整数
         */
        private static long parseNumber(byte[] buf, int len, boolean bigEndian) {
            if (buf == null || buf.length == 0) {
                throw new IllegalArgumentException("byte array is null or empty!");
            }
            int mlen = Math.min(len, buf.length);
            long r = 0;
            if (bigEndian)
                for (int i = 0; i < mlen; i++) {
                    r <<= 8;
                    r |= (buf[i] & 0x000000ff);
                }
            else
                for (int i = mlen - 1; i >= 0; i--) {
                    r <<= 8;
                    r |= (buf[i] & 0x000000ff);
                }
            return r;
        }

    }

}
