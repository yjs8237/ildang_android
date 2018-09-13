package com.jscompany.ildang.ildanglist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.advilgam.AdvDetail;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.CodeModel;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;
import com.jscompany.ildang.userlist.UserListActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationDong extends AppCompatActivity implements  View.OnClickListener{

    private String job_type;
    private String job_type_str;
    private String loc_gu;
    private String loc_gu_str;
    private String loc_dong;
    private String loc_dong_str;

    private ArrayList<IldangModel> dong_list;

    private ProgressDialog progressDialog;

    TextView tv_ad_title;
    TextView tv_ad_location;
    TextView tv_ad_contact;
    TextView tv_ad_content;

    TextView tv_ad_seq;
    TextView tv_ad_com_name;
    LinearLayout adv_linear;
    private int connect_count;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loc_gu_l001);

        tv_ad_title =(TextView) findViewById(R.id.tv_ad_title);
        tv_ad_location =(TextView) findViewById(R.id.tv_ad_location);
        tv_ad_contact =(TextView) findViewById(R.id.tv_ad_contact);
        tv_ad_content =(TextView) findViewById(R.id.tv_ad_content);
        tv_ad_com_name =(TextView) findViewById(R.id.tv_ad_com_name);
        tv_ad_seq =(TextView) findViewById(R.id.tv_ad_seq);
        adv_linear = (LinearLayout)findViewById(R.id.adv_linear);
        adv_linear.setOnClickListener(this);

        ImageButton back_img_btn =(ImageButton) findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            job_type = intent.getExtras().getString("job_type" , "none");
            loc_gu = intent.getExtras().getString("loc_gu" , "none");
            job_type_str = intent.getExtras().getString("job_type_str" , "none");
            loc_gu_str = intent.getExtras().getString("loc_gu_str" , "none");
            connect_count = intent.getExtras().getInt("connect_count");
            TextView tv_connect_count = (TextView)findViewById(R.id.tv_connect_count);
            tv_connect_count.setText("방문자 수 : " + connect_count);
        }

        dong_list = new ArrayList<>();
        // 해당 구의 동 리스트 가져오기
        IldangModel ildangModel = new IldangModel();
        ildangModel.setSearch_type("2");
        ildangModel.setLoc_gu(loc_gu);
        ildangModel.setJob_type(job_type);
        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.selectIldangCount(ildangModel);
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
                        for (int i=0; i<jsonArr.size(); i++) {
                            IldangModel tempModel = new IldangModel();
                            JsonObject tempJson = (JsonObject) jsonArr.get(i);
                            tempModel.setLoc_gu(tempJson.get("loc_gu").getAsString());
                            tempModel.setLoc_dong(tempJson.get("loc_dong").getAsString());
                            tempModel.setLoc_dong_str(tempJson.get("loc_dong_str").getAsString());
                            tempModel.setIldang_count(tempJson.get("ildang_count").getAsInt());
                            dong_list.add(tempModel);
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
                    setCreateView(); // View 만들기
                    settingAdver();
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


    private void settingAdver() {
        AdverModel adverModel = CommonUtil.getAdverModel();
        if(adverModel != null) {
            tv_ad_seq.setText(String.valueOf(adverModel.getAd_seq()));
            tv_ad_com_name.setText(adverModel.getCom_name());
            tv_ad_title.setText(adverModel.getTitle());
            tv_ad_contact.setText(CommonUtil.cell_number(adverModel.getContact_num()));
            tv_ad_location.setText(adverModel.getLocation());
            String content = adverModel.getContent();
            if(content.length() > 28) {
                content = content.substring(0,28) + "....";
            }
            tv_ad_content.setText(content);
        } else {
        }
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
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_img_btn:
                backActivity();
                break;

            case R.id.adv_linear:
                if(tv_ad_seq.getText().toString() != null && !tv_ad_seq.getText().toString().isEmpty()) {
                    intent = new Intent(this , AdvDetail.class);
                    intent.putExtra("ad_seq" , Long.parseLong(tv_ad_seq.getText().toString()));
                    startActivity(intent);
                }
                break;
        }
    }


    private void setCreateView() {

        LinearLayout linear = (LinearLayout)findViewById(R.id.base_layout);

        int totalCount = dong_list.size() / 2;
        totalCount += dong_list.size() % 2;

        int subCount = 0;
        Log.d("Restapi" , "api : " + "total count : " + totalCount);

        for (int i=0; i< totalCount; i++) {

            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 5, 0, 0);
            ll.setLayoutParams(params);

            for (int j=subCount; j<dong_list.size(); j++) {
                final IldangModel textCode = dong_list.get(j);

                Button tempBtn = new Button(this);
                tempBtn.setText(textCode.getLoc_dong_str() + "\n" + "(" + textCode.getIldang_count() + ")");
                tempBtn.setTypeface(null, Typeface.BOLD);
                LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
                btn_params.weight=1;
                btn_params.setMargins(convertDpToPixel(10),convertDpToPixel(10),convertDpToPixel(10), convertDpToPixel(10));
                tempBtn.setLayoutParams(btn_params);
                tempBtn.setWidth(convertDpToPixel(50));
                tempBtn.setHeight(convertDpToPixel(80));
                tempBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.button));
                tempBtn.setHint(textCode.getLoc_dong());
                tempBtn.setTextSize(20);
                tempBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loc_dong = textCode.getLoc_dong();   // 동의 코드값 세팅
                        loc_dong_str = textCode.getLoc_dong_str();
