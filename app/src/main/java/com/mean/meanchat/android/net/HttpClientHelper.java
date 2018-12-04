package com.mean.meanchat.android.net;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.mean.meanchat.android.bean.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClientHelper {
    private static final String TAG = "HttpClientHelper";
    private static HttpClientHelper instance;
    private OnLoginListener onLoginListener;
    private OnLogoutListener onLogoutListener;
    private OkHttpClient okHttpClient;
    private String serverUrl = "http://10.0.2.2:8080";
    public static synchronized HttpClientHelper getInstance(){
        if(instance==null){
            instance = new HttpClientHelper();
        }
        return instance;
    }

    private HttpClientHelper() {
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance().getCookieJar())
                .build();
    }

    public void login(@NonNull OnLoginListener listener,final String username,final String password){
        setOnLoginListener(listener);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilder.add("username", username);
                formBodyBuilder.add("password", password);
                String url = serverUrl + "/login";
                Log.d(TAG, "handleMessage: url:" + url);
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBodyBuilder.build())
                        .build();
                okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        onLoginListener.onLoginNoResponse("no response from server");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();
                        Log.d(TAG, "run: " + responseBody);
                        JSONObject jsonObject = JSONObject.parseObject(responseBody);
                        Boolean isSuccess = (Boolean) jsonObject.get("isSuccess");
                        Looper.prepare(); //onLoginListener的方法会更新UI
                        if (isSuccess == null) {
                            onLoginListener.onLoginUnknownResponse("can't resolve server response");
                        } else if (!isSuccess) {
                            String message = (String) jsonObject.get("message");
                            if (message == null) {
                                onLoginListener.onLoginFailure("server message unknown");
                            } else {
                                onLoginListener.onLoginFailure(message);
                            }
                        } else {
                            onLoginListener.onLoginSuccess("login success");
                        }
                        Looper.loop();
                    }
                });
            }
        }).start();
    }

    public void logout(@NonNull OnLogoutListener listener){
        setOnLogoutListener(listener);
        //TODO
    }

    public void setOnLoginListener(HttpClientHelper.OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
    }

    public interface OnLoginListener {
        void onLoginSuccess(String message);
        void onLoginNoResponse(String message);
        void onLoginUnknownResponse(String message);
        void onLoginFailure(String message);
    }

    public interface OnLogoutListener {
        void onLogoutSuccess(String message);
        void onLogoutFailure(String message);
        void onLogoutNoResponse(String message);
        void onLogoutUnknownResponse(String message);
    }
}
