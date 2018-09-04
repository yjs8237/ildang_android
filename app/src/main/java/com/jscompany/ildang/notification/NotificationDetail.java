package com.jscompany.ildang.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.R;

public class NotificationDetail extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti_detail);

        Intent intent = getIntent();
        String noti_seq = intent.getExtras().getString("noti_seq");
        String title = intent.getExtras().getString("title");
        String content = intent.getExtras().getString("content");
        String reg_date = intent.getExtras().getString("reg_date");


        ImageButton back_img_btn =(ImageButton) findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        TextView tv_noti_title = (TextView)findViewById(R.id.tv_noti_title);
        tv_noti_title.setText(title);

        TextView tv_reg_date = (TextView)findViewById(R.id.tv_reg_date);
        tv_reg_date.setText(reg_date);

        TextView tv_noti_content = (TextView)findViewById(R.id.tv_noti_content);
        tv_noti_content.setText(content);


        /* 버튼 리스너 */
//        Button obtain_btn = (Button) findViewById(R.id.obtain_reg_btn);
//        Button order_btn = (Button) findViewById(R.id.order_reg_btn);
//        obtain_btn.setOnClickListener(this);
//        order_btn.setOnClickListener(this);
        /* ---------------------- */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img_btn :
                backActivity();
                break;
        }
    }

    private void backActivity() {
        super.onBackPressed();
    }

    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }

}
