package com.termux.xinhao.web.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gaurav.avnc.model.ServerProfile;
import com.gaurav.avnc.ui.vnc.VncActivity;

public class ZeroVncReceiver extends BroadcastReceiver {
    private static final String TAG = "TermuxStyleReceiver";

    // 定义 Action 常量
    public static final String ACTION_RELOAD_VNC = "com.termux.app.reload_vnc";
    public static final String EXTRA_STYLE_DATA = "vnc_data";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_RELOAD_VNC.equals(intent.getAction())) {

            // 获取传递的额外数据
            String styleData = intent.getStringExtra(EXTRA_STYLE_DATA);
            if (styleData != null) {
                Log.d(TAG, "Receive data: " + styleData);
                // 解析 VNC 数据 (格式: vnc::127.0.0.1::5900::admin::admin)
                String[] split = styleData.split("::");
                if (split.length >= 5 && "vnc".equals(split[0])) {
                    Log.i("TAG", "fixTermuxActivityBroadcastReceiverIntentxxxxx extraReloadStyle: " + styleData);
                    try {
                        String ip = split[1];
                        String port = split[2];
                        String username = split[3];
                        String password = split[4];

                        if (!"none".equals(username)) {

                        }
                        if (!"none".equals(password)) {

                        }
                        ServerProfile serverProfile = new ServerProfile();
                        serverProfile.setHost("127.0.0.1");
                        serverProfile.setPort(5906);
                        Intent intent1 = new Intent(context, VncActivity.class);
                        intent1.putExtra("com.gaurav.avnc.server_profile", serverProfile);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                Log.i(TAG, "onReceive: vnc data error!");
            }
        }
    }
}
