package com.example.okhttpbasisdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 本demo仅限于学习基本的OkHttp使用，
 * 2016年6月14日16:55:11
 */
public class MainActivity extends AppCompatActivity implements OkHttpRequestOkCall {

    OkHttpClient okHttpClient;
    String downTxt = "http://publicobject.com/helloworld.txt";
    String getheade = "https://api.github.com/repos/square/okhttp/issues";
    String testurl = "http://10.1.37.31:8080/Cxf_xzlc_app/loan/getNewProductList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = new OkHttpClient();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
////                    synchronousGET(downTxt);
////                    printHeader(getheade);
////                    parametsPOST(testurl);
//                    okhttpTest(testurl);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        OkHttpNet.setOkHttpRequestOkCall(this);
        testOkhttpNet();

    }


    /**
     * Okhttp get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    private String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().toString();
    }


    private static final MediaType mediatype = MediaType.parse("application/json; charset=utf-8");

    /**
     * Okhtt post请求
     *
     * @param json 请求参数，
     * @param url  地址
     * @return
     * @throws IOException
     */
    private String post(String json, String url) throws IOException {
        RequestBody requestBody = RequestBody.create(mediatype, json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().toString();
    }

    /**
     * 同步GET请求，打印响应头文件，
     *
     * @return
     * @throws IOException
     */
    private void synchronousGET(String url) throws IOException {

        Request request = new Request.Builder().url(url).build();
        /**
         * 响应 execute() 返回一个Response
         */
        Response response = okHttpClient.newCall(request).execute();
        /**
         * get请求是否成功，
         * true 成功
         * false 失败
         * Returns true if the code is in [200..300), which means the request was successfully received,
         * understood, and accepted.
         */
        if (!response.isSuccessful())
            throw new IOException("Unexpected code" + response);
        Headers responseHeader = response.headers();
        for (int i = 0; i < responseHeader.size(); i++) {
            System.out.println(responseHeader.name(i) + ":" + responseHeader.value(i));
        }
        System.out.println(response.body().string());
    }

    /**
     * 异步请求GET打印请求头文件
     *
     * @param url
     */
    private void AsynchornousGET(String url) {
        Request request = new Request.Builder().url(url).build();
        /**
         * enqueue(call)响应，
         */
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /**
                 * 失败 打印失败log
                 */
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code" + response);
                Headers responseHeader = response.headers();
                for (int i = 0; i < responseHeader.size(); i++) {
                    System.out.println(responseHeader.name(i) + ":" + responseHeader.value(i));
                }
            }
        });
    }

    /**
     * 请求提取响应头文件
     *
     * @param url
     */
    public void printHeader(String url) throws IOException {
        Log.i("TAG", "-------");
        Request request = new Request
                .Builder()
                .url(url)
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json").build();
//        Log.i("TAG", "-------");
//        Response response = okHttpClient.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//        System.out.println("Server: " + response.header("Server"));
//        System.out.println("Date: " + response.header("Date"));
//        System.out.println("Vary: " + response.headers("Vary"));
//        Log.i("TAG", "-------");

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("TAG", e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    new IOException("Unexpected code" + response);
                }
                System.out.println("Server: " + response.header("Server"));
                System.out.println("Date: " + response.header("Date"));
                System.out.println("Vary: " + response.headers("Vary"));
            }
        });
    }


    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * OkHttp Post向服务器请求json，返回json
     *
     * @param url
     * @throws IOException
     */
    public void parametsPOST(String url) throws IOException {
        Map<String, String> params = new HashMap<>();//参数集合
        params.put("userId", "");
        params.put("sessionId", "");
        String boday = new Gson().toJson(params);//参数json
        Log.e("TAG", boday);
        RequestBody requestBody = RequestBody.create(JSON, boday);//请求体
        Headers headers = new Headers
                .Builder()
                .add("Accept", "application/json")
                .add("Content-Type", "application/json; charset=UTF-8")
                .build();//请求头
        final Request request = new Request
                .Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();//请求
        //响应
        okHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            Log.e("TAG", "CODE" + response.code());
                        }
                        Log.e("TAG", "CODE" + response.code());
//                        String body = decryptCode();
                        String body = new String(response.body().bytes());//取byte数组转换String，如果直接取String：IllegalStateException: closed
                        Log.i("TAG", "------" + body);
                        TotalBean totalBean = new Gson().fromJson(body, TotalBean.class);
                        String json = decryptCode(totalBean.data);
                        Log.i("TAGGGGG", "数据-----" + json);

                    }
                });

    }

    /**
     * Base64  解密
     *
     * @param ret
     * @return
     */
    public String decryptCode(String ret) {
//        Base64 base642 = new Base64();
        byte[] res = Base64.decode(ret.getBytes(), Base64.DEFAULT);
        String srt2;
        try {
            //UTF-8    GB2312
            srt2 = new String(res, "UTF-8");
            return srt2;
        } catch (UnsupportedEncodingException e) {
            Log.i("baseFragment", "解码失败");
            e.printStackTrace();
            return "";
        }
    }


    /**
     * OkHttpUtils基本使用
     *
     * @param url
     */
    public void okhttpTest(String url) throws IOException {
        Map<String, String> haders = new HashMap<>();
        haders.put("Accept", "application/json");
        haders.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> params = new HashMap<>();
        params.put("userId", "");
        params.put("sessionId", "");
        OkHttpUtils
                .postString()
                .url(url)
                .headers(haders)
                .content(new Gson().toJson(params))
                .mediaType(JSON)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        String json = decryptCode(response);
                        Log.e("TAG", json);

                    }
                });
    }

    /**
     * 简单封装OkHttpUtils，适合小资钱包
     */
    private void testOkhttpNet() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "");
        params.put("sessionId", "");

        OkHttpNet.requestPost(params, testurl);
    }

    @Override
    public void getRequestData(Object o) {

        Log.e("---TAG---", o.toString());
    }
}
