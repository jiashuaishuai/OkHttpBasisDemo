package com.example.okhttpbasisdemo;

import android.app.Application;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 贾帅帅 on 2016/6/14.
 */
public class OkHttpBasisDemoApplication extends Application {
    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
//        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//https 认证证书
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager)//设置https 具体的证书
                .addInterceptor(new LoggerInterceptor("OkHttpBasisDemoTAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        //双向认证
//        HttpsUtils.getSslSocketFactory(
//                证书的inputstream,
//                本地证书的inputstream,
//                本地证书的密码)
        /**
         * 第二种配置方法
         */
//        CookieJarImpl cookieJar  = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        OkHttpUtils okHttpUtils = OkHttpUtils.initClient(okHttpClient);
        application = this;
    }

    public static void ToastUtils(String mess) {
        Toast.makeText(application, mess, Toast.LENGTH_SHORT).show();
    }
}
