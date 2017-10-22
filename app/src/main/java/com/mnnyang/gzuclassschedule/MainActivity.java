package com.mnnyang.gzuclassschedule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    //CookieJar是用于保存Cookie的
    class LocalCookieJar implements CookieJar {
        List<Cookie> cookies;

        @Override
        public List<Cookie> loadForRequest(HttpUrl arg0) {
            if (cookies != null)
                return cookies;
            return new ArrayList<Cookie>();
        }

        @Override
        public void saveFromResponse(HttpUrl arg0, List<Cookie> cookies) {
            System.out.println("----------------saveFromResponse");
            for (Cookie cookie : cookies) {
                System.out.println(cookie);
            }
            this.cookies = cookies;
        }

    }


    private void init() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
                .followSslRedirects(false)
                .cookieJar(new LocalCookieJar())   //为OkHttp设置自动携带Cookie的功能
                .addInterceptor(new LoggerInterceptor("TAG"))
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(getBaseContext()))) //要在内存Cookie前
                .cookieJar(new CookieJarImpl(new MemoryCookieStore()))//内存Cookie
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void init2() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
                .followSslRedirects(false)
                .cookieJar(new LocalCookieJar())   //为OkHttp设置自动携带Cookie的功能
                .addInterceptor(new LoggerInterceptor("TAG"))
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public void start(View view) {
        OkHttpUtils.get().url("http://210.40.2.253:8888/default2.aspx")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "错误:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "成功:" + response);
                ((TextView) findViewById(R.id.tv)).setText(response);
                loadR();
            }
        });
    }

    private void loadR() {
        OkHttpUtils.get().url("http://210.40.2.253:8888/CheckCode.aspx").build().execute(
                new FileCallBack(getCacheDir().getAbsolutePath(), "kk.jpg") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Bitmap bitmap = BitmapFactory.decodeFile(
                                getCacheDir().getAbsolutePath() + "/kk.jpg");

                        ((ImageView) findViewById(R.id.iv)).setImageBitmap(bitmap);
                    }
                });
    }


    public void login(View view) {
        String yzm = ((EditText) findViewById(R.id.yzm)).getText().toString();

        OkHttpUtils.post().url("http://210.40.2.253:8888/default2.aspx")
                .addParams("__VIEWSTATE", "dDwtNTE2MjI4MTQ7Oz4I55DQ6KPcVdzTLmjGjlJPRWgYUQ==")
                .addParams("txtUserName", "1500170110")
                .addParams("Textbox1", "")
                .addParams("Textbox2", "ajtajt123") //密码
                .addParams("txtSecretCode", yzm) //验证码
                .addParams("RadioButtonList1", "学生")
                .addParams("Button1", "")
                .addParams("lbLanguage", "")
                .addParams("hidPdrs	", "")
                .addParams("hidsc", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, response);
                ((TextView) findViewById(R.id.tv)).setText(response);
            }
        });
    }

    public void loadK(View view) {
//        init2();
        OkHttpUtils.get().url("http://210.40.2.253:8888/xskbcx.aspx?xh=1500170110")
                .addHeader("Referer", "http://210.40.2.253:8888/xskbcx.aspx?xh=1500170110")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        List<String> headers = call.request().headers("Cookie");


//                        System.out.println("-----------loadk 错误"+headers.size());
//                        for (String header : headers) {
//                            System.out.println(header);
//                        }


                        System.out.println("head: " + call.request().headers().toString());

                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        System.out.println("loadk--------succceed");
                        System.out.println("---------->" + response);
                    }
                });
    }
}
