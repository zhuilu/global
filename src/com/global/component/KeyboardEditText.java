package com.global.component;

import com.global.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * 可定制输入软键盘的输入框
 * 
 * @author daishulin@163.com
 *
 */
public class KeyboardEditText extends EditText {
    /**
     *  输入框容器
     */
    private KeyboardContainer mContainer;
    /**
     * 软键盘
     */
    private InputKeyboardView mKeyboardView;
    /**
     * 软键盘视图ID
     */
    private int mKeyboardId;//= R.id.keyboard;
    /**
     * 是否可输入
     */
    private boolean mIsTextEditor = true;
    /**
     * 键盘是否被取消
     */
    private boolean mCanceled = false;

    public KeyboardEditText(Context context) {
        super(context);
    }

    public KeyboardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs, defStyle);
    }

    public KeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs, 0);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        //        int[] styled = ResourceUtil.getResourceDeclareStyleableIntArray(context, "KeyboardEditText");
        TypedArray a = context.obtainStyledAttributes(attrs,
            R.styleable.framework_keyboardEditText, defStyle, 0);

        mKeyboardId = a.getResourceId(
            R.styleable.framework_keyboardEditText_framework_keyboardEdittext_keyboardId, 0);
        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mContainer == null) {
            init();
        }
    }

    private void init() {
        Activity activity = (Activity) getContext();
        View view = activity.findViewById(12);
        if (view == null || !(view instanceof KeyboardContainer)) {
            throw new RuntimeException("custom keyborad must use KeyboardContainer.");
        }
        mContainer = (KeyboardContainer) view;

        view = activity.findViewById(mKeyboardId);
        if (view == null || !(view instanceof InputKeyboardView)) {
            throw new RuntimeException("custom keyborad must set valid KeyboardId.");
        }
        mKeyboardView = (InputKeyboardView) view;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (mKeyboardView == null) {
            return;
        }
        if (focused) {
            if (!mKeyboardView.focusWith(this)) {
                mKeyboardView.focusIn(this);
            }
        } else {
            if (mKeyboardView.focusWith(this)) {
                mKeyboardView.focusOut();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mCanceled)
            return;
        if (mContainer == null || !mContainer.isWindowFocusInput())
            return;
        if (mKeyboardView == null || !isFocused()) {
            return;
        }
        if (hasWindowFocus) {
            if (!mKeyboardView.focusWith(this)) {
                mKeyboardView.focusIn(this);
            }
        } else {
            if (mKeyboardView.focusWith(this)) {
                mKeyboardView.focusOut();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isFocusable() && mKeyboardView != null) {
            if (!mKeyboardView.focusWith(this)) {
                mKeyboardView.focusIn(this);
            }
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mKeyboardView != null && mKeyboardView.focusWith(this)) {
                mKeyboardView.focusOut();
                mCanceled = true;
                return true;
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled && mKeyboardView != null && mKeyboardView.focusWith(this)) {
            mKeyboardView.focusOut();
        }
        super.setEnabled(enabled);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return mIsTextEditor;
    }

    @Override
    protected boolean getDefaultEditable() {
        return false;
    }

    /**
     * 获取软键盘视图
     * 
     * @return 软键盘视图
     */
    public InputKeyboardView getKeyboardView() {
        return mKeyboardView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCanceled = false;
        if (mKeyboardView != null && !mKeyboardView.focusWith(this)) {
            mKeyboardView.focusIn(this);
            return true;
        }
        mIsTextEditor = false;
        boolean ret = super.onTouchEvent(event);
        mIsTextEditor = true;
        return ret;
    }
}
