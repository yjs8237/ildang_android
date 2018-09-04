package com.jscompany.ildang.point;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.NotiAdapter;
import com.jscompany.ildang.listview.PointAdapter;
import com.jscompany.ildang.listview.PointItem;
import com.jscompany.ildang.model.NotificationModel;
import com.jscompany.ildang.model.PointModel;
import com.jscompany.ildang.model.UserInfoModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import okhttp3.Interceptor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PointHistory extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemClickListener  , AbsListView.OnScrollListener{

    public static int TIME_OUT = 1001;

    private ListView listView;
    private ProgressDialog progressDialog;

    private PointAdapter mAdapter;
    private View view;

    private TextView empty_view;
    private SharedPreferences mPrefs;

    private String cell_no;
    private String user_pwd;
    private String user_type;

    private String user_point;

    private Retrofit retrofit;
    private RestService restService;


    private boolean isFirstData = true;
    public int start_index = 0;
    public int search_count = 20;
    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    private boolean lastItemVisibleFlag =false;

    private PointModel pointModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_list);


        listView = findViewById(R.id.point_list_view);
        empty_view = findViewById(R.id.empty_list);
        mAdapter = new PointAdapter();

        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);

        mPrefs = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        cell_no = mPrefs.getString(USER_INFO.CELL_NO , "none");
        user_pwd = mPrefs.getString(USER_INFO.USER_PWD , "none");
        user_type = mPrefs.getString(USER_INFO.USER_TYPE , "none");

        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        if(pointModel == null) {
            pointModel = new PointModel();
            pointModel.setStart_index(0);
            pointModel.setSearch_count(search_count);
        }
        doWork();

    }

    public void doWork() {
        RestService restService = ServiceGenerator.createService(RestService.class );

        pointModel.setCell_no(cell_no);
        pointModel.setUser_pwd(user_pwd);
        Call<JsonObject> call = restService.pointlist(pointModel);
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
                        JsonArray jsonArray = jsonObj.getAsJsonArray("list");
                        if(jsonArray.size() == 0) {
                            listView.setEmptyView(empty_view);
                        } else {
                            for (int i=0; i<jsonArray.size(); i++) {
                                PointModel tempModel = new PointModel();
                                JsonObject tempJson = (JsonObject) jsonArray.get(i);
                                tempModel.setPoint_seq(tempJson.get("point_seq").getAsLong());
                                tempModel.setType(tempJson.get("type").getAsString());
                                tempModel.setMoney(tempJson.get("money").getAsInt());
                                tempModel.setFinish_yn(tempJson.get("finish_yn").getAsString());
                                tempModel.setReg_date(tempJson.get("reg_date").getAsString());
                                mAdapter.addItem(tempModel);
                            }

                            mLockListView = false;
                            if(isFirstData){
                                listView.setAdapter(mAdapter);
                            }
                            mAdapter.notifyDataSetChanged();
                            start_index = start_index + jsonArray.size();

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

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.back_img_btn :
                backActivity();
                break;
            default:

                break;
        }

    }


    private void showToast(String message) {
        Toast.makeText(this , message , Toast.LENGTH_SHORT).show();
    }


    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d("Restapi" , "api : " + "onScrollStateChanged " + lastItemVisibleFlag + " , " + mLockListView);
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
//            progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            if(pointModel == null) {
                pointModel = new PointModel();
                pointModel.setStart_index(0);
                pointModel.setSearch_count(search_count);
            } else {
                pointModel.setStart_index(start_index);
            }
            isFirstData = false;
            doWork();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    private void backActivity() {
        super.onBackPressed();
    }
}
