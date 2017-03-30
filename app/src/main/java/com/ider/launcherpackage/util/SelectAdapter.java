package com.ider.launcherpackage.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.launcher.ItemEntry;
import com.ider.launcherpackage.launcher.PackageHolder;

import java.util.List;

/**
 * Created by Eric on 2017/3/23.
 */

public class SelectAdapter extends BaseAdapter {

    private static final String TAG = "SelectAdapter";

    private Context mContext;
    private int layoutId = R.layout.app_select_item;
    private List<PackageHolder> data,apps;

    public SelectAdapter(Context mContext, List<PackageHolder> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public SelectAdapter(Context mContext, int layoutId, List<PackageHolder> data,List<PackageHolder> apps) {
        this(mContext, data);
        this.layoutId = layoutId;
        this.apps= apps;
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
        SelectAdapter.ViewHolder holder;
        if(view == null) {
            holder = new SelectAdapter.ViewHolder();
            view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
            holder.image = (ImageView) view.findViewById(R.id.app_item_image);
            holder.image2 = (ImageView) view.findViewById(R.id.app_item_image_select);
            holder.text = (TextView) view.findViewById(R.id.app_item_text);
            view.setTag(holder);
        } else {
            holder = (SelectAdapter.ViewHolder) view.getTag();
        }
        String pkgname = data.get(i).getPackageName();
        PackageHolder packageHolder= data.get(i);
        if (apps.contains(packageHolder)){
            holder.image2.setVisibility(View.VISIBLE);
        }else {
            holder.image2.setVisibility(View.GONE);
        }
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
        private ImageView image2;
        private TextView text;
    }
}
