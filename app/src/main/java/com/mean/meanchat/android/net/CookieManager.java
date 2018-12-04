package com.mean.meanchat.android.net;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class CookieManager {
    private static CookieManager instance;
    public static synchronized CookieManager getInstance(){
        if(instance==null){
            instance = new CookieManager();
        }
        return instance;
    }
    private HashMap<String,List<Cookie>> cookieStore;
    private CookieJar cookieJar;

    private CookieManager() {
        cookieStore =  new HashMap<>();
        cookieJar = new CookieJar();
    }

    public HashMap<String, List<Cookie>> getCookieStore() {
        return cookieStore;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    private class CookieJar implements okhttp3.CookieJar{
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(),cookies);
        }

        @NonNull
        @Override
        public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }
}
