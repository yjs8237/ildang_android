package com.jscompany.ildang.ildanglist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.advilgam.AdvDetail;
import com.jscompany.ildang.listview.IldangAdapter;
import com.jscompany.ildang.listview.NotiAdapter;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.notification.NotificationDetail;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IldangList extends AppCompatActivity implements  View.OnClickListener , AdapterView.OnItemClickListener {

    public static int TIME_OUT = 1001;

    private ListView listView;
    private ProgressDialog progressDialog;

    private IldangAdapter ildangAdapter;

    private ArrayList<IldangModel> ildang_list;

    private String job_type;
    private String job_type_str;
    private String loc_gu;
    private String loc_gu_str;
    private String loc_dong;
    private String loc_dong_str;

    private TextView empty_view;

    private int connect_count;
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

        setContentView(R.layout.ildang_list);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null) {
            job_type        = intent.getExtras().getString("job_type" , "false");
            job_type_str    = intent.getExtras().getString("job_type_str" , "false");
            loc_gu          = intent.getExtras().getString("loc_gu" , "false");
            loc_gu_str      = intent.getExtras().getString("loc_gu_str" , "false");
            loc_dong        = intent.getExtras().getString("loc_dong" , "false");
            loc_dong_str    = intent.getExtras().getString("loc_dong_str" , "false");
            connect_count = intent.getExtras().getInt("connect_count");
            TextView tv_connect_count = (TextView)findViewById(R.id.tv_connect_count);
            tv_connect_count.setText("방문자 수 : " + connect_count);
        } else {
            job_type = "none";
            loc_gu = "none";
            loc_dong = "none";
        }



        tv_ad_title =(TextView) findViewById(R.id.tv_ad_title);
        tv_ad_location =(TextView) findViewById(R.id.tv_ad_location);
        tv_ad_contact =(TextView) findViewById(R.id.tv_ad_contact);
        tv_ad_content =(TextView) findViewById(R.id.tv_ad_content);
        tv_ad_com_name =(TextView) findViewById(R.id.tv_ad_com_name);
        tv_ad_seq =(TextView) findViewById(R.id.tv_ad_seq);
        adv_linear = (LinearLayout)findViewById(R.id.adv_linear);
        adv_linear.setOnClickListener(this);

        ImageButton back_img_btn = (ImageButton) findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        empty_view = (TextView) findViewById(R.id.empty_list);
        listView = (ListView)findViewById(R.id.ildang_list_view);
        ildangAdapter = new IldangAdapter();

        listView.setOnItemClickListener(this);
        doWork();

    }

    private void doWork() {

        ildang_list = new ArrayList<>();
        // 해당 구의 동 리스트 가져오기
        IldangModel ildangModel = new IldangModel();

        ildangModel.setLoc_gu(loc_gu);
        ildangModel.setJob_type(job_type);
        ildangModel.setLoc_dong(loc_dong);
        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.selectIldangList(ildangModel);
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
                                IldangModel tempModel = new IldangModel();
                                JsonObject tempJson = (JsonObject) jsonArr.get(i);
                                tempModel.setUser_nick(tempJson.get("user_nick").getAsString());
                                tempModel.setJob_seq(tempJson.get("job_seq").getAsLong());
                                tempModel.setUser_seq(tempJson.get("user_seq").getAsLong());
                                tempModel.setCell_no(tempJson.get("cell_no").getAsString());
                                tempModel.setJob_type(tempJson.get("job_type").getAsString());
                                tempModel.setLoc_gu(tempJson.get("loc_gu").getAsString());
                                tempModel.setLoc_dong(tempJson.get("loc_dong").getAsString());
                                tempModel.setFinish_yn(tempJson.get("finish_yn").getAsString());
                                tempModel.setWork_date(tempJson.get("work_date").getAsString());
                                tempModel.setWork_pay(tempJson.get("work_pay").getAsInt());
                                tempModel.setReg_date(tempJson.get("reg_date").getAsString());
                                tempModel.setUser_able_job(tempJson.get("user_able_job").getAsString());
                                ildang_list.add(tempModel);
                                ildangAdapter.addItem( tempModel);
                            }
                        }


                        listView.setAdapter(ildangAdapter);
                        ildangAdapter.notifyDataSetChanged();

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


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()) {
            case  R.id.back_img_btn :
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        SharedPreferences mPref = getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);

        String user_type = mPref.getString(USER_INFO.USER_TYPE , "none");
        String order_cell_no = mPref.getString(USER_INFO.CELL_NO , "none");

        if(!user_type.equals("2") ) {
            showDialogMessage("실패" , "오더주만 오더하실 수 있습니다.");
            return;
        }

        IldangModel ildang = ildangAdapter.getItem(position);
        ildang.setOrder_cell_no(order_cell_no);


        String title = "일당 매칭";
        String message = loc_dong_str + "\n" + "닉네임 (" + ildang.getUser_nick() + ")" + "\n" + "일당 오더 하시겠습니까?";
        message += "\n\n" + "기술자로부터 곧 연락이 올거에요.";

        showConfirmDialog(title , message , ildang , position);

        /*
        Intent intent = new Intent(IldangList.this , NotificationDetail.class);
        intent.putExtra("noti_seq" , notiAdapter.getItem(position).getNoti_seq());
        startActivity(intent);

        String message= String.valueOf(position) + " , " + notiAdapter.getItem(position).getNoti_seq();
        CommonUtil.showToast(getContext(), message  );
        */

    }

    private void showConfirmDialog(String title, String message , final IldangModel model , final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestIldangMatch(model , position);
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

    private void requestIldangMatch(IldangModel ildangModel , final int position) {

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.requestIldangMatch(ildangModel);
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
                        ildangAdapter.removeItem(position);
                        ildangAdapter.notifyDataSetChanged();

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
}
