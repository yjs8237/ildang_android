package com.jscompany.ildang.guide;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jscompany.ildang.R;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.GuideModel;

import java.util.ArrayList;

public class GuideAdapter extends BaseAdapter {

    private ArrayList<GuideModel> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public GuideModel getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addItem(GuideModel model) {
        mItems.add(model);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.guide_list_view, parent , false);
        }

        //TextView tv_job_seq = (TextView)convertView.findViewById(R.id.tv_job_seq);
        TextView tv_guide_type = (TextView)convertView.findViewById(R.id.tv_guide_type);
        TextView tb_guide_title = (TextView)convertView.findViewById(R.id.tb_guide_title);
        TextView tv_guide_detail = (TextView)convertView.findViewById(R.id.tv_guide_detail);

        GuideModel item = (GuideModel) getItem(position);

        //tv_job_seq.setText(String.valueOf(item.getJob_seq()));
        tv_guide_type.setText(String.valueOf(item.getType()));
        if(item.getType().equals("기술자")){
            tv_guide_type.setTextColor(Color.BLUE);
        } else if(item.getType().equals("오더주")) {
            tv_guide_type.setTextColor(Color.RED);
        } else {
            tv_guide_type.setTextColor(Color.BLACK);
        }

        tb_guide_title.setText(item.getTitle());
        tv_guide_detail.setText(item.getContent());

        return convertView;
    }
}
