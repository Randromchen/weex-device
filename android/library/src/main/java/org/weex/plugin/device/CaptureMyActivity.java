package org.weex.plugin.device;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerOptions;
import com.mylhyl.zxing.scanner.ScannerView;
import com.mylhyl.zxing.scanner.camera.open.CameraFacing;
import com.mylhyl.zxing.scanner.common.Scanner;
import com.mylhyl.zxing.scanner.decode.QRDecode;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.Collection;

import static com.mylhyl.zxing.scanner.ScannerOptions.LaserStyle.RES_LINE;

public class CaptureMyActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE = 112;
    private ScannerView mScannerView;
    private Result mLastResult;
    private ImageView picture_left_back;
    private TextView picture_title;
    private TextView picture_right;
    private LinearLayout linear1;
    private ImageView im_button1;
    private TextView tv_content1;
    private LinearLayout linear2;
    private ImageView im_button2;
    private TextView tv_content2;
    private LinearLayout linear3;
    private ImageView im_button3;
    private TextView tv_content3;
    private LinearLayout linear4;
    private ImageView im_button4;
    private TextView tv_content4;
    private LinearLayout linear5;
    private ImageView im_button5;
    private TextView tv_content5;



    private ImageView im_flash_light;
    public LoadingDialog mProgressDialog;
    public enum LaserStyle {
        /**
         * 颜色线值样式
         */
        COLOR_LINE
        /**
         * 资源文件线样式
         */
        , RES_LINE
        /**
         * 资源文件网格样式
         */
        , RES_GRID
    }

    private ScannerOptions.LaserStyle laserStyle = ScannerOptions.LaserStyle.COLOR_LINE;
    private int laserLineColor;//扫描线颜色rgb值
    private int laserLineResId;//扫描线资源文件
    private int laserLineHeight = 2;//扫描线高度，网络样式无效，单位dp
    private int laserLineMoveSpeed = 6;//扫描线移动间距，默认每毫秒移动6px，单位px
    private boolean laserMoveFullScreen = false;//扫描线全屏移动，默认在扫描框内移动
    private int frameWidth = 220;//扫描框的宽度，单位dp
    private int frameHeight = 220;//扫描框的高度，单位dp
    private int frameCornerColor;//扫描框4角颜色rgb值
    private int frameCornerLength = 15;//扫描框4角长度，单位dp 默认15
    private int frameCornerWidth = 2;//扫描框4角宽度，单位dp 默认2
    private boolean frameCornerInside = true;//扫描框4角是否在框内，默认框外
    private boolean frameCornerHide = false;//是否隐藏扫描框4角，默认显示
    private int frameTopMargin = 110;//扫描框与顶部间距，单位dp，默认居中
    private boolean frameHide = false;//是否隐藏扫描框，默认显示
    private boolean viewfinderHide;//是否隐藏整个取景视图，包括文字，默认显示
    private String tipText = "将二维码/条形码放入框内，即可自动扫描";//提示文字
    private int tipTextColor;//提示文字颜色rgb值，默认白色
    private int tipTextSize = 14;//提交文字大小，单位sp 默认15
    private boolean tipTextToFrameTop = false;//是否在扫描框上方，false默认下方
    private int tipTextToFrameMargin = 20;//离扫描框间距，单位dp 默认20
    private int mediaResId;//扫描成功音频资源文件
    private Collection<BarcodeFormat> decodeFormats;//解码类型，默认解全部
    private boolean createQrThumbnail;//生成扫描结果缩略图，默认不生成，也就是扫描成功后的第三个参数
    private boolean showQrThumbnail;//是否显示扫描结果缩略图在扫描界面
    private CameraFacing cameraFacing = CameraFacing.BACK;//启动摄像头位置，默认后置
    private boolean scanFullScreen = true;//是否全屏扫描识别，默认扫描框内识别
    private boolean scanInvert = false;//是否扫描反色二维码（true用于黑底白码）
    private double cameraZoomRatio = 2;//相机变焦比率
    private ScannerOptions.ViewfinderCallback viewfinderCallback;
    private int frameOutsideColor = Scanner.color.VIEWFINDER_MASK;//扫描框以外区域半透明黑色
    public static boolean isOpen = false;
    private String botton1="扫码";
    private String botton2="1";
    private String botton3="1";
    private String botton4="1";
    private String botton5="1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZXingLibrary.initDisplayOpinion(this);
        setContentView(R.layout.activity_capture_my);
        initView();
        init();
    }

    private void initView() {
        laserLineColor = getResources().getColor(R.color.scan_corner_color);
        frameCornerColor = getResources().getColor(R.color.scan_corner_color);
        tipTextColor = getResources().getColor(R.color.color_53);
        frameWidth = getIntent().getIntExtra("frameWidth", 220);
        frameHeight = getIntent().getIntExtra("frameHeight", 220);
        frameCornerColor = getIntent().getIntExtra("frameCornerColor", frameCornerColor);
        tipTextColor = getIntent().getIntExtra("tipTextColor", tipTextColor);
        frameHide = getIntent().getBooleanExtra("frameHide", false);
        laserMoveFullScreen = getIntent().getBooleanExtra("laserMoveFullScreen", false);
        tipText = getIntent().getStringExtra("tipText");
        botton1 = getIntent().getStringExtra("botton1");
        botton2 = getIntent().getStringExtra("botton2");
        botton3 = getIntent().getStringExtra("botton3");
        botton4 = getIntent().getStringExtra("botton4");
        botton5 = getIntent().getStringExtra("botton5");


        mScannerView = (ScannerView) findViewById(R.id.scanner_view);
        ;
        picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        im_flash_light = (ImageView) findViewById(R.id.im_flash_light);

        picture_title = (TextView) findViewById(R.id.picture_title);
        picture_right = (TextView) findViewById(R.id.picture_right);

        linear1 = (LinearLayout) findViewById(R.id.linear1);
        im_button1 = (ImageView) findViewById(R.id.im_button1);
        tv_content1 = (TextView) findViewById(R.id.tv_content1);
        if(botton1.equals("1")){
            linear1.setVisibility(View.GONE);
        }else {
            linear1.setVisibility(View.VISIBLE);
            tv_content1.setText(botton1);
        }


        linear2 = (LinearLayout) findViewById(R.id.linear2);
        im_button2 = (ImageView) findViewById(R.id.im_button2);
        tv_content2 = (TextView) findViewById(R.id.tv_content2);
        if(botton2.equals("1")){
            linear2.setVisibility(View.GONE);
        }else {
            linear2.setVisibility(View.VISIBLE);
            tv_content2.setText(botton2);
        }


        linear3 = (LinearLayout) findViewById(R.id.linear3);
        im_button3 = (ImageView) findViewById(R.id.im_button3);
        tv_content3 = (TextView) findViewById(R.id.tv_content3);
        if(botton3.equals("1")){
            linear3.setVisibility(View.GONE);
        }else {
            linear3.setVisibility(View.VISIBLE);
            tv_content3.setText(botton3);
        }


        linear4 = (LinearLayout) findViewById(R.id.linear4);
        linear4.setVisibility(View.GONE);
        im_button4 = (ImageView) findViewById(R.id.im_button4);
        tv_content4 = (TextView) findViewById(R.id.tv_content4);
        if(botton4.equals("1")){
            linear4.setVisibility(View.GONE);
        }else {
            linear4.setVisibility(View.VISIBLE);
            tv_content3.setText(botton4);
        }

        linear5 = (LinearLayout) findViewById(R.id.linear5);
        linear5.setVisibility(View.GONE);
        im_button5 = (ImageView) findViewById(R.id.im_button5);
        tv_content5 = (TextView) findViewById(R.id.tv_content5);
        if(botton5.equals("1")){
            linear5.setVisibility(View.GONE);
        }else {
            linear5.setVisibility(View.VISIBLE);
            tv_content5.setText(botton5);
        }

        /**
         * 返回
         */
        picture_left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 选择图片
         */
        picture_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        /**
         * 第一个按钮
         */
        im_flash_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    mScannerView.toggleLight(true);
                    im_flash_light.setImageResource(R.mipmap.flashlight_on);
                    isOpen = true;
                } else {
                    mScannerView.toggleLight(false);
                    im_flash_light.setImageResource(R.mipmap.flashlight_off);
                    isOpen = false;
                }
            }
        });

        /**
         * 第一个按钮
         */
        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /**
         * 第2个按钮
         */
        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        /**
         * 第3个按钮
         */
        linear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /**
         * 第4个按钮
         */
        linear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        /**
         * 第5个按钮
         */
        linear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showInputMethod();
                    }
                },100);
            }
        });
    }

    private void showInputMethod() {
        //自动弹出键盘
        InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //强制隐藏Android输入法窗口
        // inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
    }

    private void init() {



        mScannerView.setOnScannerCompletionListener(new OnScannerCompletionListener() {
            @Override
            public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
                Toast.makeText(CaptureMyActivity.this, rawResult.getText(), Toast.LENGTH_SHORT).show();
                vibrate();
                mScannerView.restartPreviewAfterDelay(0);
                finish();
            }
        });
        ScannerOptions.Builder builder = new ScannerOptions.Builder();
        builder.setFrameSize(this.frameWidth, frameHeight)//设置扫描框大小
                .setLaserStyle(RES_LINE, R.drawable.scan_image)//扫描线样式
