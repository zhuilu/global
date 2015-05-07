package com.global.menu;

import java.util.List;

import com.global.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter
{

    private Context mContext;
    private List<MenuItem> menuLists;
    private LayoutInflater mLayoutInflater;

    public MenuAdapter(Context cxt, List<MenuItem> menuLists)
    {
        super();
        this.mContext = cxt;
        this.menuLists = menuLists;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return menuLists != null ? menuLists.size() : 0;
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return menuLists != null ? menuLists.get(position) : null;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        MenuHolder menuHolder;
        if (convertView == null)
        {
            menuHolder = new MenuHolder();
            convertView = mLayoutInflater.inflate(R.layout.menu_item, null);
            menuHolder.imgView = (ImageView) convertView
                    .findViewById(R.id.id_menu_img);
            menuHolder.textView = (TextView) convertView
                    .findViewById(R.id.id_menu_text);
            convertView.setTag(menuHolder);
        }
        else
        {
            menuHolder = (MenuHolder) convertView.getTag();
        }
        MenuItem item = (MenuItem) this.getItem(position);
        menuHolder.imgView.setImageResource(item.getMenuImgRes());
        menuHolder.textView.setText(item.getMenuTextRes());

        return convertView;
    }

}
