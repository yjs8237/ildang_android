package com.jscompany.ildang.advilgam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.AdverAdapter;
import com.jscompany.ildang.listview.MyAdverAdapter;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdvList extends AppCompatActivity implements  View.OnClickListener , AdapterView.OnItemClickListener {


    private ListView listView;
    private ProgressDialog progressDialog;

    private MyAdverAdapter adverAdapter;

    private ArrayList<AdverModel> adver_list;

    private String job_type;
    private String job_type_str;
    private String loc_gu;
    private String loc_gu_str;
    private String loc_dong;
    private String loc_dong_str;

    private TextView empty_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_adv_list);

        ImageButton back_img_btn = (ImageButton) findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        empty_view = (TextView) findViewById(R.id.empty_list);
        listView = (ListView)findViewById(R.id.adver_list_view);
        adverAdapter = new MyAdverAdapter();

        listView.setOnItemClickListener(this);
        doWork();

    }

    private void doWork() {

        adver_list = new ArrayList<>();

        AdverModel model = new AdverModel();
        model.setCell_no(CommonUtil.getUserInfo(this).getCell_no());

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.mylist(model);
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
                        JsonArray jsonArr = jsonObj.getAsJsonArray("list");

                        if(jsonArr.size() == 0) {
                            listView.setEmptyView(empty_view);
                        } else {
                            for (int i=0; i<jsonArr.size(); i++) {
                                AdverModel tempModel = new AdverModel();
                                JsonObject tempJson = (JsonObject) jsonArr.get(i);
                                tempModel.setAd_seq(tempJson.get("ad_seq").getAsLong());
                                tempModel.setTitle(tempJson.get("title").getAsString());
                                tempModel.setReg_date(tempJson.get("reg_date").getAsString());
                                tempModel.setType_str(tempJson.get("type_str").getAsString());
                                adver_list.add(tempModel);
                                adverAdapter.addItem( tempModel);
                            }
                        }

                        listView.setAdapter(adverAdapter);
                        adverAdapter.notifyDataSetChanged();

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

    private void backActivity() {
        super.onBackPressed();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AdverModel model = (AdverModel) adverAdapter.getItem(position);

        Intent intent = new Intent(MyAdvList.this , MyAdvDetail.class);
        intent.putExtra("ad_seq" , ((AdverModel) adverAdapter.getItem(position)).getAd_seq());
        startActivity(intent);
    }

    private void showConfirmDialog(String title, String message , final IldangModel model , final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                requestIldangMatch(model , position);
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
}
