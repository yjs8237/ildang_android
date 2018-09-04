package com.jscompany.ildang.advilgam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.MainActivity;
import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.IlgamAdapter;
import com.jscompany.ildang.listview.IlgamListItem;
import com.jscompany.ildang.listview.NotiAdapter;
import com.jscompany.ildang.listview.NotiListItem;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.notification.NotificationDetail;
import com.jscompany.ildang.preference.SettingActivity;

import org.w3c.dom.Text;

public class AdvBadIldangMain extends AppCompatActivity implements  View.OnClickListener {

    private ProgressDialog progressDialog;

    private Button btn_ilter_list;
    private Button btn_ilter_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_layout);

        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);

        btn_ilter_list = (Button)findViewById(R.id.btn_ilter_list);
        btn_ilter_register = (Button)findViewById(R.id.btn_ilter_register);

        btn_ilter_list.setText("불성실일당 목록보기");
        btn_ilter_register.setText("불성실일당 등록하기");
        TextView tv_layout_title =(TextView) findViewById(R.id.tv_layout_title);
        tv_layout_title.setText("불성실일당 신고방");


        btn_ilter_register.setOnClickListener(this);
        btn_ilter_list.setOnClickListener(this);
        back_img_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.back_img_btn :
                backActivity();
                break;

            case R.id.btn_ilter_register :

                UserInfoModel userInfoModel = CommonUtil.getUserInfo(this);
                if(userInfoModel.getUser_type().equals("1") || userInfoModel.getUser_type().equals("2")) {
                    intent = new Intent(AdvBadIldangMain.this, AdvBadIldangRegister.class);
                    startActivity(intent);
                } else {
                    CommonUtil.showDialog(this, "비회원 사용불가" , "비회원 이용 불가 서비스 입니다.");
                }
                break;

            case R.id.btn_ilter_list :
                intent = new Intent(AdvBadIldangMain.this , AdvBadIldangList.class);
                startActivity(intent);
                break;
        }
    }



    private void backActivity() {
        super.onBackPressed();
    }
}
