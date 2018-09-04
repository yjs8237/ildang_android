package com.jscompany.ildang.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jscompany.ildang.R;
import com.jscompany.ildang.model.AdverModel;

import java.util.ArrayList;

public class MyAdverAdapter extends BaseAdapter {

    private ArrayList<AdverModel> mItems = new ArrayList<>();

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

    public void addItem(AdverModel model) {
        mItems.add(model);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_adver_list_view, parent , false);
        }

        //TextView tv_job_seq = (TextView)convertView.findViewById(R.id.tv_job_seq);


        TextView tv_ad_type = (TextView)convertView.findViewById(R.id.tv_ad_type);
        TextView tv_ad_seq = (TextView)convertView.findViewById(R.id.tv_ad_seq);
        TextView tv_ad_title = (TextView)convertView.findViewById(R.id.tv_ad_title);
        TextView tv_ad_reg_date = (TextView)convertView.findViewById(R.id.tv_ad_reg_date);

        AdverModel item = (AdverModel) getItem(position);

        //tv_job_seq.setText(String.valueOf(item.getJob_seq()));
        tv_ad_type.setText("구분 : " + item.getType_str());
        tv_ad_seq.setText(String.valueOf(item.getAd_seq()));
        tv_ad_title.setText("제목 : " + item.getTitle());
        tv_ad_reg_date.setText(item.getReg_date());

        return convertView;
    }
}
