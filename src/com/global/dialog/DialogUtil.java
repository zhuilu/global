package com.global.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

public class DialogUtil
{
    public static View editTextDialog(Context context, int resId,
            String message, String title,
            DialogInterface.OnClickListener sureListen,
            DialogInterface.OnClickListener cancelListen)
    {
        LayoutInflater vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = vi.inflate(resId, null, false);
        Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(android.R.drawable.ic_dialog_info).setView(layout)
                .setPositiveButton("确定", sureListen)
                .setNegativeButton("取消", cancelListen);
        if (title != null)
        {
            dialog.setTitle(title);
        }
        if (message != null)
        {

            dialog.setMessage(message);
        }

        dialog.show();
        return layout;
    }
}
