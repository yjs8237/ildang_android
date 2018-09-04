package com.jscompany.ildang.listview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jscompany.ildang.R;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    private ArrayList<UserListItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
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
            convertView = inflater.inflate(R.layout.obt_user_listview, parent , false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView2);
        TextView textView_1 = (TextView)convertView.findViewById(R.id.tv_name);
        TextView textView_2 = (TextView)convertView.findViewById(R.id.tv_contents);
        UserListItem item = (UserListItem) getItem(position);

        imageView.setImageDrawable(item.getIcon());
        textView_1.setText(item.getCell_no());
        textView_2.setText(item.getUser_nick());

        return convertView;
    }

    public void addItem(Drawable img, String name, String contents) {
        UserListItem item = new UserListItem();

        item.setIcon(img);
        item.setCell_no(name);
        item.setUser_nick(contents);

        mItems.add(item);

    }
}
