package com.jscompany.ildang.ildanglist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.advilgam.AdvDetail;
import com.jscompany.ildang.fcmPush.FcmPushService;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.CodeModel;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;
import com.jscompany.ildang.userlist.UserListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationGu extends AppCompatActivity implements  View.OnClickListener{

    Button button1_1;
    Button button1_2;

    Button button2_1;
    Button button2_2;

    Button button3_1;
    Button button3_2;

    Button button4_1;
    Button button4_2;

    Button button5_1;
    Button button5_2;

    Button button6_1;
    Button button6_2;

    Button button7_1;
    Button button7_2;

    Button button8_1;
    Button button8_2;

    Button button9_1;
    Button button9_2;

    Button button10_1;
    Button button10_2;

    Button button11_1;
    Button button11_2;

    Button button12_1;
    Button button12_2;

    Button button13_1;



    private String job_type;

    private ProgressDialog progressDialog;

    private ArrayList<IldangModel> ildang_list;

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
        setContentView(R.layout.loc_gu);

        ImageButton back_img_btn =(ImageButton) findViewById(R.id.back_img_btn);

        tv_ad_title =(TextView) findViewById(R.id.tv_ad_title);
        tv_ad_location =(TextView) findViewById(R.id.tv_ad_location);
        tv_ad_contact =(TextView) findViewById(R.id.tv_ad_contact);
        tv_ad_content =(TextView) findViewById(R.id.tv_ad_content);
        tv_ad_com_name =(TextView) findViewById(R.id.tv_ad_com_name);
        tv_ad_seq =(TextView) findViewById(R.id.tv_ad_seq);
        adv_linear = (LinearLayout)findViewById(R.id.adv_linear);
        adv_linear.setOnClickListener(this);

        button1_1 = (Button)findViewById(R.id.button1_1);   // 중랑구
        button1_2 = (Button)findViewById(R.id.button1_2);   // 성북구

        button2_1 = (Button)findViewById(R.id.button2_1);   // 동대문구
         button2_2 = (Button)findViewById(R.id.button2_2);   // 노원구

         button3_1 = (Button)findViewById(R.id.button3_1);   // 광진구
         button3_2 = (Button)findViewById(R.id.button3_2);   // 금천구

         button4_1 = (Button)findViewById(R.id.button4_1);   // 도봉구
         button4_2 = (Button)findViewById(R.id.button4_2);   // 종로구

         button5_1 = (Button)findViewById(R.id.button5_1);   // 중구
         button5_2 = (Button)findViewById(R.id.button5_2);   // 용산구

         button6_1 = (Button)findViewById(R.id.button6_1);   // 성동구
         button6_2 = (Button)findViewById(R.id.button6_2);   // 강북구

         button7_1 = (Button)findViewById(R.id.button7_1);   // 은평구
         button7_2 = (Button)findViewById(R.id.button7_2);   // 서대문구

         button8_1 = (Button)findViewById(R.id.button8_1);   // 마포구
         button8_2 = (Button)findViewById(R.id.button8_2);   // 양천구

         button9_1 = (Button)findViewById(R.id.button9_1);   // 강서구
         button9_2 = (Button)findViewById(R.id.button9_2);   // 구로구

         button10_1 = (Button)findViewById(R.id.button10_1);   // 영등포구
         button10_2 = (Button)findViewById(R.id.button10_2);   // 동작구

         button11_1 = (Button)findViewById(R.id.button11_1);   // 서초구
         button11_2 = (Button)findViewById(R.id.button11_2);   // 강남구

         button12_1 = (Button)findViewById(R.id.button12_1);   // 관악구
         button12_2 = (Button)findViewById(R.id.button12_2);   // 송파구

         button13_1 = (Button)findViewById(R.id.button13_1);   // 강동구

        button1_1.setOnClickListener(this);
        button1_2.setOnClickListener(this);
        button2_1.setOnClickListener(this);
        button2_2.setOnClickListener(this);
        button3_1.setOnClickListener(this);
        button3_2.setOnClickListener(this);
        button4_1.setOnClickListener(this);
        button4_2.setOnClickListener(this);
        button5_1.setOnClickListener(this);
        button5_2.setOnClickListener(this);
        button6_1.setOnClickListener(this);
        button6_2.setOnClickListener(this);
        button7_1.setOnClickListener(this);
        button7_2.setOnClickListener(this);
        button8_1.setOnClickListener(this);
        button8_2.setOnClickListener(this);
        button9_1.setOnClickListener(this);
        button9_2.setOnClickListener(this);
        button10_1.setOnClickListener(this);
        button10_2.setOnClickListener(this);
        button11_1.setOnClickListener(this);
        button11_2.setOnClickListener(this);
        button12_1.setOnClickListener(this);
        button12_2.setOnClickListener(this);
        button13_1.setOnClickListener(this);
        back_img_btn.setOnClickListener(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            job_type = intent.getExtras().getString("job_type" , "none");
            connect_count = intent.getExtras().getInt("connect_count");
            TextView tv_connect_count = (TextView)findViewById(R.id.tv_connect_count);
            tv_connect_count.setText("방문자 수 : " + connect_count);
        } else {
            job_type = "none";
        }

        IldangModel ildangModel = new IldangModel();
        ildangModel.setSearch_type("1");    // 구 목록 조회
        ildangModel.setJob_type(job_type);

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.selectIldangCount(ildangModel);
        if(progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(this, CONST.progress_title, CONST.progress_body);
        }
        ildang_list = new ArrayList<>();
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
                            tempModel.setLoc_gu_str(tempJson.get("loc_gu_str").getAsString());
                            tempModel.setIldang_count(tempJson.get("ildang_count").getAsInt());
                            ildang_list.add(tempModel);
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
                    setViewCount();
                    // 광고내용 가져오기
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
            tv_ad_contact.setText(adverModel.getContact_num());
            tv_ad_location.setText(adverModel.getLocation());
            String content = adverModel.getContent();
            if(content.length() > 28) {
                content = content.substring(0,28) + "....";
            }
            tv_ad_content.setText(content);
        } else {
        }
    }

    private void setViewCount() {

        for(int i=0; i<ildang_list.size(); i++) {

            String loc_gu_str = ildang_list.get(i).getLoc_gu_str();
            int count = ildang_list.get(i).getIldang_count();
            if(loc_gu_str.equals( button1_1.getText())) {
                button1_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button1_2.getText())) {
                button1_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button2_1.getText())) {
                button2_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button2_2.getText())) {
                button2_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button3_1.getText())) {
                button3_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button3_2.getText())) {
                button3_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button4_1.getText())) {
                button4_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button4_2.getText())) {
                button4_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button5_1.getText())) {
                button5_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button5_2.getText())) {
                button5_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button6_1.getText())) {
                button6_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button6_2.getText())) {
                button6_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button7_1.getText())) {
                button7_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button7_2.getText())) {
                button7_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button8_1.getText())) {
                button8_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button8_2.getText())) {
                button8_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button9_1.getText())) {
                button9_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button9_2.getText())) {
                button9_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button10_1.getText())) {
                button10_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button10_2.getText())) {
                button10_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button11_1.getText())) {
                button11_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button11_2.getText())) {
                button11_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button12_1.getText())) {
                button12_1.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button12_2.getText())) {
                button12_2.setText(loc_gu_str + "\n" + "("+count+")");
            } else if(loc_gu_str.equals( button13_1.getText())) {
                button13_1.setText(loc_gu_str + "\n" + "("+count+")");
            }

        }


    }


    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }
    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void goIntent(String loc_gu) {
        Intent intent = null;
        intent = new Intent(this, LocationDong.class);
        intent.putExtra("job_type" , job_type);
        intent.putExtra("loc_gu" , loc_gu);
        intent.putExtra("connect_count" , connect_count);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.adv_linear:
                if(tv_ad_seq.getText().toString() != null && !tv_ad_seq.getText().toString().isEmpty()) {
                    intent = new Intent(this , AdvDetail.class);
                    intent.putExtra("ad_seq" , Long.parseLong(tv_ad_seq.getText().toString()));
                    startActivity(intent);
                }

                break;

            case R.id.button1_1 :
                CommonUtil.showToast(this , "중랑구");
                goIntent(CONST.LOC_GU_L001);
                break;
            case R.id.button1_2 :
                CommonUtil.showToast(this , "성북구");
                goIntent(CONST.LOC_GU_L002);
                break;
            case R.id.button2_1 :
                CommonUtil.showToast(this , "동대문구");
                goIntent(CONST.LOC_GU_L003);
                break;
            case R.id.button2_2 :
                CommonUtil.showToast(this , "노원구");
                goIntent(CONST.LOC_GU_L004);
                break;
            case R.id.button3_1 :
                CommonUtil.showToast(this , "광진구");
                goIntent(CONST.LOC_GU_L005);
                break;
            case R.id.button3_2 :
                CommonUtil.showToast(this , "금천구");
                goIntent(CONST.LOC_GU_L006);
                break;
            case R.id.button4_1 :
                CommonUtil.showToast(this , "도봉구");
                goIntent(CONST.LOC_GU_L007);
                break;
            case R.id.button4_2 :
                CommonUtil.showToast(this , "종로구");
                goIntent(CONST.LOC_GU_L008);
                break;
            case R.id.button5_1 :
                CommonUtil.showToast(this , "중구");
                goIntent(CONST.LOC_GU_L009);
                break;
            case R.id.button5_2 :
                CommonUtil.showToast(this , "용산구");
                goIntent(CONST.LOC_GU_L010);
                break;
            case R.id.button6_1 :
                CommonUtil.showToast(this , "성동구");
                goIntent(CONST.LOC_GU_L011);
                break;
            case R.id.button6_2 :
                CommonUtil.showToast(this , "강북구");
                goIntent(CONST.LOC_GU_L012);
                break;
            case R.id.button7_1 :
                CommonUtil.showToast(this , "은평구");
                goIntent(CONST.LOC_GU_L013);
                break;
            case R.id.button7_2 :
                CommonUtil.showToast(this , "서대문구");
                goIntent(CONST.LOC_GU_L014);
                break;
            case R.id.button8_1 :
                CommonUtil.showToast(this , "마포구");
                goIntent(CONST.LOC_GU_L015);
                break;
            case R.id.button8_2 :
                CommonUtil.showToast(this , "양천구");
                goIntent(CONST.LOC_GU_L016);
                break;
            case R.id.button9_1 :
                CommonUtil.showToast(this , "강서구");
                goIntent(CONST.LOC_GU_L017);
                break;
            case R.id.button9_2 :
                CommonUtil.showToast(this , "구로구");
                goIntent(CONST.LOC_GU_L018);
                break;
            case R.id.button10_1 :
                CommonUtil.showToast(this , "영등포구");
                goIntent(CONST.LOC_GU_L019);
                break;
            case R.id.button10_2 :
                CommonUtil.showToast(this , "동작구");
                goIntent(CONST.LOC_GU_L020);
                break;
            case R.id.button11_1 :
                CommonUtil.showToast(this , "서초구");
                goIntent(CONST.LOC_GU_L021);
                break;
            case R.id.button11_2 :
                CommonUtil.showToast(this , "강남구");
                goIntent(CONST.LOC_GU_L022);
                break;
            case R.id.button12_1 :
                CommonUtil.showToast(this , "관악구");
                goIntent(CONST.LOC_GU_L023);
                break;
            case R.id.button12_2 :
                CommonUtil.showToast(this , "송파구");
                goIntent(CONST.LOC_GU_L024);
                break;
            case R.id.button13_1 :
                CommonUtil.showToast(this , "강동구");
                goIntent(CONST.LOC_GU_L025);
                break;
            case R.id.back_img_btn:
                CommonUtil.showToast(this , "뒤로가기");
                backActivity();
                break;
        }
    }

    private void backActivity() {
        super.onBackPressed();
    }
}