//                .setLaserLineColor(0xff06c1ae)//设置扫描线颜色值<br>
//                .setLaserLineHeight(18)//设置扫描线高度<br>
                .setLaserMoveSpeed(laserLineMoveSpeed)//设置扫描框线移动间距
                .setScanFullScreen(scanFullScreen)//是否全屏扫描，默认非全屏扫描<br>true=全屏扫描，则隐藏扫描框
                .setFrameHide(frameHide)//是否隐藏扫描框，默认不隐藏 true隐藏
                .setLaserMoveFullScreen(laserMoveFullScreen)//扫描线是否全屏移动
                .setFrameCornerColor(frameCornerColor)//设置扫描框4角颜色值
                .setFrameCornerLength(frameCornerLength)//设置扫描框4角长度
                .setFrameCornerWidth(frameCornerWidth)//设置扫描框4角宽度
                .setFrameCornerInside(frameCornerInside)//设置扫描框4角是否在框内，false默认框外
                .setFrameCornerHide(frameCornerHide)//是否隐藏扫描框4角 true隐藏
                .setFrameTopMargin(frameTopMargin)//设置扫描框与屏幕顶部距离
                .setViewfinderHide(false)//设置隐藏取景视图包括文字，false默认不隐藏
                .setTipText(tipText)//设置文字
                .setTipTextColor(tipTextColor)//设置文字颜色
                .setFrameOutsideColor(Scanner.color.VIEWFINDER_MASK)//设置扫描框以外区域颜色值
                .setTipTextSize(tipTextSize)//设置文字大小
                .setTipTextToFrameMargin(tipTextToFrameMargin)//设置文字与扫描框间距
                .setTipTextToFrameTop(tipTextToFrameTop)//设置文字是否在扫描框上方，默认下方 true=上方，false=下方
