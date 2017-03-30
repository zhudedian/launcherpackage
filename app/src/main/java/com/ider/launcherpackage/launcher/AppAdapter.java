package com.ider.launcherpackage.launcher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launcherpackage.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ider-eric on 2016/12/26.
 */

public class AppAdapter extends BaseAdapter {

    private static final String TAG = "AppAdapter";
    
    private Context mContext;
    private int layoutId = R.layout.app_select_item;
    private List<PackageHolder> data;

    public AppAdapter(Context mContext, List<PackageHolder> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public AppAdapter(Context mContext, int layoutId, List<PackageHolder> data) {
        this(mContext, data);
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
            holder.image = (ImageView) view.findViewById(R.id.app_item_image);
            holder.text = (TextView) view.findViewById(R.id.app_item_text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String pkgname = data.get(i).getPackageName();
        holder.image.setImageDrawable(getPackageDrawable(pkgname));
        holder.text.setText(getPackageText(pkgname));

        return view;
    }

    private Drawable getPackageDrawable(String pkgname) {
        if(pkgname.equals("add")) {
            return mContext.getResources().getDrawable(R.mipmap.add_item_white);
        } else {
            return ItemEntry.loadImage(mContext, pkgname);
        }
    }

    private String getPackageText(String pkgname) {
        if(pkgname.equals("add")) {
            return mContext.getResources().getString(R.string.shortcut_default_title);
        } else {
            return ItemEntry.loadLabel(mContext, pkgname);
        }
    }

    class ViewHolder {
        private ImageView image;
        private TextView text;
    }
}
