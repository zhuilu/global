package com.global.menu;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class MenuHelper
{

    /**
     * 构造菜单：获取菜单图片和文本
     * 
     * @param cxt
     * @param menuInfo
     * @return
     */
	public static MenuView constructMenu(Context cxt, List<MenuItem> menuInfo)
    {
        MenuView menuView = new MenuView(cxt);
        if (menuInfo == null)
        {
            return menuView;
        }
        for (int i = 0; i < menuInfo.size(); i++)
        {
            menuView.add(menuInfo.get(i).getMenuTextRes(), menuInfo.get(i)
                    .getMenuImgRes());
        }
        return menuView;
    }

    /**
     * 设置菜单图片和文本
     * 
     * @param menuRes
     * @return
     */
    public static ArrayList<MenuItem> processMenu(int[][] menuRes)
    {
        ArrayList<MenuItem> menuInfo = new ArrayList<MenuItem>();
        if (menuRes == null)
        {
            return menuInfo;
        }
        for (int i = 0; i < menuRes.length; i++)
        {
            MenuItem menuItem = new MenuItem();
            menuItem.setMenuTextRes(menuRes[i][0]);
            menuItem.setMenuImgRes(menuRes[i][1]);
            menuInfo.add(menuItem);
        }
        return menuInfo;
    }
}
