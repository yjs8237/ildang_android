package com.jscompany.ildang.qna;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.QnaModel;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QnaRequest extends AppCompatActivity implements  View.OnClickListener{

    private View view;

    private EditText ed_qna_title;
    private EditText ed_qna_content;

    private ProgressDialog progressDialog;

    private SharedPreferences mPrefs;

    private String cell_no;
    private String user_pwd;
    private String user_type;

    private String user_point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qna_request);


        Button btn_qna_req =(Button) findViewById(R.id.btn_qna_req);

        ed_qna_title = (EditText)findViewById(R.id.ed_qna_title);
        ed_qna_content = (EditText)findViewById(R.id.ed_qna_content);

        mPrefs = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        cell_no = mPrefs.getString(USER_INFO.CELL_NO , "none");
        user_pwd = mPrefs.getString(USER_INFO.USER_PWD , "none");
        user_type = mPrefs.getString(USER_INFO.USER_TYPE , "none");
        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        btn_qna_req.setOnClickListener(this);
    }

    private void requestQna() {

        String title    = ed_qna_title.getText().toString();
        String content  = ed_qna_content.getText().toString();

        if(title == null || title.isEmpty()) {
            CommonUtil.showToast(this , "제목을 입력해주세요.");
            return;
        }
        if(content == null || content.isEmpty()) {
            CommonUtil.showToast(this , "문의내용을 입력해주세요.");
            return;
        }

        QnaModel qnaModel = new QnaModel();
        qnaModel.setCell_no(cell_no);
        qnaModel.setUser_pwd(user_pwd);
        qnaModel.setTitle(title);
        qnaModel.setContent(content);

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.qnarequest(qnaModel);

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
                        showDialogMessage("성공" , "성공적으로 접수되었습니다.");
                        ed_qna_title.setText("");
                        ed_qna_content.setText("");
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

            case R.id.btn_qna_req :
                requestQna();
                break;
            case R.id.back_img_btn :
                backActivity();
                break;
        }

    }

    private void backActivity() {
        super.onBackPressed();
    }
}
