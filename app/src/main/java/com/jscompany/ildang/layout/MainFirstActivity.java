package com.jscompany.ildang.layout;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.advilgam.AdvBadIldangMain;
import com.jscompany.ildang.advilgam.AdvBadOwnerMain;
import com.jscompany.ildang.advilgam.AdvDetail;
import com.jscompany.ildang.advilgam.AdvGuinMain;
import com.jscompany.ildang.advilgam.AdvIlgamList;
import com.jscompany.ildang.advilgam.AdvIlgamMain;
import com.jscompany.ildang.advilgam.AdvIlterMain;
import com.jscompany.ildang.fcmPush.FcmPushService;
import com.jscompany.ildang.ildanglist.LocationGu;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.preference.SettingActivity;
import com.jscompany.ildang.qna.QnaHistory;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFirstActivity extends Fragment implements View.OnClickListener{

    private View view;

    private ProgressDialog progressDialog;


    TextView tv_ad_title;
    TextView tv_ad_location;
    TextView tv_ad_contact;
    TextView tv_ad_content;
    TextView tv_ad_seq;
    TextView tv_ad_com_name;
    LinearLayout adv_linear;

    private int connect_count;


    private Button button1_1 ;
    private Button button1_2 ;
    private Button button2_1 ;
    private Button button2_2 ;
    private Button button3_1 ;
    private Button button3_2 ;
    private Button button4_1 ;
    private Button button4_2 ;
    private Button button5_1 ;
    private Button button5_2 ;
    private Button btn_menu;

    private DrawerLayout drawer;
    private Toolbar toolbar;


    @Override
    public void onStart() {
        super.onStart();
        Log.d("LOGIN" , "MainFirstActivity onStart");
        settingAdver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container , @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.first , container , false);

        // 방문자수 가져오기
        getConnectCount();

        // 광고 시퀀스 리스트 데이터 가져오기
//        getAdverList();

        drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        button1_1 = (Button)view.findViewById(R.id.button1_1);
        button1_2 = (Button)view.findViewById(R.id.button1_2);
        button2_1 = (Button)view.findViewById(R.id.button2_1);
        button2_2 = (Button)view.findViewById(R.id.button2_2);
        button3_1 = (Button)view.findViewById(R.id.button3_1);
        button3_2 = (Button)view.findViewById(R.id.button3_2);
        button4_1 = (Button)view.findViewById(R.id.button4_1);
        button4_2 = (Button)view.findViewById(R.id.button4_2);
        button5_1 = (Button)view.findViewById(R.id.button5_1);
        button5_2 = (Button)view.findViewById(R.id.button5_2);
        btn_menu = (Button)view.findViewById(R.id.btn_menu);
        tv_ad_title =(TextView) view.findViewById(R.id.tv_ad_title);
        tv_ad_location =(TextView) view.findViewById(R.id.tv_ad_location);
        tv_ad_contact =(TextView) view.findViewById(R.id.tv_ad_contact);
        tv_ad_content =(TextView) view.findViewById(R.id.tv_ad_content);
        tv_ad_com_name =(TextView) view.findViewById(R.id.tv_ad_com_name);
        tv_ad_seq =(TextView) view.findViewById(R.id.tv_ad_seq);
        adv_linear = (LinearLayout)view.findViewById(R.id.adv_linear);
        adv_linear.setOnClickListener(this);

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
        btn_menu.setOnClickListener(this);
        return view;

    }


    private void getAdverList() {

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.adverList();
        if(progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(getActivity(), CONST.progress_title, CONST.progress_body);
        }
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

                        } else {
                            CommonUtil.adverListInitial();
                            for (int i=0; i<jsonArr.size(); i++) {
                                JsonObject tempJson = (JsonObject) jsonArr.get(i);
                                AdverModel adverModel = new AdverModel();
                                adverModel.setAd_seq(tempJson.get("ad_seq").getAsLong());
                                adverModel.setTitle(tempJson.get("title").getAsString());
                                adverModel.setCom_name(tempJson.get("com_name").getAsString());
                                adverModel.setContact_num(tempJson.get("contact_num").getAsString());
                                adverModel.setLocation(tempJson.get("location").getAsString());
                                adverModel.setContent(tempJson.get("content").getAsString());
                                CommonUtil.addAdverModel(adverModel);
                            }
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
            tv_ad_seq.setText("");
            tv_ad_com_name.setText("");
            tv_ad_title.setText("");
            tv_ad_contact.setText("");
            tv_ad_location.setText("");
            tv_ad_content.setText("");
        }
    }

    private void getConnectCount() {
        UserInfoModel userModel = new UserInfoModel();
        SharedPreferences mPref = getActivity().getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        userModel.setCell_no(cell_no);

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.getConnectCount(userModel);
        if(progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = ProgressDialog.show(getActivity(), CONST.progress_title, CONST.progress_body);
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    Log.d("Restapi" , "api : " + response.body());

                    JsonObject jsonObj = response.body();
                    if(jsonObj.get("result").toString().equals("0")) {
                        // 성공
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
                        TextView  tv_connect_count = (TextView)view.findViewById(R.id.tv_connect_count);
                        tv_connect_count.setText("방문자 수 : " + jsonObj.get("count").getAsInt());
                        connect_count = jsonObj.get("count").getAsInt();

                        JsonArray jsonArr = jsonObj.getAsJsonArray("list");
                        for (int i=0; i<jsonArr.size(); i++) {
                            IldangModel tempModel = new IldangModel();
                            JsonObject tempJson = (JsonObject) jsonArr.get(i);
                            tempModel.setIldang_count(tempJson.get("ildang_count").getAsInt());
                            tempModel.setJob_type(tempJson.get("job_type").getAsString());
                            if(tempModel.getJob_type().equals("A001")) {
                                // 재단 , 아이롱
                                button1_1.setText(button1_1.getText() + "\n" + "(" + tempModel.getIldang_count() + ")");
                            } else if(tempModel.getJob_type().equals("A002")) {
                                // 미싱
                                button1_2.setText(button1_2.getText() + "\n" + "(" + tempModel.getIldang_count() + ")");
                            } else if(tempModel.getJob_type().equals("A003")) {
                                // 삼봉
                                button2_1.setText(button2_1.getText() + "\n" + "(" + tempModel.getIldang_count() + ")");
                            } else if(tempModel.getJob_type().equals("A004")) {
                                // 오바
                                button2_2.setText(button2_2.getText() + "\n" + "(" + tempModel.getIldang_count() + ")");
                            } else if(tempModel.getJob_type().equals("A005")) {
                                // 시다
                                button3_1.setText(button3_1.getText() + "\n" + "(" + tempModel.getIldang_count() + ")");
                            } else if(tempModel.getJob_type().equals("A006")) {
                                // 기타(특종)
                                button3_2.setText(button3_2.getText() + "\n" + "(" + tempModel.getIldang_count() + ")");
                            }

                        }

                        jsonArr = jsonObj.getAsJsonArray("adv_list");
                        int type_1_cnt = 0;
                        int type_2_cnt = 0;
                        int type_3_cnt = 0;
                        int type_4_cnt = 0;
                        int type_5_cnt = 0;
                        for (int i=0; i<jsonArr.size(); i++) {
                            AdverModel adverModel = new AdverModel();
                            JsonObject tempJson = (JsonObject) jsonArr.get(i);
                            adverModel.setType(tempJson.get("type").getAsString());
                            adverModel.setCount(tempJson.get("adv_count").getAsInt());
                            if(adverModel.getType().equals("1") || adverModel.getType().equals("2")) {
                                // 일감 일터
                                type_1_cnt = adverModel.getCount();
                            } else if(adverModel.getType().equals("3")) {
                                // 구인구직
                                type_3_cnt = adverModel.getCount();
                            } else if(adverModel.getType().equals("4")) {
                                // 악덕업주신고방
                                type_4_cnt = adverModel.getCount();
                            } else if(adverModel.getType().equals("5")) {
                                // 불성실일당 신고방
                                type_5_cnt = adverModel.getCount();
                            }
                        }

                        // 서버에서 카운트 개수를 가져오는데.. 광고 타입 데이터의 마스터성의 코드 데이터가 없어서..
                        // 이렇게 연산하여.. 카운트 표시함..
                        // 서버 코드테이블에 마스터 코드 데이터 세팅이..귀찮아서...

                        button4_1.setText(button4_1.getText() + "\n" + "(" + type_1_cnt + ")");
                        button4_2.setText(button4_2.getText() + "\n" + "(" + type_3_cnt + ")");
                        button5_1.setText(button5_1.getText() + "\n" + "(" + type_4_cnt + ")");
                        button5_2.setText(button5_2.getText() + "\n" + "(" + type_5_cnt + ")");


                    } else {
                        // 실패
                        showDialogMessage("실패" , jsonObj.get("description").toString());
                        Log.d("Restapi" , "api : " + jsonObj.get("result").toString());
                    }

                } catch (Exception e) {
                    showDialogMessage("Exception" , e.getLocalizedMessage());
                    Log.d("Restapi" , "api : " + e.getLocalizedMessage());
                } finally {
                    getAdverList();
                    //progressDialog.dismiss();
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
        CommonUtil.showDialog(getActivity(), "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(getActivity(), title , message);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button1_1 :
                intent = new Intent(getContext() , LocationGu.class);
                intent.putExtra("job_type", CONST.JOB_TYPE_A001);
                intent.putExtra("connect_count", connect_count);
                startActivity(intent);
                break;
            case R.id.button1_2 :
                intent = new Intent(getContext() , LocationGu.class);
                intent.putExtra("job_type", CONST.JOB_TYPE_A002);
                intent.putExtra("connect_count", connect_count);
                startActivity(intent);
                break;
            case R.id.button2_1 :
                intent = new Intent(getContext() , LocationGu.class);
                intent.putExtra("job_type", CONST.JOB_TYPE_A003);
                intent.putExtra("connect_count", connect_count);
                startActivity(intent);
                break;
            case R.id.button2_2 :
                intent = new Intent(getContext() , LocationGu.class);
                intent.putExtra("job_type", CONST.JOB_TYPE_A004);
                intent.putExtra("connect_count", connect_count);
                startActivity(intent);
                break;
            case R.id.button3_1 :
                intent = new Intent(getContext() , LocationGu.class);
                intent.putExtra("job_type", CONST.JOB_TYPE_A005);
                intent.putExtra("connect_count", connect_count);
                startActivity(intent);
                break;
            case R.id.button3_2 :
                // 기타 (특종)
                intent = new Intent(getContext() , LocationGu.class);
                intent.putExtra("job_type", CONST.JOB_TYPE_A006);
                intent.putExtra("connect_count", connect_count);
                startActivity(intent);
                break;
            case R.id.button4_1 :
                // 일감 / 일터 나누기
                intent = new Intent(getContext() , AdvIlgamMain.class);
                startActivity(intent);
                break;
            case R.id.button4_2 :
                // 구인
                intent = new Intent(getContext() , AdvGuinMain.class);
                startActivity(intent);
                break;
            case R.id.button5_1 :
                // 악덕업주 신고방
                intent = new Intent(getContext() , AdvBadOwnerMain.class);
                startActivity(intent);
                break;
            case R.id.button5_2 :
                // 불성실 일당 신고방
                intent = new Intent(getContext() , AdvBadIldangMain.class);
                startActivity(intent);
                break;

            case R.id.adv_linear :
                if(tv_ad_seq.getText().toString() != null && !tv_ad_seq.getText().toString().isEmpty()) {
                    intent = new Intent(getContext(), AdvDetail.class);
                    intent.putExtra("ad_seq", Long.parseLong(tv_ad_seq.getText().toString()));
                    startActivity(intent);
                }
                break;

            case R.id.btn_menu:
//                intent = new Intent(getContext(), MainActivity.class);
//                intent.putExtra("param", "click");
//                startActivity(intent);

//                setSupportActionBar(toolbar);

//                drawer.openDrawer(drawer);

//                drawer.performClick();

//
//                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//                toggle.opend
//
//                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, this.getTheme());
//                toggle.setHomeAsUpIndicator(drawable);
//                toggle.setDrawerIndicatorEnabled(true);

                break;
        }

    }




}