//                .setMediaResId()//设置扫描成功的音频
//                .setScanMode(BarcodeFormat.QR_CODE)//设置扫描解码类型
//                .setShowQrThumbnail(true)//是否显示扫描结果缩略图在扫描界面，默认不显示  setCreateQrThumbnail(true)才有效
//                .setFrameOutsideColor(getResources().getColor(R.color.color_orange))
                .setCameraFacing(cameraFacing)//设置扫描摄像头，默认后置 BACK后  FRONT前
                .setScanInvert(scanInvert)//是否扫描反色二维码（黑底白码）
                .setCameraZoomRatio(cameraZoomRatio);//设置相机变焦比率
        mScannerView.setScannerOptions(builder.build());
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * 选择系统图片并解析
         */

        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                showProgressDialog("识别中...");
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, final String result) {
                            tv_content5.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CaptureMyActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                                    vibrate();
                                    dismissProgressDialog();
                                    finish();
                                }
                            },2000);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            tv_content5.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CaptureMyActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                                    dismissProgressDialog();
                                }
                            },2000);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Override
    protected void onResume() {
        mScannerView.onResume();
        resetStatusView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        super.onPause();
    }

    private void resetStatusView() {
        mLastResult = null;
    }


    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    public void showProgressDialog(String content) {
        if (isProgressDialogShowing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new LoadingDialog(CaptureMyActivity.this);
        }
        mProgressDialog.setContent(content);
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (isProgressDialogShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public boolean isProgressDialogShowing() {
        return null != mProgressDialog && mProgressDialog.isShowing();
    }
}