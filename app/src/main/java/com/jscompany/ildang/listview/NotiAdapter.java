package com.jscompany.ildang.listview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscompany.ildang.R;
import com.jscompany.ildang.model.NotificationModel;

import java.util.ArrayList;

public class NotiAdapter extends BaseAdapter {

    private ArrayList<NotificationModel> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public NotificationModel getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.noti_listview, parent , false);
        }

        TextView tv_noti_seq = (TextView)convertView.findViewById(R.id.tv_noti_seq);
        TextView tv_title = (TextView)convertView.findViewById(R.id.tv_title);
//        TextView tv_reg_date = (TextView)convertView.findViewById(R.id.tv_reg_date);

        NotificationModel item = (NotificationModel) getItem(position);

        tv_noti_seq.setText(String.valueOf(item.getNoti_seq()));
        tv_title.setText(item.getTitle());
//        tv_reg_date.setText(item.getReg_date());

        return convertView;
    }

    public void addItem(NotificationModel item) {
        mItems.add(item);
    }
}
