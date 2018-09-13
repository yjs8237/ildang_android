package com.jscompany.ildang.ildangregister;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.advilgam.AdvDetail;
import com.jscompany.ildang.fcmPush.FcmPushService;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterIldangType extends AppCompatActivity implements  View.OnClickListener{

    TextView tv_ad_title;
    TextView tv_ad_location;
    TextView tv_ad_contact;
    TextView tv_ad_content;
    TextView tv_ad_seq;
    TextView tv_ad_com_name;
    LinearLayout adv_linear;
    private ProgressDialog progressDialog;

    private int connect_count;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ildang_regi_type);

        ImageButton back_img_btn =(ImageButton) findViewById(R.id.back_img_btn);

        Button button1_1 = (Button)findViewById(R.id.button1_1);   // 제단,아이롱
        Button button1_2 = (Button)findViewById(R.id.button1_2);   // 미싱
        Button button2_1 = (Button)findViewById(R.id.button2_1);   // 삼봉
        Button button2_2 = (Button)findViewById(R.id.button2_2);   // 오바
        Button button3_1 = (Button)findViewById(R.id.button3_1);   // 시다
        Button button3_2 = (Button)findViewById(R.id.button3_2);   // 기타(특종)


        tv_ad_title =(TextView) findViewById(R.id.tv_ad_title);
        tv_ad_location =(TextView) findViewById(R.id.tv_ad_location);
        tv_ad_contact =(TextView) findViewById(R.id.tv_ad_contact);
        tv_ad_content =(TextView) findViewById(R.id.tv_ad_content);
        tv_ad_com_name =(TextView) findViewById(R.id.tv_ad_com_name);
        tv_ad_seq =(TextView) findViewById(R.id.tv_ad_seq);
        adv_linear = (LinearLayout)findViewById(R.id.adv_linear);
        adv_linear.setOnClickListener(this);


        button1_1.setOnClickListener(this);
        button1_2.setOnClickListener(this);
        button2_1.setOnClickListener(this);
        button2_2.setOnClickListener(this);
        button3_1.setOnClickListener(this);
        button3_2.setOnClickListener(this);
        back_img_btn.setOnClickListener(this);



        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("일당등록");
        alertDialogBuilder.setMessage("기술자 구분을 선택해주세요");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        settingAdver();

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

            case R.id.button1_1:
                intent = new Intent(this , RegisterIldangLocation.class);
                intent.putExtra("job_type"  , "A001");
                intent.putExtra("job_type_str"  , "재단,아이롱");
                startActivity(intent);
                break;
            case R.id.button1_2:
                intent = new Intent(this , RegisterIldangLocation.class);
                intent.putExtra("job_type"  , "A002");
                intent.putExtra("job_type_str"  , "미싱");
                startActivity(intent);
                break;
            case R.id.button2_1:
                intent = new Intent(this , RegisterIldangLocation.class);
                intent.putExtra("job_type"  , "A003");
                intent.putExtra("job_type_str"  , "삼봉");
                startActivity(intent);
                break;
            case R.id.button2_2:
                intent = new Intent(this , RegisterIldangLocation.class);
                intent.putExtra("job_type"  , "A004");
                intent.putExtra("job_type_str"  , "오바");
                startActivity(intent);
                break;
            case R.id.button3_1:
                intent = new Intent(this , RegisterIldangLocation.class);
                intent.putExtra("job_type"  , "A005");
                intent.putExtra("job_type_str"  , "시다");
                startActivity(intent);
                break;
            case R.id.button3_2:
                intent = new Intent(this , RegisterIldangLocation.class);
                intent.putExtra("job_type"  , "A006");
                intent.putExtra("job_type_str"  , "기타(특종)");
                startActivity(intent);
                break;
            case R.id.back_img_btn:
                backActivity();
                break;

        }
    }

    private void backActivity() {
        super.onBackPressed();
    }


    public void sendNotificationV4() {

        Intent intent = new Intent(RegisterIldangType.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("push" , "Oreo~~~~");

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannelGroup group1 = new NotificationChannelGroup("channel_group_id", "channel_group_name");
            notificationManager.createNotificationChannelGroup(group1);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            notificationChannel.setGroup("channel_group_id");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);

            mBuilder = new NotificationCompat.Builder(RegisterIldangType.this , "channel_id")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("title")
                    .setContentText("message")
                    .setAutoCancel(true)
                    .setContentIntent(mPendingIntent);
        } else {
            mBuilder = new NotificationCompat.Builder(RegisterIldangType.this)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("title")
                    .setContentText("message")
                    .setAutoCancel(true)
                    .setContentIntent(mPendingIntent);
        }

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }



    public void sendNotificationV3() {


        String title = "title";
        String message = "message";
        Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        Log.d("push" , "sendNotificationV3 ## 1");
        Intent intent = new Intent(RegisterIldangType.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("push" , "sendNotificationV3 ## 2");
        PendingIntent mPendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(RegisterIldangType.this , "channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                //.setDefaults(Notification.DEFAULT_SOUND)
                //.setLargeIcon(mLargeIconForNoti)
                //.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(mPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
        Log.d("push" , "sendNotificationV3 ## 3");
    }
}
