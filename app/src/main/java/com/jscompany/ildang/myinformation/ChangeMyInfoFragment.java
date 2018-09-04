package com.jscompany.ildang.myinformation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jscompany.ildang.Common.USER_INFO.ADDRESS;
import static com.jscompany.ildang.Common.USER_INFO.CELL_NO;
import static com.jscompany.ildang.Common.USER_INFO.USER_ABLE_JOB;
import static com.jscompany.ildang.Common.USER_INFO.USER_BIR_DAY;
import static com.jscompany.ildang.Common.USER_INFO.USER_BIR_MONTH;
import static com.jscompany.ildang.Common.USER_INFO.USER_BIR_YEAR;
import static com.jscompany.ildang.Common.USER_INFO.USER_NAME;
import static com.jscompany.ildang.Common.USER_INFO.USER_NICK;
import static com.jscompany.ildang.Common.USER_INFO.USER_PWD;
import static com.jscompany.ildang.Common.USER_INFO.USER_TYPE;

public class ChangeMyInfoFragment extends PreferenceFragment  implements Preference.OnPreferenceChangeListener{

    private EditTextPreference editPref_nick;
    private EditTextPreference editPref_address;

    private EditTextPreference myinfo_cell_no;
    private EditTextPreference myinfo_user_pwd;
    private EditTextPreference myinfo_user_name;
    private EditTextPreference myinfo_bir_year;
    private EditTextPreference myinfo_bir_month;
    private EditTextPreference myinfo_bir_day;
    private EditTextPreference myinfo_able_job;
    private EditTextPreference myinfo_com_name;



    private SharedPreferences mPref;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mPref = getActivity().getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);


        String user_type = mPref.getString(USER_INFO.USER_TYPE , "none");

        if (user_type.equals("1")) {
            addPreferencesFromResource(R.xml.myinfo_preference);
        } else {
            addPreferencesFromResource(R.xml.myinfo_preference_order);
        }


