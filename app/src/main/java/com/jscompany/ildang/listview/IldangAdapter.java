package com.jscompany.ildang.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.IldangModel;

import java.util.ArrayList;

public class IldangAdapter  extends BaseAdapter {

    private ArrayList<IldangModel> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public IldangModel getItem(int position) {
        return mItems.get(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void addItem(IldangModel item) {
        mItems.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ildang_list_view, parent , false);
        }

        //TextView tv_job_seq = (TextView)convertView.findViewById(R.id.tv_job_seq);
        TextView tv_nick_name = (TextView)convertView.findViewById(R.id.tv_nick_name);
        TextView tv_work_date = (TextView)convertView.findViewById(R.id.tv_work_date);
        TextView tv_work_pay = (TextView)convertView.findViewById(R.id.tv_work_pay);
        TextView tv_user_able_job = (TextView)convertView.findViewById(R.id.tv_user_able_job);

        LinearLayout ildang_list_linear =(LinearLayout) convertView.findViewById(R.id.ildang_list_linear);


        IldangModel item = (IldangModel) getItem(position);
        if(item.getFinish_yn() != null && !item.getFinish_yn().equals("N")) {
            // 이미 오더주가 매칭한 일당일 경우
            ildang_list_linear.setBackgroundColor(Color.GRAY);
        } else {
            ildang_list_linear.setBackgroundColor(Color.WHITE);
        }
        //tv_job_seq.setText(String.valueOf(item.getJob_seq()));
        tv_nick_name.setText(item.getUser_nick());
        tv_work_date.setText(item.getWork_date());
        tv_work_pay.setText("일당단가 : " + CommonUtil.comma(String.valueOf(item.getWork_pay())));
        tv_user_able_job.setText("가능작업 : " + item.getUser_able_job());
        return convertView;
    }
}
