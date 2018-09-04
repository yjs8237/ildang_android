package com.jscompany.ildang.preference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.jscompany.ildang.R;

public class SettingPreference extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener , View.OnClickListener{

    private SharedPreferences pref;

    private CheckBoxPreference ring_push_Preference;
    private CheckBoxPreference vibrate_push_Preference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preference);
        setContentView(R.layout.setting_layout);

        ring_push_Preference = (CheckBoxPreference)findPreference("ring_push");
//        vibrate_push_Preference = (CheckBoxPreference)findPreference("vibrate_push");

        pref = PreferenceManager.getDefaultSharedPreferences(this);


        ImageButton img_back_btn = (ImageButton)findViewById(R.id.imageButton2);
        img_back_btn.setOnClickListener(this);

        if(pref.getBoolean("ring_push" , false)) {
            Log.d("pref" , "ring_push");
        }

        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals("ring_push")) {
            Log.d("pref" , "ring_push 이벤트 " +  pref.getBoolean(key , false));
        }

        if(key.equals("vibrate_push")) {
            Log.d("pref" , "vibrate_push 이벤트 " +  pref.getBoolean(key , false));
        }
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
