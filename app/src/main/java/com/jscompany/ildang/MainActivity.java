package com.jscompany.ildang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.advilgam.MyAdvList;
import com.jscompany.ildang.guide.GuideMain;
import com.jscompany.ildang.ildanghistory.IldangHistory;
import com.jscompany.ildang.ildangregister.RegisterIldangLocation;
import com.jscompany.ildang.ildangregister.RegisterIldangType;
import com.jscompany.ildang.layout.MainFirstActivity;
import com.jscompany.ildang.login.LoginActivity;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.myinformation.ChangeMyInfoActivity;
import com.jscompany.ildang.notification.NotificationMain;
import com.jscompany.ildang.point.PointCharge;
import com.jscompany.ildang.point.PointHistory;
import com.jscompany.ildang.point.PointRefund;
import com.jscompany.ildang.preference.SettingActivity;
import com.jscompany.ildang.preference.SettingFragment;
import com.jscompany.ildang.preference.SettingPreference;
import com.jscompany.ildang.qna.QnaHistory;
import com.jscompany.ildang.qna.QnaRequest;
import com.jscompany.ildang.register.RegisterActivity;
import com.jscompany.ildang.register.UserChoiceActivity;
import com.jscompany.ildang.userlist.UserListActivity;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.jscompany.ildang.Common.USER_INFO.*;
import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener{

    private long pressedTime;


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LOGIN" , "onStart");
        SharedPreferences mPref = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        String user_pwd = mPref.getString(USER_INFO.USER_PWD , "none");
        Log.d("LOGIN" , "cell_no : " + cell_no);
        Log.d("LOGIN" , "user_pwd : " + user_pwd);

        /*
        if(cell_no == null || cell_no.equals("none") || cell_no.isEmpty()) {
            Log.d("LOGIN" , "NOT LOGIN");
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
        }
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPref = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        String user_pwd = mPref.getString(USER_INFO.USER_PWD , "none");
        String user_type = mPref.getString(USER_INFO.USER_TYPE , "none");

        pressedTime = 0;

        /*
        if(cell_no.equals("none")) {
            Log.d("LOGIN" , "NOT LOGIN");
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
        }
        */
        if(user_type.equals("none")) {
            user_type = "";
        }

        if(user_type.equals("2") || user_type.equals("order")) {
            setContentView(R.layout.activity_main_order);
        } else if(user_type.equals("1") || user_type.equals("obtain")){
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main_other);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//
//        if(bundle != null) {
//            String paramData = intent.getExtras().getString("param" , "false");
//        } else {
//            intent = new Intent(MainActivity.this , UserListActivity.class);
//            startActivity(intent);
//        }

        Fragment fragment = new MainFirstActivity();
        if(fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame , fragment);
            ft.commit();
        }

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_launcher_foreground, this.getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setDrawerIndicatorEnabled(true);


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Nav 헤더에 유저 휴대폰번호 노출
        View nav_header_view = navigationView.getHeaderView(0);
        TextView nav_header_id_text = (TextView) nav_header_view.findViewById(R.id.tv_cell_no);
        String userinfo = cell_no;
        if(user_type.equals("1")) {
            userinfo +=  " (기술자)";
        } else if(user_type.equals("2")) {
            userinfo +=  " (오더주)";
        }
        nav_header_id_text.setText(userinfo);

        ImageButton kakao_Btn = (ImageButton)findViewById(R.id.kko_img_btn);
        TextView tv_app_name = (TextView)findViewById(R.id.tv_app_name);
        kakao_Btn.setOnClickListener(this);
        tv_app_name.setOnClickListener(this);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kko_img_btn :
//                showToast("카카오");
                kakaoLink();
//                getHash();
                break;

            case R.id.tv_app_name :
