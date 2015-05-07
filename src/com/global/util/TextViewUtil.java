package com.global.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class TextViewUtil
{
    /**
     * 将TextView 后面增加红色*
     * */
    public static void formatTextView(TextView title)
    {
        Spannable wordToSpan = new SpannableString(title.getText().toString());
        wordToSpan.setSpan(
                new ForegroundColorSpan(title.getCurrentTextColor()), 0,
                wordToSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(wordToSpan);
        Spannable WordToSpan1 = new SpannableString("*");
        WordToSpan1.setSpan(new ForegroundColorSpan(Color.RED), 0,
                WordToSpan1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.append(WordToSpan1);
    }
}
