package com.esp.socialintegrationdemo.frndlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.esp.socialintegrationdemo.R;

import java.util.ArrayList;

/**
 * Created by admin on 25/4/16.
 */
public class UserAdapter extends ArrayAdapter<userBean> {

    private Context context;
    private int res;
    private ArrayList<userBean> friendList;

    public UserAdapter(Context context, int resource, ArrayList<userBean> friendlist) {
        super(context, resource, friendlist);
        this.context = context;
        this.res = resource;
        this.friendList = friendlist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(res, null);
            viewHolder = new ViewHolder();
            viewHolder.imgAvtar = (ImageView) convertView.findViewById(R.id.imgAvtar);
            viewHolder.txtname = (TextView) convertView.findViewById(R.id.txtname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtname.setText(friendList.get(position).getName());

        Glide.with(context).load(friendList.get(position).getFb_avtar()).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewHolder.imgAvtar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                viewHolder.imgAvtar.setImageDrawable(circularBitmapDrawable);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        ImageView imgAvtar;
        TextView txtname;
    }
}
