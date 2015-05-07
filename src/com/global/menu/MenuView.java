package com.global.menu;

import java.util.ArrayList;
import java.util.List;

import com.global.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuView
{

    /**
     * the value of color
     */
    // private int POPUP_WINDOW_BG = 0x72000000;
    private int DEFAULT_COLOR_BG = 0xFFC0C0C0;
    private int GRID_VIEW_COLOR = 0x00000000;

    private final int SPACING = 1;
    private final float SHADOW_HEIGHT = 3.4f;

    private int INVALID = 0;
    private int DEFAULT_COLUMN = 3;
    private int totalCount = 1;
    private int mColumn = INVALID;

    private RelativeLayout convertView;
    private Context mContext;
    private GridView mGridView;
    private List<MenuItem> menuLists;
    private PopupWindow popConver;
    private OnMenuItemClickListener mListener;

    public MenuView(Context mContext)
    {
        super();
        this.mContext = mContext;
    }

    public void add(int textRes, int imageRes)
    {

        if (menuLists == null)
        {
            menuLists = new ArrayList<MenuItem>();
        }
        MenuItem menuItem = new MenuItem();
        menuItem.setMenuImgRes(imageRes);
        menuItem.setMenuTextRes(textRes);

        menuLists.add(menuItem);
    }

    /**
     * 显示菜单
     */
    public boolean show()
    {
        if (mColumn == INVALID)
        {
            if (menuLists == null || menuLists.size() == 0)
            {
                return false;
            }
            else if (menuLists.size() <= 4)
            {
                mColumn = menuLists.size();
            }
            else if (menuLists.size() <= 6)
            {
                mColumn = DEFAULT_COLUMN;
            }
            else
            {
                mColumn = 4;
            }
        }
        else
        {
            mColumn = DEFAULT_COLUMN;
        }

        final Context context = mContext;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float density = metrics.density;

        final LinearLayout layout = initLayout(context);
        if (menuLists == null || menuLists.size() == 0)
        {
            totalCount = 1;
        }
        else
        {
            totalCount = menuLists.size();
        }

        /**
         * 五个菜单2行展示，无法以宫格形式，自定义
         */
        if (totalCount == 5)
        {
            //
            /**
             * 上层阴影
             */
            ImageView img = new ImageView(context);
            img.setImageResource(R.drawable.app_titlebar_bg);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(img, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    (int) (SHADOW_HEIGHT * density)));

            /**
             * 第二行以GridView展现
             */
            GridView fiveGrid = getGridViewForFive(context);
            layout.addView(fiveGrid, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            /**
             * 一二行间水平分隔符
             */
            img = new ImageView(context);
            img.setImageDrawable(new ColorDrawable(DEFAULT_COLOR_BG));
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(img, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, SPACING));

            /**
             * 第二行以宽度平分的Linear展现
             */
            LinearLayout fiveLayout2 = new LinearLayout(context);
            fiveLayout2.setFadingEdgeLength(0);
            fiveLayout2.setOrientation(LinearLayout.HORIZONTAL);
            fiveLayout2.setGravity(Gravity.CENTER);

            for (int i = 2; i < 5; i++)
            {
                convertView = (RelativeLayout) LayoutInflater.from(mContext)
                        .inflate(R.layout.menu_item, null);
                TextView text = (TextView) convertView
                        .findViewById(R.id.id_menu_text);
                ImageView image = (ImageView) convertView
                        .findViewById(R.id.id_menu_img);
                text.setText(menuLists.get(i).getMenuTextRes());
                image.setImageResource(menuLists.get(i).getMenuImgRes());
                convertView.getChildAt(0).setId(i);
                convertView.getChildAt(0).setTag(
                        menuLists.get(i).getMenuTextRes());
                convertView.getChildAt(0).setOnClickListener(
                        new OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                if (mListener != null)
                                {
                                    mListener.onMenuItemClick(null, v,
                                            v.getId(), (Integer) v.getTag());
                                }
                                hide();
                            }
                        });
                convertView.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1));
                fiveLayout2.addView(convertView);
                if (i != 4)
                {
                    img = new ImageView(context);
                    img.setImageDrawable(new ColorDrawable(DEFAULT_COLOR_BG));
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    fiveLayout2.addView(img, new LinearLayout.LayoutParams(
                            SPACING, convertView.getChildAt(0)
                                    .getLayoutParams().height));
                }
            }
            layout.addView(fiveLayout2, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            layout.setOnKeyListener(new OnKeyListener()
            {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if (event.getAction() == KeyEvent.ACTION_UP)
                    {
                        switch (keyCode)
                        {
                            case KeyEvent.KEYCODE_BACK:
                                hide();
                                break;
                            case KeyEvent.KEYCODE_MENU:
                                hide();
                                break;
                            default:
                                break;
                        }
                    }
                    return false;
                }
            });

        }
        else
        {
            if (mGridView == null)
            {
                mGridView = getGridView(context);
            }
            ImageView imgView = new ImageView(context);
            imgView.setImageResource(R.drawable.menu_shadow);
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imgView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    (int) (density * SHADOW_HEIGHT)));
            layout.addView(mGridView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        showPopupConver(layout);

        return true;
    }

    private GridView getGridView(Context context)
    {
        GridView mGridView = new GridView(context);
        MenuAdapter adapter = new MenuAdapter(context, menuLists);
        mGridView.setAdapter(adapter);
        mGridView.setSelector(new ColorDrawable(GRID_VIEW_COLOR));
        mGridView.setVerticalSpacing(0);
        mGridView.setVerticalScrollBarEnabled(false);
        mGridView.setHorizontalScrollBarEnabled(false);
        mGridView.setVerticalSpacing(SPACING);
        mGridView.setHorizontalSpacing(SPACING);
        mGridView.setNumColumns(mColumn);
        mGridView.setGravity(Gravity.CENTER);
        mGridView.setBackgroundColor(DEFAULT_COLOR_BG);

        setGridViewListener(mGridView);
        return mGridView;
    }

    private GridView getGridViewForFive(Context context)
    {
        GridView mGridView = new GridView(context);
        ArrayList<MenuItem> lists = new ArrayList<MenuItem>();
        for (int i = 0; i < 2 && i < menuLists.size(); i++)
        {
            lists.add(menuLists.get(i));
        }
        MenuAdapter adapter = new MenuAdapter(context, lists);
        mGridView.setAdapter(adapter);
        mGridView.setNumColumns(2);
        mGridView.setGravity(Gravity.CENTER);
        mGridView.setVerticalSpacing(0);
        mGridView.setVerticalScrollBarEnabled(false);
        mGridView.setHorizontalScrollBarEnabled(false);
        mGridView.setHorizontalSpacing(SPACING);
        mGridView.setSelector(new ColorDrawable(GRID_VIEW_COLOR));
        mGridView.setBackgroundColor(DEFAULT_COLOR_BG);

        setGridViewListener(mGridView);
        return mGridView;
    }

    private void setGridViewListener(GridView gridView)
    {
        if (gridView.getOnItemClickListener() == null)
        {
            gridView.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                        int position, long id)
                {
                    if (null != mListener)
                    {
                        mListener.onMenuItemClick(parent, v, position,
                                menuLists.get(position).getMenuTextRes());
                    }
                    hide();
                }
            });
            gridView.setOnKeyListener(new OnKeyListener()
            {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    switch (event.getAction())
                    {
                        case KeyEvent.ACTION_DOWN:
                            if (keyCode == KeyEvent.KEYCODE_BACK)
                            {
                                hide();
                            }
                            break;
                        case KeyEvent.ACTION_UP:
                            if (keyCode == KeyEvent.KEYCODE_MENU)
                            {
                                hide();
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 设置菜单项被选中监听器
     * 
     * @param listener
     */
    public void setOnMenuItemClickListenr(OnMenuItemClickListener listener)
    {
        mListener = listener;
    }

    private LinearLayout initLayout(Context context)
    {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setFadingEdgeLength(0);
        layout.setGravity(Gravity.CENTER);

        layout.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    hide();
                }
                return false;
            }
        });
        return layout;
    }

    /**
	 *
	 */
    public boolean hide()
    {
        if (popConver != null && popConver.isShowing())
        {
            popConver.dismiss();
            popConver = null;
            if (mListener != null)
            {
                dismiss();
            }
        }
        return false;
    }

    private void dismiss()
    {
        if (menuLists != null)
        {
            menuLists.clear();
        }
        mGridView = null;
    }

    /**
     * 
     * @param cxt
     */
    private void showPopupConver(LinearLayout layout)
    {
        if (popConver == null)
        {
            popConver = new PopupWindow(layout,
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // popConver.setAnimationStyle(R.style.menu_in_out);
        popConver.setFocusable(true);
        popConver.setTouchable(true);
        popConver.setOutsideTouchable(true);
        popConver.setBackgroundDrawable(null);// popConver.setBackgroundDrawable(new
                                              // ColorDrawable(POPUP_WINDOW_BG));
        popConver.showAtLocation(layout, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
    }
}
