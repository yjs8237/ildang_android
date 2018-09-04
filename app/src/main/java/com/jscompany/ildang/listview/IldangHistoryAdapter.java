package com.jscompany.ildang.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jscompany.ildang.R;
import com.jscompany.ildang.model.IldangModel;

import java.util.ArrayList;

public class IldangHistoryAdapter extends BaseAdapter {

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
            convertView = inflater.inflate(R.layout.ildanghist_list_view, parent , false);
        }

        TextView tv_job_seq = (TextView)convertView.findViewById(R.id.tv_job_seq);
        TextView tv_job_str = (TextView)convertView.findViewById(R.id.tv_job_str);
        TextView tv_nick_name = (TextView)convertView.findViewById(R.id.tv_nick_name);
        TextView tv_work_date = (TextView)convertView.findViewById(R.id.tv_work_date);
        TextView tv_loc_str = (TextView)convertView.findViewById(R.id.tv_loc_str);
        TextView tv_remain_time = (TextView)convertView.findViewById(R.id.tv_remain_time);

        IldangModel item = (IldangModel) getItem(position);

        tv_job_seq.setText(String.valueOf(item.getJob_seq()));
        tv_nick_name.setText("오더주 닉네임 : " + item.getUser_nick());
        tv_work_date.setText(item.getWork_date());
        tv_job_str.setText("업무 : " + item.getJob_type_str());
        tv_loc_str.setText("지역 : " + item.getLoc_gu_str() + "-" + item.getLoc_dong_str());
        tv_remain_time.setText("연락가능 남은시간 : " + item.getRemain_time() + "분");
        return convertView;
    }
}
