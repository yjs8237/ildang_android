package com.jscompany.ildang.Common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonUtil {

    public static String APP_VER = "";

    public static int index = 0;
    private static List adverList = new ArrayList<>();

    public static HashMap<String, String> loc_dong_map = new HashMap<>();

    public static AdverModel getAdverModel() {

        AdverModel adverModel;

        if(adverList.size() == 0) {
            return null;
        }

        if(adverList.size() <= index) {
            index = 0;
        }
        adverModel = (AdverModel) adverList.get(index);
        index++;
        return adverModel;

    }

    public static UserInfoModel getUserinfo(AppCompatActivity activity) {
        SharedPreferences mPrefs = activity.getSharedPreferences("USER_INFO" , activity.MODE_PRIVATE);
        UserInfoModel userModel = new UserInfoModel();

        userModel.setCell_no(mPrefs.getString(USER_INFO.CELL_NO, "none"));

        return userModel;
    }

    public static void adverListInitial() {
        adverList = new ArrayList<>();
    }
    public static void addAdverModel(AdverModel adverModel) {
        adverList.add(adverModel);
    }
    public static void removeAdverModel(long ad_seq) {
        for (int i=0; i<adverList.size(); i++) {
            if(ad_seq == ((AdverModel)adverList.get(i)).getAd_seq()) {
                adverList.remove(i);
                break;
            }
        }
    }


    public static String cell_number(String cell_no) {
        if(cell_no == null || cell_no.isEmpty()) {
            return "";
        }

        if(cell_no.length() < 8) {
            return cell_no;
        }

        String resultStr = "";
        resultStr = cell_no.substring(0,3);

        resultStr += "-";
        resultStr += cell_no.substring(3, 7);

        resultStr += "-";
        resultStr += cell_no.substring(7);

        return resultStr;

    }

    public static String comma(String str) {
        if(str == null) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(Integer.parseInt(str));
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch( Exception e) {
            return false;
        }
    }

    public static void showToast(Context context , String message) {
        Toast.makeText(context , message , Toast.LENGTH_SHORT).show();
    }


    public static void showDialog(Context context, String title, String message ) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }



    public static UserInfoModel getUserInfo(Context context) {
        SharedPreferences mPref = context.getSharedPreferences("USER_INFO" , context.MODE_PRIVATE);
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setCell_no(mPref.getString(USER_INFO.CELL_NO, ""));
        userInfoModel.setUser_pwd(mPref.getString(USER_INFO.USER_PWD, ""));
        userInfoModel.setUser_name(mPref.getString(USER_INFO.USER_NAME, ""));
        userInfoModel.setUser_nick(mPref.getString(USER_INFO.USER_NICK, ""));
        userInfoModel.setUser_type(mPref.getString(USER_INFO.USER_TYPE, ""));

        return userInfoModel;
    }



    public static void updateToken(String cell_no , String token) {
        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setCell_no(cell_no);
        userInfoModel.setToken(token);

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.token(userInfoModel);
//        progressDialog = ProgressDialog.show(getActivity(), CONST.progress_title, CONST.progress_body);
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
//                        showDialogMessage("실패" , jsonObj.get("description").toString());
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
                    }

                } catch (Exception e) {
//                    showDialogMessage("Exception" , e.getLocalizedMessage());
                    Log.d("Restapi" , "api : " + e.getLocalizedMessage());
                } finally {
//                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Restapi" , "api : " + "fail!!! " + t.getLocalizedMessage());
//                progressDialog.dismiss();
//                showNetworkError();
            }
        });
    }

}
