package com.global.component;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 可定制的软键盘
 * 
 * @author daishulin@163.com
 *
 */
public abstract class InputKeyboardView extends KeyboardView {
    /**
     * 需要輸入的輸入框
     */
    protected EditText mEditText;

    public InputKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InputKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        setKeyboard(getInputKeyboard());
        setOnKeyboardActionListener(getKeyboardActionListener());
    }

    /**
     * 绑定目标输入框
     * 
     * @param editText 目标输入框
     */
    public void focusIn(EditText editText) {
        mEditText = editText;
        show();
    }

    /**
     * 是否绑定目标输入框
     * 
     * @param editText 目标输入框
     * @return 是否绑定
     */
    public boolean focusWith(EditText editText) {
        return mEditText == editText;
    }

    /**
     * 隐藏软键盘
     */
    public void focusOut() {
        mEditText = null;
        hide();
    }

    /**
     * 获取Keyboard
     * 
     * @return 软键盘
     */
    protected abstract Keyboard getInputKeyboard();

    /**
     * 获取软键盘的OnKeyboardActionListener
     * 
     * @return 软键盘Listener
     */
    protected abstract OnKeyboardActionListener getKeyboardActionListener();

    /**
     * 显示软键盘
     */
    protected abstract void show();

    /**
     * 影藏软键盘
     */
    protected abstract void hide();
}
