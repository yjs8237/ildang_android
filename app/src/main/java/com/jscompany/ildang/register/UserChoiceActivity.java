package com.jscompany.ildang.register;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;

public class UserChoiceActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_choice);


        /* 버튼 리스너 */
        Button obtain_btn = (Button) findViewById(R.id.obtain_reg_btn);
        Button order_btn = (Button) findViewById(R.id.order_reg_btn);
        obtain_btn.setOnClickListener(this);
        order_btn.setOnClickListener(this);
        /* ---------------------- */
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.obtain_reg_btn :
                CommonUtil.showToast(getApplicationContext() , "기술자");
                intent = new Intent(UserChoiceActivity.this , RegisterActivity.class);
                intent.putExtra("user_type" , "1");
                startActivity(intent);
                break;
            case R.id.order_reg_btn :
                CommonUtil.showToast(getApplicationContext() , "오더주");
                intent = new Intent(UserChoiceActivity.this , RegisterActivity.class);
                intent.putExtra("user_type" , "2");
                startActivity(intent);
                break;
        }
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext() , message , Toast.LENGTH_SHORT).show();
    }
}
