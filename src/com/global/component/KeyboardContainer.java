package com.global.component;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * 需要自定义软键盘KeyboardEditText的容器
 * 
 * @author daishulin@163.com
 *
 */
public class KeyboardContainer extends RelativeLayout {
    private boolean mWindowFocusInput;
    
    public KeyboardContainer(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public KeyboardContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public KeyboardContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Activity activity = (Activity) getContext();
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        mWindowFocusInput=(params.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        if (mWindowFocusInput) {
            // 隐藏软键盘
            activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    /**
     * 是否初始需要弹出键盘
     * 
     * @return 是否初始需要弹出键盘
     */
    public boolean isWindowFocusInput() {
        return mWindowFocusInput;
    }
    
    
}
