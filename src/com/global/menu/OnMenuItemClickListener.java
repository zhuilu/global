package com.global.menu;

import android.view.View;
import android.widget.AdapterView;

public interface OnMenuItemClickListener
{

    /**
     * 菜单项被点击时调用
     * 
     * @param parent
     * @param view
     * @param position
     *            当前点击的位置
     * @param tag
     *            当前点击菜单的字符串资源Id
     */
    public void onMenuItemClick(AdapterView<?> parent, View view, int position,
            int tag);
}
