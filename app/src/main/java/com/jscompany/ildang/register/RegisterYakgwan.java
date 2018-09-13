package com.jscompany.ildang.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jscompany.ildang.R;
import com.jscompany.ildang.login.LoginActivity;

public class RegisterYakgwan  extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_yakgwan);

        Button yakgwan_btn = (Button)findViewById(R.id.yakgwan_btn);

        TextView tv_yakgwan_1 =(TextView) findViewById(R.id.tv_yakgwan_1);
        tv_yakgwan_1.setText(R.string.yakgwan_1);

        TextView tv_yakgwan_2 =(TextView) findViewById(R.id.tv_yakgwan_2);
        tv_yakgwan_2.setText(R.string.yakgwan_2);

        TextView tv_yakgwan_3 =(TextView) findViewById(R.id.tv_yakgwan_3);
        tv_yakgwan_3.setText(R.string.yakgwan_3);

        TextView tv_yakgwan_4 =(TextView) findViewById(R.id.tv_yakgwan_4);
        tv_yakgwan_4.setText(R.string.yakgwan_4);

        TextView tv_yakgwan_5 =(TextView) findViewById(R.id.tv_yakgwan_5);
        tv_yakgwan_5.setText(R.string.yakgwan_5);

        TextView tv_yakgwan_6 =(TextView) findViewById(R.id.tv_yakgwan_6);
        tv_yakgwan_6.setText(R.string.yakgwan_6);

        TextView tv_yakgwan_7 =(TextView) findViewById(R.id.tv_yakgwan_7);
        tv_yakgwan_7.setText(R.string.yakgwan_7);

        TextView tv_yakgwan_8 =(TextView) findViewById(R.id.tv_yakgwan_8);
        tv_yakgwan_8.setText(R.string.yakgwan_8);

        TextView tv_yakgwan_9 =(TextView) findViewById(R.id.tv_yakgwan_9);
        tv_yakgwan_9.setText(R.string.yakgwan_9);

        TextView tv_yakgwan_10 =(TextView) findViewById(R.id.tv_yakgwan_10);
        tv_yakgwan_10.setText(R.string.yakgwan_10);


        yakgwan_btn.setOnClickListener(this);





    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yakgwan_btn:
                Intent intent = new Intent(RegisterYakgwan.this , UserChoiceActivity.class);
                startActivity(intent);
                break;
        }
    }
}
