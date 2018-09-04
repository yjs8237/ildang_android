package com.jscompany.ildang.qna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.QnaModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnaDetail extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_qna_title;
    private TextView tv_qna_content;
    private TextView tv_qna_reply;
    private TextView tv_qna_reg_date;
    private String qna_seq;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qna_detail);

        Intent intent = getIntent();
        qna_seq = intent.getExtras().get("qna_seq").toString();
        Log.d("Restapi" , "qna_seq : " + qna_seq);

        ImageButton back_img_btn =(ImageButton) findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        tv_qna_title = (TextView)findViewById(R.id.tv_qna_title);
        tv_qna_content = (TextView)findViewById(R.id.tv_qna_content);
        tv_qna_reply = (TextView)findViewById(R.id.tv_qna_reply);
        tv_qna_reg_date= (TextView)findViewById(R.id.tv_qna_reg_date);

        if(qna_seq != null && !qna_seq.isEmpty()) {
            getQnaInfo();
        }

    }

    private void getQnaInfo() {
        QnaModel qnaModel = new QnaModel();
        qnaModel.setQna_seq(Long.parseLong(qna_seq));

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.qnadetail(qnaModel);

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

                        tv_qna_title.setText(jsonObj.get("title").getAsString());
                        tv_qna_content.setText(jsonObj.get("content").getAsString());
                        tv_qna_reg_date.setText(jsonObj.get("reg_date").getAsString());
                        if(!jsonObj.get("reply").isJsonObject()) {
                            tv_qna_reply.setText(jsonObj.get("reply").getAsString());
                        }

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
        switch (v.getId()) {
            case R.id.back_img_btn :
                backActivity();
                break;
        }
    }

    private void backActivity() {
        super.onBackPressed();
    }
}
