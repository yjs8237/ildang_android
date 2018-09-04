package com.jscompany.ildang.notification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.MainActivity;

import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.IldangHistoryAdapter;
import com.jscompany.ildang.listview.NotiAdapter;
import com.jscompany.ildang.listview.NotiListItem;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.model.NotificationModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;
import com.jscompany.ildang.userlist.UserListActivity;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationMain extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemClickListener , AbsListView.OnScrollListener{


    public static int TIME_OUT = 1001;

    private ListView listView;
    private ProgressDialog progressDialog;

    private NotiAdapter mAdapter;
    private View view;

    private TextView empty_view;

    private NotificationModel notificationModel;

    private boolean isFirstData = true;
    public int start_index = 0;
    public int search_count = 20;
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    private boolean lastItemVisibleFlag =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti_main);

        listView = findViewById(R.id.noti_list_view);
        mAdapter = new NotiAdapter();

        empty_view = findViewById(R.id.empty_list);

        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(this);

        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        if(notificationModel == null) {
            notificationModel = new NotificationModel();
            notificationModel.setStart_index(0);
            notificationModel.setSearch_count(search_count);
        }
        doWork();

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("Restapi" , "api : " + "onScrollStateChanged " + lastItemVisibleFlag + " , " + mLockListView);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
//            progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            if(notificationModel == null) {
                notificationModel = new NotificationModel();
                notificationModel.setStart_index(0);
                notificationModel.setSearch_count(search_count);
            } else {
                notificationModel.setStart_index(start_index);
            }
            isFirstData = false;
            doWork();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d("Restapi" , "api : " + "onScroll " + firstVisibleItem + " , " + visibleItemCount + " , " + totalItemCount);
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }


    private void doWork() {

        SharedPreferences mPref = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");

        RestService restService = ServiceGenerator.createService(RestService.class );
        Call<JsonObject> call = restService.notilist(notificationModel);
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
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
                                NotificationModel tempModel = new NotificationModel();
                                JsonObject tempJson = (JsonObject) jsonArr.get(i);
                                tempModel.setNoti_seq(tempJson.get("noti_seq").getAsLong());
                                tempModel.setReg_date(tempJson.get("reg_date").getAsString());
                                tempModel.setContent(tempJson.get("content").getAsString());
                                tempModel.setTitle(tempJson.get("title").getAsString());
                                mAdapter.addItem( tempModel);
                            }
                        }
//                        progressBar.setVisibility(View.GONE);
                        mLockListView = false;
                        if(isFirstData){
                            listView.setAdapter(mAdapter);
                        }
                        mAdapter.notifyDataSetChanged();
                        start_index = start_index + jsonArr.size();

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




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this , NotificationDetail.class);
        intent.putExtra("noti_seq" , mAdapter.getItem(position).getNoti_seq());
        intent.putExtra("title" , mAdapter.getItem(position).getTitle());
        intent.putExtra("content" , mAdapter.getItem(position).getContent());
        intent.putExtra("reg_date" , mAdapter.getItem(position).getReg_date());
        startActivity(intent);

        String message= String.valueOf(position) + " , " + mAdapter.getItem(position).getNoti_seq();
        CommonUtil.showToast(this, message  );

    }

    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.back_img_btn :
                backActivity();
                break;
            case R.id.noti_list_view :

                break;
            default:

                break;
        }

    }

    private void backActivity() {
        super.onBackPressed();
    }

}
