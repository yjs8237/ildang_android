package com.jscompany.ildang.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.PointModel;

import java.util.ArrayList;

public class PointAdapter extends BaseAdapter {

    private ArrayList<PointModel> mItems = new ArrayList<>();

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
            convertView = inflater.inflate(R.layout.point_listview, parent , false);
        }

        TextView tv_point_type = (TextView)convertView.findViewById(R.id.tv_point_type);
        TextView tv_point = (TextView)convertView.findViewById(R.id.tv_point);
        TextView tv_reg_date = (TextView)convertView.findViewById(R.id.tv_reg_date);
        TextView tv_finish_yn = (TextView)convertView.findViewById(R.id.tv_finish_yn);

        PointModel item = (PointModel) getItem(position);

        if(item.getType().equals("1")) {
            tv_point_type.setText("충전");
            tv_point_type.setTextColor(Color.RED);
        } else {
            tv_point_type.setText("환전");
            tv_point_type.setTextColor(Color.BLUE);
        }
        tv_point.setText(CommonUtil.comma(String.valueOf(item.getMoney())));
        tv_reg_date.setText(item.getReg_date());
        if(item.getFinish_yn().equals("Y")) {
            tv_finish_yn.setText("처리완료");
        } else {
            tv_finish_yn.setText("대기중");
        }

        return convertView;
    }

    public void addItem(PointModel item) {
        mItems.add(item);
    }
}
