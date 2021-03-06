package com.mean.meanchat.android;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.mean.meanchat.android.bean.User;
import com.mean.meanchat.android.net.HttpClientHelper;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements HttpClientHelper.OnLoginListener{
    public static final String TAG = "LoginActivity";
    private MeanChatApplication mApplication = (MeanChatApplication)getApplication();

    private static final String serverUrl = "http://10.0.2.2:8080";

    private TextView et_username;
    private TextView et_password;
    private CheckBox cb_remember;
    private Button btn_login;
    private TextView tv_register;
    private TextView tv_forget;
    private List<View> loginViewList;
    private ProgressBar pb_logining;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        pb_logining = findViewById(R.id.pb_logining);
        pb_logining.setVisibility(View.INVISIBLE);
        cb_remember = findViewById(R.id.cb_remember);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);
        tv_forget = findViewById(R.id.tv_forget);
        loginViewList = new ArrayList<>();
        loginViewList.add(cb_remember);
        loginViewList.add(btn_login);
        loginViewList.add(tv_register);
        loginViewList.add(tv_forget);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if(username.isEmpty() || password.isEmpty()){
                    showToast("username or password can't be empty");
                    return;
                }
                et_username.setEnabled(false);
                et_password.setEnabled(false);
                updateUI$Logging();
                HttpClientHelper.getInstance().login(LoginActivity.this,username,password);
            }
        });

    }

    private void updateUI$Logging(){
        et_username.setEnabled(false);
        et_password.setEnabled(false);
        for (View v:loginViewList){
            v.setVisibility(View.INVISIBLE);
        }
        pb_logining.setVisibility(View.VISIBLE);
    }

    private void updateUI$AllowLogin(){
        et_username.setEnabled(true);
        et_password.setEnabled(true);
        for (View v:loginViewList){
            v.setVisibility(View.VISIBLE);
        }
        pb_logining.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onLoginSuccess(String message) {
        showToast(message);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginNoResponse(String message) {
        showToast("login failure\n"+message);
        updateUI$AllowLogin();
    }

    @Override
    public void onLoginUnknownResponse(String message) {
        showToast("login failure\n"+message);
        updateUI$AllowLogin();
    }

    @Override
    public void onLoginFailure(String message) {
        showToast("login failure\n"+message);
        updateUI$AllowLogin();
    }

    private void showToast(String msg){
        Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}

