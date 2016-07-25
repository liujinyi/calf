package com.calf.frame.http;

import com.calf.frame.log.Logger;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JinYi Liu on 16-7-22.
 */
public final class HttpSession {

    private OkHttpClient mClient = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .build();

    public Response get(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void asyncGet(String url, Callback callback) {
        Interceptor interceptor;
    }

    public Response proxy(Proxy proxy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.proxy(proxy);
        return null;
    }

    public Response post(String url) {

        return null;
    }

    public void asyncPost(String url, Callback callback) {

    }

    public Response download(String url, String path) {
        return null;
    }

    public void asyncDownload(String url, String path, Callback callback) {

    }

    public Response upload(String url) {
        return null;
    }

    public void asyncUpload(String url, Callback callback) {

    }


}
