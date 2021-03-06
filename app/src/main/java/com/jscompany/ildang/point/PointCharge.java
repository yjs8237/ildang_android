package com.jscompany.ildang.point;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
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
import com.jscompany.ildang.model.UserInfoModel;
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

    private String user_point;

    private Button btn_charge_req;

    private ProgressDialog progressDialog;

    private EditText edt_acc_name;
    private EditText edt_money;

    private ScrollView charge_scroll;
    private ScrollView refund_scroll;
    private Button charge_button;
    private Button refund_button;

    private Button btn_refund_req;

    private EditText edit_back_name;
    private EditText edit_acc_no;
    private EditText edit_acc_name;
    private EditText edit_refunc_money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.point_charge);
        setContentView(R.layout.point_tong);

        mPrefs = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        cell_no = mPrefs.getString(USER_INFO.CELL_NO , "none");
        user_pwd = mPrefs.getString(USER_INFO.USER_PWD , "none");
        user_type = mPrefs.getString(USER_INFO.USER_TYPE , "none");


        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        charge_button =(Button) findViewById(R.id.charge_button);
        refund_button =(Button) findViewById(R.id.refund_button);
        btn_refund_req =(Button) findViewById(R.id.btn_refund_req);
        charge_button.setOnClickListener(this);
        refund_button.setOnClickListener(this);
        btn_refund_req.setOnClickListener(this);
        charge_scroll = (ScrollView) findViewById(R.id.charge_scroll);
        refund_scroll = (ScrollView) findViewById(R.id.refund_scroll);
        edit_back_name = (EditText)findViewById(R.id.edit_back_name);
        edit_acc_no = (EditText)findViewById(R.id.edit_acc_no);
        edit_acc_name   = (EditText)findViewById(R.id.edit_acc_name);
        edit_refunc_money   = (EditText)findViewById(R.id.edit_refunc_money);

//        back_img_btn.setBackground(ContextCompat.getDrawable(this, R.drawable.lable_border));


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

            case R.id.charge_button:
                charge_scroll.setVisibility(View.VISIBLE);
                refund_scroll.setVisibility(View.GONE);
                charge_button.setBackground(ContextCompat.getDrawable(this, R.drawable.lable_border));
                refund_button.setBackground(ContextCompat.getDrawable(this,R.drawable.layout_border));
                break;

            case R.id.refund_button:
                charge_scroll.setVisibility(View.GONE);
                refund_scroll.setVisibility(View.VISIBLE);
                charge_button.setBackground(ContextCompat.getDrawable(this, R.drawable.layout_border));
                refund_button.setBackground(ContextCompat.getDrawable(this,R.drawable.lable_border));
                getUserInfo();
                break;

            case R.id.btn_refund_req:
                requestRefund();
                break;

        }
    }

    private void backActivity() {
        super.onBackPressed();
    }



    private void requestRefund() {

        if(Integer.parseInt(user_point) <= 0) {
            showToast("환불 가능한 포인트가 없습니다.");
            return;
        }
        String back_name = edit_back_name.getText().toString();
        if(back_name == null || back_name.isEmpty()) {
            showToast("은행명을 입력해주세요");
            return;
        }
        String acc_name = edit_acc_name.getText().toString();
        if(acc_name == null || acc_name.isEmpty()) {
            showToast("예금주를 입력해주세요");
            return;
        }
        String acc_no = edit_acc_no.getText().toString();
        if(acc_no == null || acc_no.isEmpty()) {
            showToast("계좌번호를 입력해주세요");
            return;
        }
        String money = edit_refunc_money.getText().toString();
        if(money == null || money.isEmpty()) {
            showToast("환전요청 금액을 입력해주세요");
            return;
        }

        boolean isInteger = false;
        try {
            Integer.parseInt(money);
            isInteger = true;
        } catch(Exception e) {

        }
        if(!isInteger) {
            showToast("환전요청 금액은 숫자만 입력 가능합니다.");
            return;
        }


        if(Integer.parseInt(user_point) < Integer.parseInt(money)) {
            showToast("환전 가능 금액이 아닙니다.");
            return;
        }

        String message = "은행 : " + back_name + " 예금주 : " + acc_name + "\n" + " 계좌번호 : " + acc_no + "\n" + "요청금액 : " + money + " 원 환전 신청하시겠습니까?";
        showConfirmDialog("환전요청" , message, back_name , acc_name , acc_no, money );

    }

    private void getUserInfo() {
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setCell_no(cell_no);
        userInfoModel.setUser_pwd(user_pwd);

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.getUserInfo(userInfoModel);

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

                        TextView tv_current_point =(TextView)findViewById(R.id.tv_current_point);
                        user_point = jsonObj.get("user_point").getAsString();
                        tv_current_point.setText(CommonUtil.comma(user_point));

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



    private void showConfirmDialog(String title, String message, final String back_name , final String acc_name , final String acc_no , final String money) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pointRefund(back_name , acc_name , acc_no , money);
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



    private void pointRefund (final String back_name , String acc_name , String acc_no , String money ) {

        PointModel pointModel = new PointModel();
        pointModel.setCell_no(cell_no);
        pointModel.setUser_pwd(user_pwd);
        pointModel.setType("2");    // 1: 충전요청 2: 환전요청
        pointModel.setBank_name(back_name);
        pointModel.setAcc_name(acc_name);
        pointModel.setAcc_no(acc_no);
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
                        showDialogMessage("성공" , "환전요청 되었습니다. 입금확인 후 빠르게 처리해드리겠습니다.");
                        edit_acc_name.setText("");
                        edit_acc_no.setText("");
                        edit_back_name.setText("");
                        edit_refunc_money.setText("");
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
