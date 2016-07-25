package com.calf.frame.http;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * Created by JinYi Liu on 16-7-22.
 */
public interface ProgressCallback extends Callback {

    public void onStart(Call cal);

    public void onCancel(Call call);

    public void onProgress(double current, double total);

}
