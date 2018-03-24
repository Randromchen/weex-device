package org.weex.plugin.device;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by lannister on 2017/6/19.
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
        init();
    }

    public void setContent(@NonNull String content) {
        ((TextView) findViewById(R.id.content)).setText(content);
    }

    private void init(){
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(true);
        setCancelable(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 0.9f;
        getWindow().setAttributes(attributes);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (this.isShowing())
                    this.dismiss();
                break;
        }
        return true;
    }

}