//        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        String user_name = mPref.getString(USER_INFO.USER_NAME , "none");

        String user_able_job = mPref.getString(USER_INFO.USER_ABLE_JOB , "none");

        String user_pwd = mPref.getString(USER_INFO.USER_PWD , "none");
        String user_nick = mPref.getString(USER_INFO.USER_NICK , "none");
        String address = mPref.getString(USER_INFO.ADDRESS , "none");

        String bir_year = mPref.getString(USER_INFO.USER_BIR_YEAR , "none");
        String bir_month = mPref.getString(USER_INFO.USER_BIR_MONTH , "none");
        String bir_day = mPref.getString(USER_INFO.USER_BIR_DAY , "none");


        myinfo_cell_no =(EditTextPreference) findPreference("myinfo_cell_no");
        myinfo_cell_no.setSummary(cell_no);

        myinfo_user_pwd =(EditTextPreference) findPreference("myinfo_user_pwd");

        myinfo_user_name =(EditTextPreference) findPreference("myinfo_user_name");
        myinfo_user_name.setSummary(user_name);

        editPref_nick =(EditTextPreference) findPreference("myinfo_nick_name");
        editPref_nick.setSummary(user_nick);

        editPref_address =(EditTextPreference) findPreference("myinfo_address");
        editPref_address.setSummary(address);

        if (user_type.equals("1")) {
            //기술자의 경우
            myinfo_able_job =(EditTextPreference) findPreference("myinfo_able_job");
            myinfo_able_job.setSummary(user_able_job);

            myinfo_bir_year =(EditTextPreference) findPreference("myinfo_bir_year");
            myinfo_bir_year.setSummary(bir_year);

            myinfo_bir_month =(EditTextPreference) findPreference("myinfo_bir_month");
            myinfo_bir_month.setSummary(bir_month);

            myinfo_bir_day =(EditTextPreference) findPreference("myinfo_bir_day");
            myinfo_bir_day.setSummary(bir_day);

            myinfo_bir_year.setOnPreferenceChangeListener(this);
            myinfo_bir_month.setOnPreferenceChangeListener(this);
            myinfo_bir_day.setOnPreferenceChangeListener(this);

            myinfo_able_job.setOnPreferenceChangeListener(this);

        } else {
            // 오더주의 경우
        }

        myinfo_user_pwd.setOnPreferenceChangeListener(this);
        myinfo_user_name.setOnPreferenceChangeListener(this);
        editPref_nick.setOnPreferenceChangeListener(this);
        editPref_address.setOnPreferenceChangeListener(this);

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        UserInfoModel updateUserModel = null;
        boolean isPassworkdChange = false;


        if(preference.getKey().equals("myinfo_user_pwd")) {

            String user_pwd = mPref.getString(USER_INFO.USER_PWD , "none");
            String new_user_pwd = (String)newValue;
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(USER_PWD , new_user_pwd);
            editor.commit();

            updateUserModel = new UserInfoModel();
            updateUserModel.setCell_no(mPref.getString(USER_INFO.CELL_NO , "none"));
            updateUserModel.setUser_pwd(new_user_pwd);

            isPassworkdChange = true;

        } else if(preference.getKey().equals("myinfo_user_name")) {
            String user_name = mPref.getString(USER_INFO.USER_NAME , "none");
            String new_user_name = (String)newValue;
            myinfo_user_name.setSummary(new_user_name);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(USER_NAME , new_user_name);
            editor.commit();

            updateUserModel = new UserInfoModel();
            updateUserModel.setCell_no(mPref.getString(USER_INFO.CELL_NO , "none"));
            updateUserModel.setUser_name(new_user_name);
            updateUserModel.setUser_pwd(mPref.getString(USER_INFO.USER_PWD , "none"));

        } else if(preference.getKey().equals("myinfo_nick_name")) {
            String user_nick = mPref.getString(USER_INFO.USER_NICK , "none");
            Log.d("LOGIN" , "myinfo_nick_name : " + user_nick + " , " + newValue);
            String new_user_nick = (String)newValue;
            editPref_nick.setSummary(new_user_nick);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(USER_NICK , new_user_nick);
            editor.commit();

            updateUserModel = new UserInfoModel();
            updateUserModel.setCell_no(mPref.getString(USER_INFO.CELL_NO , "none"));
            updateUserModel.setUser_nick(new_user_nick);
            updateUserModel.setUser_pwd(mPref.getString(USER_INFO.USER_PWD , "none"));

        } else if(preference.getKey().equals("myinfo_address")) {

            String address = mPref.getString(USER_INFO.ADDRESS , "none");
            String new_address = (String)newValue;
            editPref_address.setSummary((String)newValue);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(ADDRESS , new_address);
            editor.commit();

            updateUserModel = new UserInfoModel();
            updateUserModel.setCell_no(mPref.getString(USER_INFO.CELL_NO , "none"));
            updateUserModel.setUser_pwd(mPref.getString(USER_INFO.USER_PWD , "none"));
            updateUserModel.setAddress(new_address);

        } else if(preference.getKey().equals("myinfo_bir_year")) {


            String new_myinfo_bir_year = (String)newValue;

            if(new_myinfo_bir_year.length() != 4) {
                CommonUtil.showToast(getActivity(), "4자리 숫자만 입력 가능합니다.");
                return  false;
            }
            if(!CommonUtil.isNumber(new_myinfo_bir_year)) {
                CommonUtil.showToast(getActivity(), "4자리 숫자만 입력 가능합니다.");
                return  false;
            }

            myinfo_bir_year.setSummary((String)newValue);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(USER_BIR_YEAR , new_myinfo_bir_year);
            editor.commit();

            updateUserModel = new UserInfoModel();
            updateUserModel.setCell_no(mPref.getString(USER_INFO.CELL_NO , "none"));
            updateUserModel.setUser_pwd(mPref.getString(USER_INFO.USER_PWD , "none"));
            updateUserModel.setUser_bir_year(new_myinfo_bir_year);

        } else if(preference.getKey().equals("myinfo_bir_month")) {

            String new_myinfo_bir_month = (String)newValue;

            if(new_myinfo_bir_month.length() > 2 || !CommonUtil.isNumber(new_myinfo_bir_month)) {
                CommonUtil.showToast(getActivity(), "2자리 이하 숫자만 입력 가능합니다.");
                return  false;
            }

            int number = Integer.parseInt(new_myinfo_bir_month);
            if(number <= 0 || number > 12 ) {
                CommonUtil.showToast(getActivity(), "올바른 입력이 아닙니다.");
                return  false;
            }


            myinfo_bir_month.setSummary((String)newValue);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(USER_BIR_MONTH , new_myinfo_bir_month);
            editor.commit();

            updateUserModel = new UserInfoModel();
            updateUserModel.setCell_no(mPref.getString(USER_INFO.CELL_NO , "none"));
            updateUserModel.setUser_pwd(mPref.getString(USER_INFO.USER_PWD , "none"));
            updateUserModel.setUser_bir_month(new_myinfo_bir_month);

        } else if(preference.getKey().equals("myinfo_bir_day")) {

            String new_myinfo_bir_day = (String)newValue;

            if(new_myinfo_bir_day.length() > 2 || !CommonUtil.isNumber(new_myinfo_bir_day)) {
                CommonUtil.showToast(getActivity(), "2자리 이하 숫자만 입력 가능합니다.");
                return  false;
            }

            int number = Integer.parseInt(new_myinfo_bir_day);
            if(number <= 0 || number > 31 ) {
                CommonUtil.showToast(getActivity(), "올바른 입력이 아닙니다.");
                return  false;
            }

            myinfo_bir_day.setSummary((String)newValue);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(USER_BIR_DAY , new_myinfo_bir_day);
            editor.commit();

            updateUserModel = new UserInfoModel();
            updateUserModel.setCell_no(mPref.getString(USER_INFO.CELL_NO , "none"));
            updateUserModel.setUser_pwd(mPref.getString(USER_INFO.USER_PWD , "none"));
            updateUserModel.setUser_bir_day(new_myinfo_bir_day);

        } else if(preference.getKey().equals("myinfo_able_job")) {

            String user_able_job = (String)newValue;

            myinfo_able_job.setSummary((String)newValue);
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(USER_ABLE_JOB , user_able_job);
            editor.commit();

            updateUserModel = new UserInfoModel();
            updateUserModel.setCell_no(mPref.getString(USER_INFO.CELL_NO , "none"));
            updateUserModel.setUser_pwd(mPref.getString(USER_INFO.USER_PWD , "none"));
            updateUserModel.setUser_able_job(user_able_job);

        }




        if(updateUserModel != null) {
            updateUserInfo(updateUserModel , isPassworkdChange);
        }

        return true;
    }


    private void updateUserInfo(UserInfoModel updateUserModel , boolean isPassworkdChange){

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = null;
        if(isPassworkdChange) {
            // 비밀번호 변경이면 다른 서비스를 호출
            call = restService.changePassword(updateUserModel);
        } else {
            call = restService.updateUserInfo(updateUserModel);
        }

        progressDialog = ProgressDialog.show(getActivity(), CONST.progress_title, CONST.progress_body);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    Log.d("Restapi" , "api : " + response.body());

                    JsonObject jsonObj = response.body();
                    if(jsonObj.get("result").toString().equals("0")) {
                        // 성공
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());

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

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(getActivity(), title , message);
    }
    private void showNetworkError() {
        CommonUtil.showDialog(getActivity(), "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

}
