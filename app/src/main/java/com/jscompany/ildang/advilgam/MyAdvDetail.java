package com.jscompany.ildang.advilgam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdvDetail extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog progressDialog;

    private TextView tv_com_name;
    private TextView tv_cell_no;
    private TextView tv_address;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_type;
    private TextView tv_end_date;

    private long ad_seq;

    private Spinner spinner;
    private String adv_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_adv_detail);

        Intent intent = getIntent();
        ad_seq = intent.getExtras().getLong("ad_seq");

        tv_com_name = (TextView) findViewById(R.id.tv_com_name);
        tv_cell_no = (TextView) findViewById(R.id.tv_cell_no);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_end_date = (TextView) findViewById(R.id.tv_end_date);
        spinner = (Spinner) findViewById(R.id.spinner);

        Button btn_delete_adv = (Button)findViewById(R.id.btn_delete_adv);
        Button btn_modify_adv = (Button)findViewById(R.id.btn_modify_adv);
        Button btn_postpone_adv =(Button) findViewById(R.id.btn_postpone_adv);

        btn_delete_adv.setOnClickListener(this);
        btn_modify_adv.setOnClickListener(this);
        btn_postpone_adv.setOnClickListener(this);
//        tv_cell_no.setOnClickListener(this);


        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);


        doWork();

        /* 버튼 리스너 */
//        Button obtain_btn = (Button) findViewById(R.id.obtain_reg_btn);
//        Button order_btn = (Button) findViewById(R.id.order_reg_btn);
//        obtain_btn.setOnClickListener(this);
//        order_btn.setOnClickListener(this);
        /* ---------------------- */
    }


    private void doWork() {

        AdverModel model = new AdverModel();
        model.setAd_seq(ad_seq);
        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.adverDetail(model);
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
                        tv_type.setText(jsonObj.get("type_str").getAsString());
                        tv_title.setText(jsonObj.get("title").getAsString());
                        tv_address.setText(jsonObj.get("location").getAsString());
                        tv_cell_no.setText(CommonUtil.cell_number(jsonObj.get("contact_num").getAsString()));
                        tv_com_name.setText(jsonObj.get("com_name").getAsString());
                        tv_content.setText(jsonObj.get("content").getAsString());
                        tv_end_date.setText(jsonObj.get("end_date").getAsString());
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


    private void deleteMyAdver() {
        AdverModel model = new AdverModel();
        model.setAd_seq(ad_seq);
        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.deleteAdver(model);
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
                        CommonUtil.removeAdverModel(ad_seq);
                        Intent intent = new Intent(MyAdvDetail.this , MyAdvList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    private void modifyMyAdver() {
        AdverModel model = new AdverModel();
        model.setAd_seq(ad_seq);
        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.deleteAdver(model);
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
                        CommonUtil.removeAdverModel(ad_seq);
                        Intent intent = new Intent(MyAdvDetail.this , MyAdvList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
            case R.id.back_img_btn :
                backActivity();
                break;

            case R.id.btn_delete_adv :
                showConfirmDialog("광고삭제" , "해당 광고를 정말로 삭제하시겠습니까?" , "delete");
                break;

            case R.id.btn_modify_adv:
                Intent intent = new Intent(MyAdvDetail.this , MyAdvModify.class);
                Log.d("Restapi" , "tv_com_name : " + tv_com_name.getText());
                intent.putExtra("ad_seq" , ad_seq);
                intent.putExtra("com_name" , tv_com_name.getText());
                intent.putExtra("contact_num" , tv_cell_no.getText());
                intent.putExtra("address" , tv_address.getText());
                intent.putExtra("title" , tv_title.getText());
                intent.putExtra("content" , tv_content.getText());
                startActivity(intent);
//                showConfirmDialog("광고수정" , "해당 광고를 수정하시겠습니까?" , "modify");
                break;

            case R.id.btn_postpone_adv:
                adv_day = spinner.getSelectedItem().toString();
                adv_day = adv_day.replaceAll("일","");

                showPostPoneDialog("광고연장" , adv_day + "일 광고를 연장 하시겠습니까? \n " + 2000 * Integer.parseInt(adv_day)  + "캐쉬가 차감됩니다.");
//                showPostPoneDialog("광고연장" , "광고연장 일 수를 선택하여주세요");
                break;
        }
    }

    private void showPostPoneDialog(String title , String message ) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reqPostPoneAdv(adv_day , ad_seq);
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

    private void reqPostPoneAdv(String adv_day , long ad_seq) {
        SharedPreferences mPrefs = getSharedPreferences("USER_INFO" , MODE_PRIVATE);


        AdverModel model = new AdverModel();
        model.setAd_seq(ad_seq);
        model.setAdv_days(Integer.parseInt(adv_day));
        model.setCell_no(mPrefs.getString(USER_INFO.CELL_NO, "none"));

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.postpone(model);
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
                        CommonUtil.showDialog(MyAdvDetail.this,"성공", "광고 연장 완료되었습니다.");
                        tv_end_date.setText(jsonObj.get("end_date").getAsString());
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


    private void showConfirmDialog(String title, String message , final String type ) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type.equals("delete")){
                    deleteMyAdver();
                } else {
                   Intent intent = new Intent(MyAdvDetail.this , MyAdvModify.class);
                    Log.d("Restapi" , "tv_com_name : " + tv_com_name.getText());
                   intent.putExtra("ad_seq" , ad_seq);
                    intent.putExtra("com_name" , tv_com_name.getText());
                    intent.putExtra("contact_num" , tv_cell_no.getText());
                    intent.putExtra("address" , tv_address.getText());
                    intent.putExtra("title" , tv_title.getText());
                    intent.putExtra("content" , tv_content.getText());
                    startActivity(intent);
                }
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
