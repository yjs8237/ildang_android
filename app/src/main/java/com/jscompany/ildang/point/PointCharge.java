package com.jscompany.ildang.point;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.NotiAdapter;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.model.PointModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointCharge extends AppCompatActivity implements View.OnClickListener{
    private View view;

    private SharedPreferences mPrefs;

    private String cell_no;
    private String user_pwd;
    private String user_type;

    private Button btn_charge_req;

    private ProgressDialog progressDialog;

    private EditText edt_acc_name;
    private EditText edt_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_charge);

        mPrefs = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        cell_no = mPrefs.getString(USER_INFO.CELL_NO , "none");
        user_pwd = mPrefs.getString(USER_INFO.USER_PWD , "none");
        user_type = mPrefs.getString(USER_INFO.USER_TYPE , "none");


        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        btn_charge_req = (Button) findViewById(R.id.btn_charge_req);
        btn_charge_req.setOnClickListener(this);

        edt_acc_name = findViewById(R.id.edt_acc_name);
        edt_money = findViewById(R.id.edt_money);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.back_img_btn :
                backActivity();
                break;

            case R.id.btn_charge_req :
                requestPointCharge();
                break;
        }
    }

    private void backActivity() {
        super.onBackPressed();
    }

    private void requestPointCharge() {
        String acc_name = edt_acc_name.getText().toString();
        if(acc_name == null || acc_name.isEmpty()) {
            showToast("입금자명을 입력해주세요");
            return;
        }
        edt_money = (EditText)findViewById(R.id.edt_money);
        String money = edt_money.getText().toString();
        if(money == null || money.isEmpty()) {
            showToast("입금액을 입력해주세요");
            return;
        }

        boolean isInteger = false;
        try {
            Integer.parseInt(money);
            isInteger = true;
        } catch(Exception e) {

        }

        if(!isInteger) {
            showToast("입금액은 숫자만 입력 가능합니다.");
            return;
        }

        showConfirmDialog("충전요청" , "입금자명 : " + acc_name + " 입금요청금액 : " + money + "원 입금신청하시겠습니까?" , acc_name , money );


    }




    private void showConfirmDialog(String title, String message, final String edt_acc_name , final String edt_money) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestAPI(edt_acc_name , edt_money);
            }
        });
        alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void requestAPI (final String acc_name , String money) {

        PointModel pointModel = new PointModel();
        pointModel.setCell_no(cell_no);
        pointModel.setUser_pwd(user_pwd);
        pointModel.setType("1");    // 1: 충전요청 2: 환전요청
        pointModel.setAcc_name(acc_name);
        pointModel.setMoney(Integer.parseInt(money));

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.pointrequest(pointModel);

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
                        showDialogMessage("성공" , "충전요청 되었습니다. 입금확인 후 빠르게 처리해드리겠습니다.");
                        edt_acc_name.setText("");
                        edt_money.setText("");
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



    private void showToast(String message) {
        Toast.makeText(this , message , Toast.LENGTH_SHORT).show();
    }


    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }
}
