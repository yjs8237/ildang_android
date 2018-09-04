package com.jscompany.ildang.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jscompany.ildang.R;

import java.util.ArrayList;

public class IlgamAdapter extends BaseAdapter {

    private ArrayList<IlgamListItem> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public IlgamListItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public void addItem(IlgamListItem item) {
        mItems.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adv_ilgam_list_view, parent , false);
        }

        TextView tv_seq = (TextView)convertView.findViewById(R.id.tv_seq);
        TextView tv_title = (TextView)convertView.findViewById(R.id.tv_title);
        TextView tv_reg_date = (TextView)convertView.findViewById(R.id.tv_reg_date);

        IlgamListItem item = (IlgamListItem) getItem(position);

        tv_seq.setText(item.getSeq());
        tv_title.setText(item.getTitle());
        tv_reg_date.setText(item.getReg_date());

        return convertView;
    }
}
