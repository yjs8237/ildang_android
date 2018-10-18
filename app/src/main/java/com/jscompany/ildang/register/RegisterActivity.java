package com.jscompany.ildang.register;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.UserLoginInfo;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.ArrayList;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{


    private String user_type;

    private String cell_no;
    private String user_name;
    private String user_nick;
    private String address;
    private String bir_year;
    private String bir_month;
    private String bir_day;
    private String com_name;
    private String user_pwd;

    private String user_able_job;

    private ArrayList<IldangModel> dong_list;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            user_type = intent.getExtras().getString("user_type" , "none");
        } else {
        }

        if(user_type.equals("1")) {
            setContentView(R.layout.register_user_v2);
        } else {
            setContentView(R.layout.order_register_user);
        }

        Button btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register :
                registerUser();
                break;
        }
    }

    private void registerUser() {
        if(user_type.equals("1")){
            // 기술자 회원가입
            EditText edt_cell_no = (EditText)findViewById(R.id.edt_cell_no);
            EditText edt_user_name = (EditText)findViewById(R.id.edt_user_name);
            EditText edt_user_nick = (EditText)findViewById(R.id.edt_user_nick);
            EditText edt_address = (EditText)findViewById(R.id.edt_address);
            EditText edt_user_pwd = (EditText)findViewById(R.id.edt_user_pwd);

            EditText edt_user_able_job_1 = (EditText)findViewById(R.id.edt_user_able_job_1);
            EditText edt_user_able_job_2 = (EditText)findViewById(R.id.edt_user_able_job_2);
            EditText edt_user_able_job_3 = (EditText)findViewById(R.id.edt_user_able_job_3);
            EditText edt_user_able_job_4 = (EditText)findViewById(R.id.edt_user_able_job_4);


            Spinner spinner_year = (Spinner)findViewById(R.id.spinner_year);
            Spinner spinner_month = (Spinner)findViewById(R.id.spinner_month);
            Spinner spinner_day = (Spinner)findViewById(R.id.spinner_day);
            cell_no = edt_cell_no.getText().toString();
            if(cell_no == null || cell_no.isEmpty()) {
                CommonUtil.showToast(this, "휴대폰번호를 입력해주세요.");
                return;
            }
            user_pwd = edt_user_pwd.getText().toString();
            if(user_pwd == null || user_pwd.isEmpty()) {
                CommonUtil.showToast(this, "비밀번호를 입력해주세요.");
                return;
            }
            user_name = edt_user_name.getText().toString();
            if(user_name == null || user_name.isEmpty()) {
                CommonUtil.showToast(this, "이름을 입력해주세요.");
                return;
            }
            user_nick = edt_user_nick.getText().toString();
            if(user_nick == null || user_nick.isEmpty()) {
                CommonUtil.showToast(this, "닉네임을 입력해주세요.");
                return;
            }
            address = edt_address.getText().toString();
            if(address == null || address.isEmpty()) {
                CommonUtil.showToast(this, "주소를 입력해주세요.");
                return;
            }
            bir_year = spinner_year.getSelectedItem().toString();
            bir_month = spinner_month.getSelectedItem().toString();
            bir_day = spinner_day.getSelectedItem().toString();


            String job_1 = edt_user_able_job_1.getText().toString();
            String job_2 = edt_user_able_job_2.getText().toString();
            String job_3 = edt_user_able_job_3.getText().toString();
            String job_4 = edt_user_able_job_4.getText().toString();

            user_able_job = job_1 + " " + job_2 + " " + job_3 + " " + job_4;
            if(user_able_job == null || user_able_job.isEmpty()) {
                CommonUtil.showToast(this, "작업가능 업무를 최소 1개이상 입력해주세요.");
                return;
            }

            if(user_able_job.trim().isEmpty()) {
                CommonUtil.showToast(this, "작업가능 업무를 최소 1개이상 입력해주세요.");
                return;
            }

            if(user_able_job.length() > 16) {
                CommonUtil.showToast(this, "작업가능 업무 16자 초과");
                return;
            }


            StringBuffer sb = new StringBuffer();
            sb.append("휴대폰번호 : ").append("\t").append(cell_no).append("\n");
            sb.append("이름 : ").append("\t").append(user_name).append("\n");
            sb.append("닉네임 : ").append("\t").append(user_nick).append("\n");
            sb.append("작업가능업무 : ").append("\t").append(user_able_job).append("\n");
            sb.append("주소 : ").append("\t").append(address).append("\n").append("\n");
            sb.append("위 정보가 맞습니까?").append("\n").append("휴대폰번호는 나중에 변경하실 수 없습니다.");
            showConfirmDialog("회원가입" , sb.toString());


        } else {
            // 오더주 회원가입
            EditText edt_cell_no = (EditText)findViewById(R.id.edt_cell_no);
            EditText edt_user_name = (EditText)findViewById(R.id.edt_user_name);
            EditText edt_user_pwd = (EditText)findViewById(R.id.edt_user_pwd);
            EditText edt_com_name = (EditText)findViewById(R.id.edt_com_name);
            EditText edt_address = (EditText)findViewById(R.id.edt_address);
            EditText edt_user_nick= (EditText)findViewById(R.id.edt_user_nick);
            cell_no = edt_cell_no.getText().toString();
            if(cell_no == null || cell_no.isEmpty()) {
                CommonUtil.showToast(this, "휴대폰번호를 입력해주세요.");
                return;
            }
            user_pwd = edt_user_pwd.getText().toString();
            if(user_pwd == null || user_pwd.isEmpty()) {
                CommonUtil.showToast(this, "비밀번호를 입력해주세요.");
                return;
            }
            user_name = edt_user_name.getText().toString();
            if(user_name == null || user_name.isEmpty()) {
                CommonUtil.showToast(this, "이름을 입력해주세요.");
                return;
            }
            com_name = edt_com_name.getText().toString();
            if(com_name == null || com_name.isEmpty()) {
                CommonUtil.showToast(this, "업체명을 입력해주세요.");
                return;
            }
            address = edt_address.getText().toString();
            if(address == null || address.isEmpty()) {
                CommonUtil.showToast(this, "주소를 입력해주세요.");
                return;
            }
            user_nick = edt_user_nick.getText().toString();
            if(user_nick == null || user_nick.isEmpty()) {
                CommonUtil.showToast(this, "닉네임을 입력해주세요.");
                return;
            }


            StringBuffer sb = new StringBuffer();
            sb.append("휴대폰번호 : ").append("\t").append(cell_no).append("\n");
            sb.append("이름 : ").append("\t").append(user_name).append("\n");
            sb.append("닉네임 : ").append("\t").append(user_nick).append("\n");
            sb.append("업체명 : ").append("\t").append(com_name).append("\n");
            sb.append("주소 : ").append("\t").append(address).append("\n").append("\n");
            sb.append("위 정보가 맞습니까?").append("\n").append("휴대폰번호는 나중에 변경하실 수 없습니다.");
            showConfirmDialog("회원가입" , sb.toString());

        }
    }

    private void showConfirmDialog(String title, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registerUserAPI();
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

    private void registerUserAPI() {

        UserInfoModel userModel = new UserInfoModel();
        userModel.setCell_no(cell_no);
        userModel.setUser_pwd(user_pwd);
        userModel.setAddress(address);
        userModel.setUser_name(user_name);
        userModel.setUser_nick(user_nick);
        if(user_type.equals("1")) {
            userModel.setUser_type("1");
            userModel.setUser_able_job(user_able_job);
            userModel.setUser_nick(user_nick);
            userModel.setUser_bir_year(bir_year);
            userModel.setUser_bir_month(bir_month);
            userModel.setUser_bir_day(bir_day);
        } else {
            userModel.setUser_type("2");
            userModel.setCom_name(com_name);
        }

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.registerUser(userModel);

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
                        registerSuccess();
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

    private void goMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
         } else {
           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }

    private void setUserPreference() {
        SharedPreferences mPref = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(CELL_NO , cell_no);
        editor.putString(ADDRESS , address);
        editor.putString(USER_PWD , user_pwd);
        editor.putString(USER_TYPE , user_type);
        editor.putString(USER_NICK , user_nick);
        editor.putString(USER_NAME , user_name);
        editor.putString(USER_BIR_YEAR , bir_year);
        editor.putString(USER_BIR_MONTH , bir_month);
        editor.putString(USER_BIR_DAY , bir_day);
        editor.putString(USER_ABLE_JOB , user_able_job);
        editor.commit();

    }

    private void registerSuccess() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("성공");
        alertDialogBuilder.setMessage("회원가입을 환영합니다.");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setUserPreference();
                goMainActivity();
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }
    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }
}
