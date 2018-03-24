package org.weex.plugin.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WeexModule(name = "Device")
public class DeviceModule extends WXModule {
    public static final int REQUEST_CODE = 111;
    private int frameWidth = 220;//扫描框的宽度，单位dp
    private int frameHeight = 220;//扫描框的高度，单位dp
    private int frameCornerColor;//扫描框4角颜色rgb值
    private boolean frameHide = false;//是否隐藏扫描框，默认显示
    private String tipText = "将二维码/条形码放入框内，即可自动扫描";//提示文字
    private int tipTextColor;//提示文字颜色rgb值，默认白色
    private boolean laserMoveFullScreen = false;//扫描线全屏移动，默认在扫描框内移动
    private String botton1="扫码";
    private String botton2="1";
    private String botton3="1";
    private String botton4="1";
    private String botton5="1";
    private JSCallback onShowCallBack;
    //sync ret example
    //TODO: Auto-generated method example
    @JSMethod
    public String syncRet(String param) {
        Log.e("syncRet: ",param);
//        show();
        return param;
    }

    //async ret example
    //TODO: Auto-generated method example
    @JSMethod
    public void asyncRet(String param, JSCallback callback) {
        callback.invoke(param);
    }

    @JSMethod
    public void show(HashMap<String,Object> options, JSCallback callback){
        onShowCallBack=callback;
        frameWidth = (int) options.get("frameWidth");
        frameWidth = (int) options.get("frameHeight");
        frameHide = (boolean) options.get("frameHide");
        tipText = (String) options.get("tipText");
        laserMoveFullScreen = (boolean) options.get("laserMoveFullScreen");
        JSONArray buttons = (JSONArray) options.get("buttons");

        ArrayList<HashMap<String,Object>> buttonns = (ArrayList<HashMap<String,Object>>) options.get("buttons");
        for (int i = 0; i < buttonns.size(); i++) {
            HashMap<String, Object> stringObjectHashMap = buttonns.get(i);

        }
        botton1 = (String) options.get("botton1");
        botton2 = (String) options.get("botton2");
        botton3 = (String) options.get("botton3");
        botton4 = (String) options.get("botton4");
        botton5 = (String) options.get("botton5");
        ZXingLibrary.initDisplayOpinion(mWXSDKInstance.getContext());
        Intent intent = new Intent(mWXSDKInstance.getContext(), CaptureMyActivity.class);
        intent.putExtra("frameWidth", this.frameWidth);
        intent.putExtra("frameHeight",frameHeight);
        intent.putExtra("frameCornerColor",frameCornerColor);
        intent.putExtra("frameHide", this.frameHide);
        intent.putExtra("tipText", this.tipText);
        intent.putExtra("laserMoveFullScreen",laserMoveFullScreen);
        intent.putExtra("botton1", this.botton1);
        intent.putExtra("botton2",botton2);
        intent.putExtra("botton3",botton3);
        intent.putExtra("botton4",botton4);
        intent.putExtra("botton5",botton5);
        Activity activity=(Activity)mWXSDKInstance.getUIContext();
        activity.startActivityForResult(intent,REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    onShowCallBack.invoke(result);
                    Toast.makeText(mWXSDKInstance.getContext(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(mWXSDKInstance.getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}