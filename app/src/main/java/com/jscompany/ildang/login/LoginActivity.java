package com.jscompany.ildang.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.ildangregister.RegisterIldangType;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.register.RegisterActivity;
import com.jscompany.ildang.register.RegisterYakgwan;
import com.jscompany.ildang.register.UserChoiceActivity;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jscompany.ildang.Common.USER_INFO.CELL_NO;
import static com.jscompany.ildang.Common.USER_INFO.USER_PWD;
import static com.jscompany.ildang.Common.USER_INFO.USER_TYPE;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private long pressedTime;

    private ProgressDialog progressDialog;

    private boolean loginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pressedTime = 0;

        Button loginBtn = (Button) findViewById(R.id.btn_login);
        Button registerBtn = (Button) findViewById(R.id.btn_register);
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login) {

            // 로그인 체크
            EditText edit_cell_no = (EditText) findViewById(R.id.edit_cell_no);
            EditText edit_password = (EditText) findViewById(R.id.edit_password);

            String cell_no = edit_cell_no.getText().toString();
            String password  =  edit_password.getText().toString();

            if(checkLogin(cell_no , password)) {
                restLogin(cell_no , password);
            }
        } else if(v.getId() == R.id.btn_register) {

            Intent intent = new Intent(LoginActivity.this , RegisterYakgwan.class);
            startActivity(intent);

        }
    }

    private boolean restLogin(String cell_no , String user_pwd) {

        final boolean[] result_bool = {false};

        final SharedPreferences mPref = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
        String token = mPref.getString(USER_INFO.TOKEN , "");

        final UserInfoModel userModel = new UserInfoModel();
        userModel.setCell_no(cell_no);
        userModel.setUser_pwd(user_pwd);
        if(!token.isEmpty()) {
            userModel.setToken(token);
        }

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.login(userModel);

        if(progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(this, CONST.progress_title, CONST.progress_body);
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String token = "";

                try {
                    Log.d("Restapi" , "api : " + response.body());

                    JsonObject jsonObj = response.body();
                    if(jsonObj.get("result").toString().equals("0")) {
                        // 성공
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());

                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putString(CELL_NO , userModel.getCell_no());
                        editor.putString(USER_PWD , userModel.getUser_pwd());
                        editor.putString(USER_INFO.USER_TYPE , jsonObj.get("user_type").getAsString());
                        editor.putString(USER_INFO.USER_NICK , jsonObj.get("user_nick").getAsString());
                        editor.putString(USER_INFO.ADDRESS , jsonObj.get("address").getAsString());
                        editor.putString(USER_INFO.USER_POINT , jsonObj.get("user_point").getAsString());
                        editor.putString(USER_INFO.USER_NAME , jsonObj.get("user_name").getAsString());
                        editor.putString(USER_INFO.PUSH_YN , "Y");

                        String user_bir_year = jsonObj.get("user_bir_year").isJsonNull() ? "" : jsonObj.get("user_bir_year").getAsString();
                        editor.putString(USER_INFO.USER_BIR_YEAR , user_bir_year);

                        String user_bir_month = jsonObj.get("user_bir_month").isJsonNull() ? "" : jsonObj.get("user_bir_month").getAsString();
                        editor.putString(USER_INFO.USER_BIR_MONTH , user_bir_month);

                        String user_bir_day = jsonObj.get("user_bir_day").isJsonNull() ? "" : jsonObj.get("user_bir_day").getAsString();
                        editor.putString(USER_INFO.USER_BIR_DAY , user_bir_day);

                        String user_able_job = jsonObj.get("user_able_job").isJsonNull() ? "" : jsonObj.get("user_able_job").getAsString();
                        editor.putString(USER_INFO.USER_ABLE_JOB , user_able_job);
                        editor.commit();

                        // 로그인하면 푸쉬알람 받기 설정을 true 로 강제세팅
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        editor = pref.edit();
                        editor.putBoolean("ring_push", true);
                        editor.commit();




                        //finish();
                        goMainActivity();
                        loginSuccess = true;
                        result_bool[0] = true;
                    } else {
                        // 실패
                        showDialogMessage("실패" , jsonObj.get("description").toString());
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
                        result_bool[0] = false;
                    }

                } catch (Exception e) {
                    showDialogMessage("Exception" , e.getLocalizedMessage());
                    Log.d("Restapi" , "api : " + e.getLocalizedMessage());
                } finally {
                    if(!token.equals("") && !token.isEmpty()) {
                        userModel.setToken(token);
//                        updateToken(userModel);
                    } else {
                        progressDialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Restapi" , "api : " + "fail!!! " + t.getLocalizedMessage());
                progressDialog.dismiss();
                showNetworkError();
            }
        });

        return result_bool[0];
    }



    private void updateToken(UserInfoModel userInfoModel) {
        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.token(userInfoModel);

        if(progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(this, CONST.progress_title, CONST.progress_body);
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                String token = "";

                try {
                    Log.d("Restapi" , "api : " + response.body());

                    JsonObject jsonObj = response.body();
                    if(jsonObj.get("result").toString().equals("0")) {
                        // 성공
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());


                    } else {
                        // 실패
                        showDialogMessage("실패" , jsonObj.get("description").toString());
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
                    }

                } catch (Exception e) {
                    showDialogMessage("Exception" , e.getLocalizedMessage());
                    Log.d("Restapi" , "api : " + e.getLocalizedMessage());
                } finally {
                    if(progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
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
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        startActivity(intent);
    }


    private boolean checkLogin(String cell_no , String password) {


        if(cell_no == null || cell_no.isEmpty()) {
            showToast("휴대폰번호를 입력해주세요.");
            return false;
        }
        if(password == null || password.isEmpty()) {
            showToast("비밀번호를 입력해주세요.");
            return false;
        }
        return true;
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        /*
        if ( pressedTime == 0 ) {
            Toast.makeText(LoginActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if ( seconds > 2000 ) {
                Toast.makeText(LoginActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
                pressedTime = 0 ;
            }
            else {
//                super.onBackPressed();
                finishAffinity();
//                finish(); // app 종료 시키기
            }
        }
        */

    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext() , message , Toast.LENGTH_SHORT).show();
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }
    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }


}
