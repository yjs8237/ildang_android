package com.jscompany.ildang.preference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingPreference extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener , View.OnClickListener{

    private SharedPreferences pref;

    private CheckBoxPreference ring_push_Preference;
    private CheckBoxPreference vibrate_push_Preference;

    private ProgressDialog progressDialog;

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

        if(pref.getBoolean("ring_push" , true)) {
            Log.d("pref" , "ring_push");
        }

        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals("ring_push")) {
            boolean isPushSetting= pref.getBoolean(key , false);
            Log.d("pref" , "ring_push 이벤트 " + isPushSetting);
//            changeSetting(isPushSetting);
        }

        if(key.equals("vibrate_push")) {
            Log.d("pref" , "vibrate_push 이벤트 " +  pref.getBoolean(key , false));
        }
    }

    private void changeSetting(boolean isPush) {

        UserInfoModel userInfoModel = new UserInfoModel();

        SharedPreferences mPref = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        String push_yn = mPref.getString(USER_INFO.PUSH_YN , "none");
        if(cell_no.equals("none")) {
            return;
        }


        if(push_yn.equals("none") || push_yn.isEmpty()) {
            return;
        }

        userInfoModel.setCell_no(cell_no);
        if(isPush) {
            userInfoModel.setPush_yn("Y");
        } else {
            userInfoModel.setPush_yn("N");
        }
        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.updatePush(userInfoModel);
        progressDialog = ProgressDialog.show(this, CONST.progress_title, CONST.progress_body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    Log.d("Restapi" , "api : " + response.body());

                    JsonObject jsonObj = response.body();
                    if(jsonObj.get("result").toString().equals("0")) {
                        // 성공
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
//                        mAdapter.removeItem(position);

                    } else {
                        // 실패
                        showDialogMessage("실패" , jsonObj.get("description").toString());
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
                    }

                } catch (Exception e) {
                    showDialogMessage("Exception" , e.getLocalizedMessage());
                    Log.d("Restapi" , "api : " + e.getLocalizedMessage());
                } finally {
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Restapi" , "api : " + "fail!!! " + t.getLocalizedMessage());
                progressDialog.dismiss();
                showNetworkError();
            }
        });

    }


    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
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
