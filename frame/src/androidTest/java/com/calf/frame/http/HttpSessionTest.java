package com.calf.frame.http;

import android.test.InstrumentationTestCase;

import com.calf.frame.log.Logger;

import org.junit.Test;

import okhttp3.Response;

/**
 * Created by JinYi Liu on 16-7-22.
 */
public class HttpSessionTest extends InstrumentationTestCase {

    private HttpSession mSession;
    @Override
    protected void setUp() throws Exception {
        Logger.changeLog(Logger.Type.DEBUG);
        mSession = new HttpSession();
    }

    @Test
    public void testGet() throws Exception {
        String url = "http://60.28.195.121:8180/ksingnew/index.htm";
        Response response = mSession.get(url);
        Logger.e("xiaoniu",":::::::::::>" + response.body().string());
    }

    @Test
    public void testAsyncGet() throws Exception {

    }

    @Test
    public void testPost() throws Exception {

    }

    @Test
    public void testAsyncPost() throws Exception {

    }

    @Test
    public void testDownload() throws Exception {

    }

    @Test
    public void testAsyncDownload() throws Exception {

    }

    @Test
    public void testUpload() throws Exception {

    }

    @Test
    public void testAsyncUpload() throws Exception {

    }
}