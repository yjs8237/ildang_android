package com.jscompany.ildang.preference;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jscompany.ildang.R;

public class SettingActivity  extends PreferenceActivity implements  View.OnClickListener{

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //addPreferencesFromResource(R.xml.preference);
        setContentView(R.layout.setting_layout);
        /*
        toolbar = findViewById(R.id.setting_toolbar);
        toolbar.setTitle("환경설정");
        toolbar.setNavigationIcon(R.drawable.people_icon);
        */

        ImageButton img_back_btn = (ImageButton)findViewById(R.id.imageButton2);
        img_back_btn.setOnClickListener(this);

        // PreferenceScreen 은 일반 Layout 을 사용 못하기 때문에 Fragment 를 Replace 하는 방식으로 사용한다.
        getFragmentManager().beginTransaction().replace(R.id.setting_content , new SettingFragment()).commit();

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imageButton2:
                // 뒤로가기 이미지버튼
                backActivity();
                break;
        }
    }

    private void backActivity() {
        super.onBackPressed();
    }
}
