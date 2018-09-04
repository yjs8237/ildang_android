package com.jscompany.ildang.preference;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.PointAdapter;

public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


    public static int TIME_OUT = 1001;

    private ListView listView;
    private ProgressDialog progressDialog;

    private PointAdapter mAdapter;
    private View view;

    private SharedPreferences pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

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
}