//                showToast("앱이름");
                Fragment fragment = new MainFirstActivity();
                if(fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame , fragment);
                    ft.commit();
                }
                break;
        }
    }

    private String getHash() {
        PackageInfo packageInfo = getPackageInfo(this, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.d("LOGIN" , "HASH " + Base64.encodeToString(md.digest(), Base64.NO_WRAP));

                return null;
            } catch (NoSuchAlgorithmException e) {
//                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;


    }


    private void kakaoLink() {
        Log.d("LOGIN" , "kakaoLink");

        try {
//            KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
//            KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//            kakaoBuilder.addText("테스트");
//            String url = "http://post.phinf.naver.net/MjAxNzExMDZfMTAy/MDAxNTA5OTU0Mzc5MDg2.orxHt_a5K6mvqU85GdbVYvksPQLf33azcRaMN6YZXbsg.aqhDbDa2II288zXdEphUHoyyoOPJgYT0FMSGLsLtX1Ig.JPEG/IA6TgExE7sgNzD4z-MJ3gKm2jF9c.jpg";
//            kakaoBuilder.addImage(url, 1090, 1920);
//
//            kakaoBuilder.addAppButton("앱 실행");
//
//            kakaoLink.sendMessage(kakaoBuilder,this);

            FeedTemplate params = FeedTemplate
                    .newBuilder(ContentObject.newBuilder("일당불러",
                            "https://ildangcall.com/static/images/ildang/ildang.png",
                            LinkObject.newBuilder().setWebUrl("https://play.google.com/store/apps/details?id=com.jscompany.ildang")
                                    .setMobileWebUrl("https://play.google.com/store/apps/details?id=com.jscompany.ildang").build())
                            .setDescrption("기술자와 오더주의 기막힌 만남!! 일당불러 앱 다운로드 받으세요~")
                            .build())
                    .setSocial(SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                            .setSharedCount(30).setViewCount(40).build())
                    /*
                    .addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl("'https://developers.kakao.com").setMobileWebUrl("'https://developers.kakao.com").build()))
                    .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                            .setWebUrl("'https://developers.kakao.com")
                            .setMobileWebUrl("'https://developers.kakao.com")
                            .setAndroidExecutionParams("key1=value1")
                            .setIosExecutionParams("key1=value1")
                            .build()))
                    */
                    .build();



            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
            serverCallbackArgs.put("user_id", "${current_user_id}");
            serverCallbackArgs.put("product_id", "${shared_product_id}");

            KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.d("LOGIN" , "ERROR " + errorResult.getErrorMessage());
                }

                @Override
                public void onSuccess(KakaoLinkResponse result) {
                    // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                    Log.d("LOGIN" , "SUCCESS " + result.toString());
                }
            });




        } catch(Exception e) {
            Log.d("LOGIN" , e.getLocalizedMessage());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ( pressedTime == 0 ) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
                pressedTime = System.currentTimeMillis();
            }
            else {
                int seconds = (int) (System.currentTimeMillis() - pressedTime);

                if ( seconds > 2000 ) {
                    Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_LONG).show();
                    pressedTime = 0 ;
                }
                else {
                    finishAffinity();
//                finish(); // app 종료 시키기
                }
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        PreferenceFragment preFragment = null;
        if (id == R.id.menu_noti) {
            // Handle the camera action
//            fragment = new NotificationMain();
            Intent intent = new Intent(MainActivity.this , NotificationMain.class);
            startActivity(intent);
        } else if (id == R.id.menu_point_charge) {
            // 포인트 충전
//            fragment = new PointCharge();
            Intent intent = new Intent(MainActivity.this , PointCharge.class);
            startActivity(intent);
        }  else if (id == R.id.menu_point_back) {
            // 포인트 환전
//            fragment = new PointRefund();
            Intent intent = new Intent(MainActivity.this , PointRefund.class);
            startActivity(intent);
        } else if (id == R.id.menu_point_history) {
            // 포인트 충전내역
//            fragment = new PointHistory();
            Intent intent = new Intent(MainActivity.this , PointHistory.class);
            startActivity(intent);
        } else if (id == R.id.menu_qna_request) {
            // 문의하기
//            fragment = new QnaRequest();
            Intent intent = new Intent(MainActivity.this , QnaRequest.class);
            startActivity(intent);
        } else if (id == R.id.menu_qna_history) {
            // 문의내역
//            fragment = new QnaHistory();
            Intent intent = new Intent(MainActivity.this , QnaHistory.class);
            startActivity(intent);
        } else if(id == R.id.menu_ildang_match) {
            // 일당이력
            if(checkObtainUser()) {
//                fragment = new IldangHistory();
                Intent intent = new Intent(MainActivity.this , IldangHistory.class);
                startActivity(intent);
            }
        } else if(id == R.id.menu_person) {
            // 개인설정
            Intent intent = new Intent(MainActivity.this , ChangeMyInfoActivity.class);
            startActivity(intent);
        } else if(id == R.id.menu_setting) {
            // 환경설정
            Intent intent = new Intent(MainActivity.this , SettingPreference.class);
            startActivity(intent);
//            preFragment = new SettingPreference();
        } else if(id == R.id.menu_home) {
            // 이용방법보기
            Intent intent = new Intent(MainActivity.this , GuideMain.class);
            startActivity(intent);

//            fragment = new MainFirstActivity();
        } else if(id == R.id.menu_ildang_regi) {
            if(checkObtainUser()) {
                Intent intent = new Intent(MainActivity.this , RegisterIldangType.class);
                startActivity(intent);
            }
        } else if(id == R.id.menu_logout) {
            showAlertDialog("로그아웃" , "정말 로그아웃 하시겠습니까?" , true);
        } else if(id == R.id.menu_register_user) {
            Intent intent = new Intent(MainActivity.this , LoginActivity.class);
            startActivity(intent);
        } else if(id == R.id.menu_my_adver) {
            Intent intent = new Intent(MainActivity.this , MyAdvList.class);
            startActivity(intent);
        }

        if(fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame , fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private boolean checkObtainUser() {
        SharedPreferences mPref = getSharedPreferences("USER_INFO" , MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        String user_pwd = mPref.getString(USER_INFO.USER_PWD , "none");
        String user_type = mPref.getString(USER_INFO.USER_TYPE , "none");
        if(user_type.equals("1")) {
            return true;
        } else {
            showToast("기술자 회원만 이용할 수 있는 서비스입니다.");
            return false;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext() , message , Toast.LENGTH_SHORT).show();
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
                editor.commit();

                Intent intent = new Intent(MainActivity.this , MainActivity.class);
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


}
