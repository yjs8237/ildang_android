package com.jscompany.ildang.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.common.internal.service.Common;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.advilgam.MyAdvList;
import com.jscompany.ildang.guide.GuideMain;
import com.jscompany.ildang.ildanghistory.IldangHistory;
import com.jscompany.ildang.ildangregister.RegisterIldangType;
import com.jscompany.ildang.login.LoginActivity;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.myinformation.ChangeMyInfoActivity;
import com.jscompany.ildang.notification.NotificationMain;
import com.jscompany.ildang.point.PointCharge;
import com.jscompany.ildang.point.PointHistory;
import com.jscompany.ildang.preference.SettingPreference;
import com.jscompany.ildang.qna.QnaHistory;
import com.jscompany.ildang.qna.QnaRequest;

import static com.jscompany.ildang.Common.USER_INFO.ADDRESS;
import static com.jscompany.ildang.Common.USER_INFO.CELL_NO;
import static com.jscompany.ildang.Common.USER_INFO.PUSH_YN;
import static com.jscompany.ildang.Common.USER_INFO.USER_ABLE_JOB;
import static com.jscompany.ildang.Common.USER_INFO.USER_BIR_DAY;
import static com.jscompany.ildang.Common.USER_INFO.USER_BIR_MONTH;
import static com.jscompany.ildang.Common.USER_INFO.USER_BIR_YEAR;
import static com.jscompany.ildang.Common.USER_INFO.USER_NAME;
import static com.jscompany.ildang.Common.USER_INFO.USER_PWD;
import static com.jscompany.ildang.Common.USER_INFO.USER_TYPE;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_menu_way;
    private Button btn_menu_regildang;
    private Button btn_menu_ildanghist;
    private Button btn_menu_myadvhist;
    private Button btn_menu_noti;
    private Button btn_menu_myinfo;
    private Button btn_menu_point;
    private Button btn_menu_pointhist;
    private Button btn_menu_qna;
    private Button btn_menu_qnahist;
    private Button btn_menu_setting;
    private Button btn_menu_logout;
    private Button btn_menu_registeruser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        btn_menu_way = findViewById(R.id.btn_menu_way);
        btn_menu_regildang = findViewById(R.id.btn_menu_regildang);
        btn_menu_ildanghist = findViewById(R.id.btn_menu_ildanghist);
        btn_menu_myadvhist = findViewById(R.id.btn_menu_myadvhist);
        btn_menu_noti = findViewById(R.id.btn_menu_noti);
        btn_menu_myinfo = findViewById(R.id.btn_menu_myinfo);
        btn_menu_point = findViewById(R.id.btn_menu_point);
        btn_menu_pointhist = findViewById(R.id.btn_menu_pointhist);
        btn_menu_qna = findViewById(R.id.btn_menu_qna);
        btn_menu_qnahist = findViewById(R.id.btn_menu_qnahist);
        btn_menu_setting = findViewById(R.id.btn_menu_setting);
        btn_menu_logout = findViewById(R.id.btn_menu_logout);
        btn_menu_registeruser = findViewById(R.id.btn_menu_registeruser);

        btn_menu_way.setOnClickListener(this);
        btn_menu_regildang.setOnClickListener(this);
        btn_menu_ildanghist.setOnClickListener(this);
        btn_menu_myadvhist.setOnClickListener(this);
        btn_menu_noti.setOnClickListener(this);
        btn_menu_myinfo.setOnClickListener(this);
        btn_menu_point.setOnClickListener(this);
        btn_menu_pointhist.setOnClickListener(this);
        btn_menu_qna.setOnClickListener(this);
        btn_menu_qnahist.setOnClickListener(this);
        btn_menu_setting.setOnClickListener(this);
        btn_menu_logout.setOnClickListener(this);
        btn_menu_registeruser.setOnClickListener(this);



        UserInfoModel userInfoModel = CommonUtil.getUserInfo(this);
        Log.d("Restapi" , "userInfo : " + userInfoModel.getCell_no());

        if(userInfoModel.getUser_type() == null || userInfoModel.getUser_type().isEmpty()) {
            // 비회원
            btn_menu_way.setVisibility(View.VISIBLE);
            btn_menu_regildang.setVisibility(View.GONE);
            btn_menu_ildanghist.setVisibility(View.GONE);
            btn_menu_myadvhist.setVisibility(View.GONE);
            btn_menu_noti.setVisibility(View.VISIBLE);
            btn_menu_myinfo.setVisibility(View.GONE);
            btn_menu_point.setVisibility(View.GONE);
            btn_menu_pointhist.setVisibility(View.GONE);
            btn_menu_qna.setVisibility(View.GONE);
            btn_menu_qnahist.setVisibility(View.GONE);
            btn_menu_setting.setVisibility(View.VISIBLE);
            btn_menu_logout.setVisibility(View.GONE);
            btn_menu_registeruser.setVisibility(View.VISIBLE);
        } else if(userInfoModel.getUser_type() != null && userInfoModel.getUser_type().equals("1")) {
            // 기술자회원
            btn_menu_way.setVisibility(View.VISIBLE);
            btn_menu_regildang.setVisibility(View.VISIBLE);
            btn_menu_ildanghist.setVisibility(View.VISIBLE);
            btn_menu_myadvhist.setVisibility(View.VISIBLE);
            btn_menu_noti.setVisibility(View.VISIBLE);
            btn_menu_myinfo.setVisibility(View.VISIBLE);
            btn_menu_point.setVisibility(View.VISIBLE);
            btn_menu_pointhist.setVisibility(View.VISIBLE);
            btn_menu_qna.setVisibility(View.VISIBLE);
            btn_menu_qnahist.setVisibility(View.VISIBLE);
            btn_menu_setting.setVisibility(View.VISIBLE);
            btn_menu_logout.setVisibility(View.VISIBLE);
            btn_menu_registeruser.setVisibility(View.GONE);
        } else if(userInfoModel.getUser_type() != null && userInfoModel.getUser_type().equals("2")) {
            // 오더주회원
            btn_menu_way.setVisibility(View.VISIBLE);
            btn_menu_regildang.setVisibility(View.GONE);
            btn_menu_ildanghist.setVisibility(View.VISIBLE);
            btn_menu_myadvhist.setVisibility(View.VISIBLE);
            btn_menu_noti.setVisibility(View.VISIBLE);
            btn_menu_myinfo.setVisibility(View.VISIBLE);
            btn_menu_point.setVisibility(View.VISIBLE);
            btn_menu_pointhist.setVisibility(View.VISIBLE);
            btn_menu_qna.setVisibility(View.VISIBLE);
            btn_menu_qnahist.setVisibility(View.VISIBLE);
            btn_menu_setting.setVisibility(View.VISIBLE);
            btn_menu_logout.setVisibility(View.VISIBLE);
            btn_menu_registeruser.setVisibility(View.GONE);
        }


    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_img_btn :
                backActivity();
                break;
            case R.id.btn_menu_way:
                // 이용방법보기
                intent = new Intent(MenuActivity.this , GuideMain.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_registeruser:
                intent = new Intent(MenuActivity.this , LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_regildang:
                // 일당등록하기
                if(checkObtainUser()) {
                    intent = new Intent(MenuActivity.this , RegisterIldangType.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_menu_ildanghist:
                // 일당이력
                intent = new Intent(MenuActivity.this , IldangHistory.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_myadvhist:
                // 나의 광고내역
                intent = new Intent(MenuActivity.this , MyAdvList.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_noti:
                // 공지사항
                intent = new Intent(MenuActivity.this , NotificationMain.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_myinfo:
                // 개인설정
                intent = new Intent(MenuActivity.this , ChangeMyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_point:
                // 포인트 충/환전
                intent = new Intent(MenuActivity.this , PointCharge.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_pointhist:
                // 포인트내역
                intent = new Intent(MenuActivity.this , PointHistory.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_qna:
                // 문의하기
                intent = new Intent(MenuActivity.this , QnaRequest.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_qnahist:
                // 문의내역
                intent = new Intent(MenuActivity.this , QnaHistory.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_setting:
                // 환경설정
                intent = new Intent(MenuActivity.this , SettingPreference.class);
                startActivity(intent);
                break;
            case R.id.btn_menu_logout:
                showAlertDialog("로그아웃" , "정말 로그아웃 하시겠습니까?" , true);
                break;

        }
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

                SharedPreferences mPref = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putString(CELL_NO , "");
                editor.putString(USER_PWD , "");
                editor.putString(USER_TYPE , "");
                editor.putString(USER_ABLE_JOB , "");
                editor.putString(USER_NAME , "");
                editor.putString(USER_BIR_DAY , "");
                editor.putString(USER_BIR_MONTH , "");
                editor.putString(USER_BIR_YEAR , "");
                editor.putString(ADDRESS , "");
                editor.putString(PUSH_YN , "");
                editor.commit();


                mPref = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this);
                editor = mPref.edit();
                editor.putBoolean("ring_push", false);
                editor.commit();


                Intent intent = new Intent(MenuActivity.this , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


                /*
                Intent intent = new Intent(MainActivity.this , LoginActivity.class);
                startActivity(intent);
                */
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

    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }
    private void backActivity() {
        super.onBackPressed();
    }

    private boolean checkObtainUser() {
        SharedPreferences mPref = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        String user_pwd = mPref.getString(USER_INFO.USER_PWD , "none");
        String user_type = mPref.getString(USER_INFO.USER_TYPE , "none");
        if(user_type.equals("1")) {
            return true;
        } else {
            CommonUtil.showToast(this,"기술자 회원만 이용할 수 있는 서비스입니다.");
            return false;
        }
    }
}
