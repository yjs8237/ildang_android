package com.jscompany.ildang.fcmPush;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jscompany.ildang.Common.USER_INFO.CELL_NO;
import static com.jscompany.ildang.Common.USER_INFO.USER_PWD;

public class FcmPushToken extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        updateToken(refreshedToken);
        pushToServer(refreshedToken);
    }

    private void updateToken(String token) {
        SharedPreferences mPref = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(USER_INFO.TOKEN , token);
        editor.commit();
    }

    private void pushToServer(String token) {

        UserInfoModel userInfoModel = new UserInfoModel();
        SharedPreferences mPref = getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
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
