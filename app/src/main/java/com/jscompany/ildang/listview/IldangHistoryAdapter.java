package com.jscompany.ildang.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    public void finishItem(int position) {
        mItems.get(position).setFinish_yn("E");
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
        TextView tv_com_name = (TextView)convertView.findViewById(R.id.tv_com_name);
        TextView tv_work_date = (TextView)convertView.findViewById(R.id.tv_work_date);
        TextView tv_loc_str = (TextView)convertView.findViewById(R.id.tv_loc_str);
        TextView tv_remain_time = (TextView)convertView.findViewById(R.id.tv_remain_time);
        TextView tv_finish_yn = (TextView)convertView.findViewById(R.id.tv_finish_yn);
        ImageView imageView3 = (ImageView)convertView.findViewById(R.id.imageView3);
        LinearLayout linear_01 = (LinearLayout)convertView.findViewById(R.id.linear_01);

        IldangModel item = (IldangModel) getItem(position);

        tv_job_seq.setText(String.valueOf(item.getJob_seq()));
        tv_com_name.setText("업체명 : " + item.getCom_name());
        tv_work_date.setText(item.getWork_date());
        tv_job_str.setText("업무 : " + item.getJob_type_str());
        tv_loc_str.setText("지역 : " + item.getLoc_gu_str() + "-" + item.getLoc_dong_str());
        tv_remain_time.setText("연락가능 남은시간 : " + item.getRemain_time() + "분");

        if(item.getFinish_yn().equals("Y")) {
            // 오더주 선택
            tv_finish_yn.setText("연락가능");
            tv_finish_yn.setTextColor(Color.RED);
            linear_01.setBackgroundColor(Color.WHITE);
            imageView3.setVisibility(View.VISIBLE);
        } else if(item.getFinish_yn().equals("E")) {
            // 오더주 연락 완료
            tv_finish_yn.setText("매칭완료");
            tv_finish_yn.setTextColor(Color.GRAY);
            imageView3.setVisibility(View.GONE);
            linear_01.setBackgroundColor(Color.LTGRAY);
        } else if(item.getFinish_yn().equals("C")) {
            // 시간초과
            tv_finish_yn.setText("시간초과");
            tv_finish_yn.setTextColor(Color.GRAY);
            imageView3.setVisibility(View.GONE);
            linear_01.setBackgroundColor(Color.LTGRAY);
        } else {
            // 대기중
            tv_finish_yn.setText("대기중");
            tv_finish_yn.setTextColor(Color.BLUE);
            imageView3.setVisibility(View.GONE);
            linear_01.setBackgroundColor(Color.WHITE);
            imageView3.setVisibility(View.VISIBLE);
            imageView3.setImageResource(R.drawable.cancle_ildang);
//            tv_com_name.setVisibility(View.INVISIBLE);
//            tv_remain_time.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
