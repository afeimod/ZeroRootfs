package com.termux.xinhao.web.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.gaurav.avnc.model.ServerProfile;
import com.gaurav.avnc.ui.vnc.VncActivity;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AndroidTextSocketService extends Service {
    private static final String TAG = "TextSocketService";
    private static final int PORT = 19951;

    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private boolean isRunning = false;

    // 广播 Action
    public static final String ACTION_MESSAGE_RECEIVED = "com.xinhao.web.services.MESSAGE_RECEIVED";
    public static final String ACTION_SERVER_STATUS = "com.xinhao.web.services.SERVER_STATUS";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_CLIENT = "client";
    public static final String EXTRA_TIMESTAMP = "timestamp";
    public static final String EXTRA_STATUS = "status";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务创建");
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "服务启动命令");
        if (!isRunning) {
            startSocketServer();
        }
        return START_STICKY;
    }

    private void startSocketServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT);
                isRunning = true;

                Log.i(TAG, "✅ 文本Socket监听器启动成功，端口: " + PORT);
                sendStatusBroadcast("SERVER_STARTED", "监听端口 " + PORT + " 已启动");

                // 监听循环
                while (isRunning && !serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    handleClientConnection(clientSocket);
                }

            } catch (IOException e) {
                if (isRunning) { // 只有运行时的错误才报告
                    Log.e(TAG, "❌ 监听器错误: " + e.getMessage());
                    sendStatusBroadcast("SERVER_ERROR", "监听错误: " + e.getMessage());
                }
            }
        }).start();
    }

    private void handleClientConnection(Socket clientSocket) {
        threadPool.execute(() -> {
            String clientInfo = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
            Log.i(TAG, "🔗 客户端连接: " + clientInfo);
            sendStatusBroadcast("CLIENT_CONNECTED", "客户端连接: " + clientInfo);

            try {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));

                String message;
                while ((message = reader.readLine()) != null) {
                    // 记录接收到的消息
                    String timestamp = getCurrentTime();
                    Log.i(TAG, "📨 收到消息 [" + timestamp + "] from " + clientInfo + ": " + message);
                    jumpActivity(message);
                    // 发送广播通知消息接收
                    sendMessageBroadcast(message, clientInfo, timestamp);

                    // 如果是退出命令，关闭连接
                    if ("exit".equalsIgnoreCase(message.trim()) || "quit".equalsIgnoreCase(message.trim())) {
                        break;
                    }
                }

            } catch (IOException e) {
                Log.e(TAG, "📖 读取客户端数据错误: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "🔌 关闭客户端连接错误: " + e.getMessage());
                }
                Log.i(TAG, "❌ 客户端断开: " + clientInfo);
                sendStatusBroadcast("CLIENT_DISCONNECTED", "客户端断开: " + clientInfo);
            }
        });
    }

    private void sendMessageBroadcast(String message, String client, String timestamp) {
        Intent intent = new Intent(ACTION_MESSAGE_RECEIVED);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_CLIENT, client);
        intent.putExtra(EXTRA_TIMESTAMP, timestamp);
        sendBroadcast(intent);
    }

    private void jumpActivity(String msg) {
        String[] split = msg.split("::");
        if (split.length >= 5 && "vnc".equals(split[0])) {
            Log.i("TAG", "fixTermuxActivityBroadcastReceiverIntentxxxxx extraReloadStyle: " + msg);
            try {
                ServerProfile serverProfile = new ServerProfile();

                String ip = split[1];
                String port = split[2];
                String username = split[3];
                String password = split[4];
                serverProfile.setHost(ip);
                serverProfile.setPort(Integer.parseInt(port));
                if (!"none".equals(username)) {
                    serverProfile.setUsername(username);
                }
                if (!"none".equals(password)) {
                    serverProfile.setPassword(password);
                }
                Intent intent1 = new Intent(getApplication(), VncActivity.class);
                intent1.putExtra("com.gaurav.avnc.server_profile", serverProfile);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(intent1);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendStatusBroadcast(String status, String message) {
        Intent intent = new Intent(ACTION_SERVER_STATUS);
        intent.putExtra(EXTRA_STATUS, status);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TIMESTAMP, getCurrentTime());
        sendBroadcast(intent);
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "服务销毁");
        stopSocketServer();
    }

    private void stopSocketServer() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (threadPool != null) {
                threadPool.shutdown();
            }
            Log.i(TAG, "🛑 Socket监听器已停止");
            sendStatusBroadcast("SERVER_STOPPED", "监听器已停止");
        } catch (IOException e) {
            Log.e(TAG, "停止监听器时出错: " + e.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
