package com.jscompany.ildang.userlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.UserAdapter;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class UserListActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private ProgressDialog progressDialog;

    public static int TIME_OUT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obt_user_list);

        listView = (ListView)findViewById(R.id.user_list_view);

        doWork();

        /* 버튼 리스너 */
//        Button obtain_btn = (Button) findViewById(R.id.obtain_reg_btn);
//        Button order_btn = (Button) findViewById(R.id.order_reg_btn);
//        obtain_btn.setOnClickListener(this);
//        order_btn.setOnClickListener(this);
        /* ---------------------- */
    }

    private void doWork() {
        progressDialog = ProgressDialog.show(this, CONST.progress_title, CONST.progress_body);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listDataSetting();
                mHandler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }

    private void listDataSetting() {
        UserAdapter userAdapter = new UserAdapter();

        for (int i=0; i<10; i++) {
            userAdapter.addItem(ContextCompat.getDrawable(getApplicationContext() , R.drawable.people_icon) , "name" + i , "contents" + i);
        }
        listView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {

    }


    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            progressDialog.dismiss(); // ProgressDialog를 종료
            if (msg.what == TIME_OUT) { // 타임아웃이 발생하면

            }
        }
    };


}
