package com.jscompany.ildang.ildangregister;

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
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;
import com.jscompany.ildang.userlist.UserListActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterIldangLocation extends AppCompatActivity implements  View.OnClickListener{

    private String job_type;
    private String job_type_str;
    private String loc_gu;
    private String loc_gu_str;
    private String loc_dong;
    private String loc_dong_str;

    private ArrayList<IldangModel> gu_list;

    private ProgressDialog progressDialog;



    TextView tv_ad_title;
    TextView tv_ad_location;
    TextView tv_ad_contact;
    TextView tv_ad_content;
    TextView tv_ad_seq;
    TextView tv_ad_com_name;
    LinearLayout adv_linear;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ildang_regi_location);

        ImageButton back_img_btn =(ImageButton) findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            job_type = intent.getExtras().getString("job_type" , "none");
            loc_gu = intent.getExtras().getString("loc_gu" , "none");
            job_type_str = intent.getExtras().getString("job_type_str" , "none");
            loc_gu_str = intent.getExtras().getString("loc_gu_str" , "none");
        }

        tv_ad_title =(TextView) findViewById(R.id.tv_ad_title);
        tv_ad_location =(TextView) findViewById(R.id.tv_ad_location);
        tv_ad_contact =(TextView) findViewById(R.id.tv_ad_contact);
        tv_ad_content =(TextView) findViewById(R.id.tv_ad_content);
        tv_ad_com_name =(TextView) findViewById(R.id.tv_ad_com_name);
        tv_ad_seq =(TextView) findViewById(R.id.tv_ad_seq);
        adv_linear = (LinearLayout)findViewById(R.id.adv_linear);
        adv_linear.setOnClickListener(this);

        settingAdver();

        gu_list = new ArrayList<>();
        // 해당 작업의 구 리스트 가져오기
        IldangModel ildangModel = new IldangModel();
        ildangModel.setJob_type(job_type);
        ildangModel.setSearch_type("1");
        ildangModel.setLoc_gu(loc_gu);
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
                            tempModel.setLoc_gu_str(tempJson.get("loc_gu_str").getAsString());
                            tempModel.setIldang_count(tempJson.get("ildang_count").getAsInt());
                            gu_list.add(tempModel);
                        }

                    } else {
                        // 실패
                        showDialogMessage("실패" , "데이터가 존재하지 않습니다.");
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
                    }

                } catch (Exception e) {
                    showDialogMessage("Exception" , e.getLocalizedMessage());
                    Log.d("Restapi" , "api : " + e.getLocalizedMessage());
                } finally {
                    setCreateView(); // View 만들기
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



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("일당등록");
        alertDialogBuilder.setMessage("프로필을 등록하실 지역을 선택해주세요");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();




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



    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }



    private void setCreateView() {

        LinearLayout linear = (LinearLayout)findViewById(R.id.base_layout);

        int totalCount = gu_list.size() / 2;
        totalCount += gu_list.size() % 2;

        int subCount = 0;
        Log.d("Restapi" , "api : " + "total count : " + totalCount);

        for (int i=0; i< totalCount; i++) {

            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 5, 0, 0);
            ll.setLayoutParams(params);

            for (int j=subCount; j<gu_list.size(); j++) {
                final IldangModel textCode = gu_list.get(j);

                Button tempBtn = new Button(this);
                tempBtn.setText(textCode.getLoc_gu_str());
                tempBtn.setTypeface(null, Typeface.BOLD);
                tempBtn.setTextSize(20);
                LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
                btn_params.weight=1;
                btn_params.setMargins(convertDpToPixel(10),convertDpToPixel(10),convertDpToPixel(10), convertDpToPixel(10));
                tempBtn.setLayoutParams(btn_params);
                tempBtn.setWidth(convertDpToPixel(50));
                tempBtn.setHeight(convertDpToPixel(80));
                tempBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.button));
                tempBtn.setHint(textCode.getLoc_gu());


                tempBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loc_dong = textCode.getLoc_dong();   // 동의 코드값 세팅
                        loc_dong_str = textCode.getLoc_dong_str();
                        loc_gu = textCode.getLoc_gu();
                        loc_gu_str = textCode.getLoc_gu_str();
                        goIntent();
                        //CommonUtil.showToast(getApplicationContext() , job_type + " , " + loc_gu + " , " + textCode.getCd_value());
                    }
                });

                ll.addView(tempBtn);
                subCount++;

                if(subCount % 2 ==0) {
                    break;
                }

            }


            if(subCount == gu_list.size()) {
                // 마지막 카운트의 버튼을 만들고
                if(gu_list.size() % 2 != 0) {
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


    private void goIntent(){
        Intent intent = new Intent(this , RegisterIldangLocDong.class);
        intent.putExtra("job_type"  , job_type);
        intent.putExtra("loc_gu"  , loc_gu);
        intent.putExtra("job_type_str"  , job_type_str);
        intent.putExtra("loc_gu_str"  , loc_gu_str);
        startActivity(intent);
    }

    public int convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int)px;
    }


    private void showAlertDialog(String title , String message , boolean isNegativeButton) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ildangRegister();
                Intent intent = new Intent(RegisterIldangLocation.this , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        if(isNegativeButton) {
            alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void backActivity() {
        super.onBackPressed();
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
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L001");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "중랑구");
                startActivity(intent);
                break;
            case R.id.button1_2 :
                CommonUtil.showToast(this , "성북구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L002");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "성북구");
                startActivity(intent);
                break;
            case R.id.button2_1 :
                CommonUtil.showToast(this , "동대문구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L003");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "동대문구");
                startActivity(intent);
                break;
            case R.id.button2_2 :
                CommonUtil.showToast(this , "노원구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L004");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "노원구");
                startActivity(intent);
                break;
            case R.id.button3_1 :
                CommonUtil.showToast(this , "광진구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L005");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "광진구");
                startActivity(intent);
                break;
            case R.id.button3_2 :
                CommonUtil.showToast(this , "금천구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L006");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "금천구");
                startActivity(intent);
                break;
            case R.id.button4_1 :
                CommonUtil.showToast(this , "도봉구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L007");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "도봉구");
                startActivity(intent);
                break;
            case R.id.button4_2 :
                CommonUtil.showToast(this , "종로구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L008");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "종로구");
                startActivity(intent);
                break;
            case R.id.button5_1 :
                CommonUtil.showToast(this , "중구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L009");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "중구");
                startActivity(intent);
                break;
            case R.id.button5_2 :
                CommonUtil.showToast(this , "용산구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L010");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "용산구");
                startActivity(intent);
                break;
            case R.id.button6_1 :
                CommonUtil.showToast(this , "성동구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L011");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "성동구");
                startActivity(intent);
                break;
            case R.id.button6_2 :
                CommonUtil.showToast(this , "강북구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L012");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "강북구");
                startActivity(intent);
                break;
            case R.id.button7_1 :
                CommonUtil.showToast(this , "은평구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L013");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "은평구");
                startActivity(intent);
                break;
            case R.id.button7_2 :
                CommonUtil.showToast(this , "서대문구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L014");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "서대문구");
                startActivity(intent);
                break;
            case R.id.button8_1 :
                CommonUtil.showToast(this , "마포구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L015");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "마포구");
                startActivity(intent);
                break;
            case R.id.button8_2 :
                CommonUtil.showToast(this , "양천구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L016");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "양천구");
                startActivity(intent);
                break;
            case R.id.button9_1 :
                CommonUtil.showToast(this , "강서구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L017");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "강서구");
                startActivity(intent);
                break;
            case R.id.button9_2 :
                CommonUtil.showToast(this , "구로구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L018");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "구로구");
                startActivity(intent);
                break;
            case R.id.button10_1 :
                CommonUtil.showToast(this , "영등포구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L019");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "영등포구");
                startActivity(intent);
                break;
            case R.id.button10_2 :
                CommonUtil.showToast(this , "동작구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L020");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "동작구");
                startActivity(intent);
                break;
            case R.id.button11_1 :
                CommonUtil.showToast(this , "서초구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L021");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "서초구");
                startActivity(intent);
                break;
            case R.id.button11_2 :
                CommonUtil.showToast(this , "강남구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L022");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "강남구");
                startActivity(intent);
                break;
            case R.id.button12_1 :
                CommonUtil.showToast(this , "관악구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L023");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "관악구");
                startActivity(intent);
                break;
            case R.id.button12_2 :
                CommonUtil.showToast(this , "송파구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L024");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "송파구");
                startActivity(intent);
                break;
            case R.id.button13_1 :
                CommonUtil.showToast(this , "강동구");
                intent = new Intent(this , RegisterIldangLocDong.class);
                intent.putExtra("job_type"  , job_type);
                intent.putExtra("loc_gu"  , "L025");
                intent.putExtra("job_type_str"  , job_type_str);
                intent.putExtra("loc_gu_str"  , "강동구");
                startActivity(intent);
                break;
            case R.id.back_img_btn:
//                CommonUtil.showToast(this , "뒤로가기");
                backActivity();
                break;
        }
    }


}
