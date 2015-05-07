package com.global.menu;

import com.global.R;

import android.app.Activity;

public class MenuAction
{

    private Activity caller;

    public MenuAction(Activity cxt)
    {
        super();
        this.caller = cxt;
    }

    public void menuExecute(int resId)
    {
        switch (resId)
        {
        // case R.string.menu_settings:
            case 1:
                // HjUtil.launchActivity(caller, HjSystemSetActivity.class);
                break;

            default:
                break;
        }
    }

}