//                        job_type = intent.getExtras().getString("job_type" , "none");
//                        loc_gu = intent.getExtras().getString("loc_gu" , "none");
//                        job_type_str = intent.getExtras().getString("job_type_str" , "none");
//                        loc_gu_str = intent.getExtras().getString("loc_gu_str" , "none");

                        goIntent(job_type , loc_gu, loc_gu_str,loc_dong , loc_dong_str);

                        //showConfirmDialog(job_type_str + "-" + loc_gu_str , textCode.getCd_value() + " 에 일당 등록하시겠습니까?");
                        //CommonUtil.showToast(getApplicationContext() , job_type + " , " + loc_gu + " , " + textCode.getCd_value());
                    }
                });

                ll.addView(tempBtn);
                subCount++;

                if(subCount % 2 ==0) {
                    break;
                }

            }

            if(subCount == dong_list.size()) {
                // 마지막 카운트의 버튼을 만들고
                if(dong_list.size() % 2 != 0) {
                    // 홀수의 건수일 경우 히든 버튼을 하나 삽입
                    Button tempBtn = new Button(this);
                    tempBtn.setText("");
                    tempBtn.setTypeface(null, Typeface.BOLD);
                    LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
                    btn_params.weight=1;
                    btn_params.setMargins(convertDpToPixel(10),convertDpToPixel(10),convertDpToPixel(10), convertDpToPixel(10));
                    tempBtn.setLayoutParams(btn_params);
                    tempBtn.setWidth(convertDpToPixel(50));
                    tempBtn.setHeight(convertDpToPixel(80));
                    tempBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.button));
                    tempBtn.setVisibility(View.INVISIBLE);
                    ll.addView(tempBtn);
                }
            }

            linear.addView(ll);
        }
    }

    private void goIntent(String job_type , String loc_gu , String loc_gu_str , String loc_dong , String loc_dong_str ) {
        Intent intent = new Intent(LocationDong.this , IldangList.class);
        intent.putExtra("job_type" , job_type);
        intent.putExtra("job_type_str" , job_type_str);
        intent.putExtra("loc_gu" , loc_gu);
        intent.putExtra("loc_gu_str" , loc_gu_str);
        intent.putExtra("loc_dong" , loc_dong);
        intent.putExtra("loc_dong_str" , loc_dong_str);
        intent.putExtra("connect_count" , connect_count);
        startActivity(intent);
    }

    public int convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int)px;
    }

    private void showConfirmDialog(String title, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //registerIldang();
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
