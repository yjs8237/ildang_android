package com.jscompany.ildang.advilgam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.model.IlgamModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvBadIldangRegister  extends AppCompatActivity implements  View.OnClickListener {

    private Button btn_ilgam_req;

    private EditText ed_com_name;
    private EditText ed_cell_no;
    private EditText ed_address;
    private EditText ed_title;
    private EditText ed_content;
    private AdverModel adverModel;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adv_badowner_register);
        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        btn_ilgam_req = (Button)findViewById(R.id.btn_ilgam_req);
        ed_com_name = (EditText)findViewById(R.id.ed_com_name);
        ed_cell_no = (EditText)findViewById(R.id.ed_cell_no);
        ed_address = (EditText)findViewById(R.id.ed_address);
        ed_title = (EditText)findViewById(R.id.ed_title);
        ed_content = (EditText)findViewById(R.id.ed_content);

        TextView textView5 = (TextView)findViewById(R.id.textView5);
        textView5.setText("불성실일당 등록");

        btn_ilgam_req.setOnClickListener(this);
        back_img_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_img_btn :
                backActivity();
                break;

            case R.id.btn_ilgam_req:
                if(checkData()) {
                    showConfirmDialog("불성실일당" , "불성실일당을 등록하시겠습니까?");
                }
                break;
        }
    }

    private void registerIlgam() {

        SharedPreferences mPrefs = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
        adverModel.setCell_no(mPrefs.getString(USER_INFO.CELL_NO, "none"));
        adverModel.setType("5");    // 불성실일당
        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.registerAdver(adverModel);

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
                        ed_address.setText("");
                        ed_cell_no.setText("");
                        ed_address.setText("");
                        ed_content.setText("");
                        ed_com_name.setText("");
                        showDialogMessage("성공" , "성공적으로 등록되었습니다. 등록 광고 삭제는 나의광고내역 메뉴에서 이용 가능합니다.");
                    } else {
                        // 실패
                        showDialogMessage("실패" , jsonObj.get("description").toString());
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
                    }

                } catch (Exception e) {
                    showDialogMessage("Exception" , e.getLocalizedMessage());
                    Log.d("Restapi" , "api : " + e.getLocalizedMessage());
                } finally {
//                    getAdverList();
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

    private boolean checkData() {
        boolean result = true;
        adverModel = new AdverModel();
        String data = "";

        data = ed_com_name.getText().toString();
        if(data == null || data.isEmpty()) {
            CommonUtil.showToast(this, "업체명을 입력하여 주세요.");
            return false;
        } else {
            adverModel.setCom_name(data);
        }

        data = ed_cell_no.getText().toString();
        if(data == null || data.isEmpty()) {
            CommonUtil.showToast(this, "휴대폰번호를 입력하여 주세요.");
            return false;
        } else {
            adverModel.setContact_num(data);
        }

        data = ed_address.getText().toString();
        if(data == null || data.isEmpty()) {
            CommonUtil.showToast(this, "주소를 입력하여 주세요.");
            return false;
        } else {
            adverModel.setLocation(data);
        }

        data = ed_title.getText().toString();
        if(data == null || data.isEmpty()) {
            CommonUtil.showToast(this, "제목을 입력하여 주세요.");
            return false;
        } else {
            adverModel.setTitle(data);
        }

        data = ed_content.getText().toString();
        if(data == null || data.isEmpty()) {
            CommonUtil.showToast(this, "내용을 입력하여 주세요.");
            return false;
        } else {
            adverModel.setContent(data);
        }

        return result;
    }

    private void showConfirmDialog(String title, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registerIlgam();
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
