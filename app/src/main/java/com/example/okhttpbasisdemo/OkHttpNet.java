package com.example.okhttpbasisdemo;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 * Created by 贾帅帅 on 2016/6/14.
 */
public class OkHttpNet extends OkHttpUtils {
    //请求json 媒体类型
    public static final MediaType DEFAULT_UTF_8 = MediaType.parse("application/json; charset=utf-8");
    /**
     * 头文件集合
     */
    private static Map<String, String> haders = new HashMap<>();
    /**
     * 创建一个 postStringBuilder，添加固定的头文件和json媒体类型
     */
    private static PostStringBuilder builder = postString()
            .headers(haders)
            .mediaType(DEFAULT_UTF_8);
    private static OkHttpRequestOkCall mOkHttpRequestOkCall;

    public OkHttpNet(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    /**
     * 静态模块添加头文件
     */ {
        haders.put("Accept", "application/json");
        haders.put("Content-Type", "application/json; charset=UTF-8");

    }

    /**
     * post 请求json
     *
     * @param params
     * @param url
     */
    public static void requestPost(Map<String, String> params, String url) {
/**
 * 请求并相应
 */
        builder.content(new Gson().toJson(params))
                .url(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                TotalBean totalBean = new Gson().fromJson(response, TotalBean.class);
                decJson(totalBean);
            }
        });

    }

    /**
     * 对json串解密
     *
     * @param totalBean
     */
    private static void decJson(TotalBean totalBean) {
        byte[] res = Base64.decode(totalBean.data.getBytes(), Base64.DEFAULT);
        String srt2;
        try {
            //UTF-8    GB2312
            srt2 = new String(res, "UTF-8");
            errortest(srt2);
        } catch (UnsupportedEncodingException e) {
            Log.i("baseFragment", "解码失败");
            e.printStackTrace();
        }
    }

    /**
     * 对请求回来的数据进行错误处理
     *
     * @param srt2
     */
    private static void errortest(String srt2) {
        DataBean dataBean = new Gson().fromJson(srt2, DataBean.class);

        if (dataBean.data.code.equals("0000")) {
            //没问题将则回调
            mOkHttpRequestOkCall.getRequestData(srt2);
        } else {
            OkHttpBasisDemoApplication.ToastUtils(dataBean.data.msg);
            Log.e("--error--", "CODE:" + dataBean.data.code + "MSG:" + dataBean.data.msg);
        }
    }

    public static void setOkHttpRequestOkCall(OkHttpRequestOkCall okHttpRequestOkCall) {
        mOkHttpRequestOkCall = okHttpRequestOkCall;
    }



}
