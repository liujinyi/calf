package com.calf.frame.http;

import android.text.TextUtils;

import com.calf.frame.log.Logger;
import com.calf.frame.tool.Assert;

import java.io.IOException;
import java.net.Proxy;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JinYi Liu on 16-7-22.
 */
public final class HttpSession {

    static final String TAG = "HttpSession";

    private OkHttpClient mClient = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor())
            .build();

    public Response get(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            Logger.printStackTrace(e);
        }
        return response;
    }

    public void asyncGet(String url, Callback callback) {
        Assert.classAssert(callback != null, "HttpSession [asyncGet] callback is null");
        Assert.classAssert(!TextUtils.isEmpty(url), "HttpSession [asyncGet] url is empty");
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//                Headers responseHeaders = response.headers();
//                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
//                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                }
//
//                System.out.println(response.body().string());
            }
        });
    }


    public Response download(String url, String path) {
        return null;
    }

    public void asyncDownload(String url, String path, Callback callback) {

    }

//    public Response upload(String url) {
//        return null;
//    }
//
//    public void asyncUpload(String url, Callback callback) {
//
//    }
//
//    public Response proxy(Proxy proxy) {
//        return null;
//    }
//
//    public Response post(String url) {
//
//        return null;
//    }
//
//    public void asyncPost(String url, Callback callback) {
//
//    }
}
