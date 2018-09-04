package com.jscompany.ildang.Common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.UserInfoModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommonUtil {

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


    public static String comma(String str) {
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

}
