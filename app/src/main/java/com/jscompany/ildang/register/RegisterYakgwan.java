package com.jscompany.ildang.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jscompany.ildang.R;
import com.jscompany.ildang.login.LoginActivity;

public class RegisterYakgwan  extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_yakgwan);

        Button yakgwan_btn = (Button)findViewById(R.id.yakgwan_btn);
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
