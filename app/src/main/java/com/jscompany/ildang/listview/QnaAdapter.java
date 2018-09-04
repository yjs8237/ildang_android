package com.jscompany.ildang.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jscompany.ildang.R;
import com.jscompany.ildang.model.QnaModel;

import java.util.ArrayList;

public class QnaAdapter extends BaseAdapter {

    private ArrayList<QnaModel> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public QnaModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.qna_listview, parent , false);
        }

        TextView tv_qna_result_yn = (TextView)convertView.findViewById(R.id.tv_qna_result_yn);
        TextView tv_qna_title = (TextView)convertView.findViewById(R.id.tv_qna_title);
//        TextView tv_reg_date = (TextView)convertView.findViewById(R.id.tv_reg_date);

        QnaModel item = (QnaModel) getItem(position);

        String result_str = "답변대기";
        tv_qna_result_yn.setTextColor(Color.RED);
        if(item.getFinish_yn().equals("Y")) {
            tv_qna_result_yn.setTextColor(Color.BLUE);
            result_str = "답변완료";
        }
        tv_qna_result_yn.setText(result_str);
        tv_qna_title.setText(item.getTitle());
//        tv_reg_date.setText(item.getReg_date());

        return convertView;
    }

    public void addItem(QnaModel item) {
        mItems.add(item);
    }
}
