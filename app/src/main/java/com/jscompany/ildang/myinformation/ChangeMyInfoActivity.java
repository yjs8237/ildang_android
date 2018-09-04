package com.jscompany.ildang.myinformation;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.jscompany.ildang.R;
import com.jscompany.ildang.preference.SettingFragment;

public class ChangeMyInfoActivity extends PreferenceActivity implements  View.OnClickListener  {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //addPreferencesFromResource(R.xml.preference);
        setContentView(R.layout.change_myinfo_layout);
        /*
        toolbar = findViewById(R.id.setting_toolbar);
        toolbar.setTitle("환경설정");
        toolbar.setNavigationIcon(R.drawable.people_icon);
        */

        ImageButton img_back_btn = (ImageButton)findViewById(R.id.imageButton2);
        img_back_btn.setOnClickListener(this);




        // PreferenceScreen 은 일반 Layout 을 사용 못하기 때문에 Fragment 를 Replace 하는 방식으로 사용한다.
        getFragmentManager().beginTransaction().replace(R.id.myinfo_content , new ChangeMyInfoFragment()).commit();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton2:
                backActivity();
                break;
        }
    }

    private void backActivity() {
        super.onBackPressed();
    }


    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            Log.d("##MyInfo##" , "111");
            if (p.getTitle().toString().toLowerCase().contains("password"))
            {
                Log.d("##MyInfo##" , "222");
                p.setSummary("******");
            } else {
                Log.d("##MyInfo##" , "333");
                p.setSummary(editTextPref.getText());
            }
        }
        if (p instanceof MultiSelectListPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            p.setSummary(editTextPref.getText());
        }
    }

}
