package com.mean.meanchat.android.net;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MessageWebSocketClient extends WebSocketListener {
    public static final String TAG = "MessageWebSocketClient";

    public MessageWebSocketClient() {
        super();
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d(TAG, "onOpen");
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(TAG, "onMessage: "+text);
        super.onMessage(webSocket, text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.d(TAG, "onClosed");
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d(TAG, "onFailure");
        super.onFailure(webSocket, t, response);
    }
}
