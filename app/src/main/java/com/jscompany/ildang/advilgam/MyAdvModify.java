package com.jscompany.ildang.advilgam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.fcmPush.FcmPushService;
import com.jscompany.ildang.ildanghistory.IldangHistory;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdvModify  extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;

    private Button btn_ilgam_req;
    private EditText ed_com_name;
    private EditText ed_cell_no;
    private EditText ed_address;
    private EditText ed_title;
    private EditText ed_content;

    private long ad_seq;
    private String com_name;
    private String contact_num;
    private String address;
    private String title;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_adv_modify);

        Intent intent = getIntent();
        ad_seq = intent.getExtras().getLong("ad_seq");
        com_name = intent.getExtras().getString("com_name");
        contact_num = intent.getExtras().getString("contact_num");
        address = intent.getExtras().getString("address");
        title = intent.getExtras().getString("title");
        content = intent.getExtras().getString("content");


        btn_ilgam_req = (Button)findViewById(R.id.btn_ilgam_req);
        ed_com_name = (EditText)findViewById(R.id.ed_com_name);
        ed_cell_no = (EditText)findViewById(R.id.ed_cell_no);
        ed_address = (EditText)findViewById(R.id.ed_address);
        ed_title = (EditText)findViewById(R.id.ed_title);
        ed_content = (EditText)findViewById(R.id.ed_content);

        ed_com_name.setText(com_name);
        ed_cell_no.setText(contact_num);
        ed_address.setText(address);
        ed_title.setText(title);
        ed_content.setText(content);

        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);
        btn_ilgam_req.setOnClickListener(this);

//        doWork();

    }


    private void modifyAdv() {

        AdverModel model = new AdverModel();
        SharedPreferences mPrefs = getSharedPreferences("USER_INFO" , MODE_PRIVATE);

        model.setCell_no(mPrefs.getString(USER_INFO.CELL_NO, "none"));
        model.setAd_seq(ad_seq);
        model.setCom_name(ed_com_name.getText().toString());
        model.setContact_num(ed_cell_no.getText().toString());
        model.setLocation(ed_address.getText().toString());
        model.setTitle(ed_title.getText().toString());
        model.setContent(ed_content.getText().toString());

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.adverModify(model);
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
                        Intent intent = new Intent(MyAdvModify.this, MyAdvDetail.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("ad_seq" , ad_seq);
                        startActivity(intent);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img_btn:
                backActivity();
                break;

            case R.id.btn_ilgam_req:
                showConfirmDialog("광고 수정" , "광고내용을 수정하시겠습니까?");
                break;
        }
    }


    private void showConfirmDialog(String title, String message  ) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modifyAdv();
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
    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }
    private void backActivity() {
        super.onBackPressed();
    }
}
