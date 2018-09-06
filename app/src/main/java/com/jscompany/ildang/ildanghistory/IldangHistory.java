package com.jscompany.ildang.ildanghistory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jscompany.ildang.Common.CONST;
import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.Common.USER_INFO;
import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.IldangHistoryAdapter;
import com.jscompany.ildang.listview.QnaAdapter;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.restAPI.RestService;
import com.jscompany.ildang.restAPI.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IldangHistory extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemClickListener {

    public static int TIME_OUT = 1001;

    private ListView listView;
    private ProgressDialog progressDialog;

    private IldangHistoryAdapter mAdapter;
    private View view;

    private TextView empty_view;

    private ArrayList<IldangModel> ildang_list;

    private String order_cell_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ildanghist_list);

        listView = findViewById(R.id.ildang_hist_list_view);
        mAdapter = new IldangHistoryAdapter();
        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        empty_view = findViewById(R.id.empty_list);
        listView.setOnItemClickListener(this);
        doWork();

    }

    private void doWork() {
        ildang_list = new ArrayList<>();

        IldangModel ildangModel = new IldangModel();
        SharedPreferences mPref = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        final String user_type = mPref.getString(USER_INFO.USER_TYPE , "none");
        ildangModel.setCell_no(cell_no);
        ildangModel.setUser_type(user_type);
        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.mymatchlist(ildangModel);
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
                                tempModel.setJob_seq(tempJson.get("job_seq").getAsLong());
                                tempModel.setUser_seq(tempJson.get("user_seq").getAsLong());
                                tempModel.setCell_no(tempJson.get("cell_no").getAsString());
                                tempModel.setJob_type(tempJson.get("job_type").getAsString());
                                tempModel.setLoc_gu(tempJson.get("loc_gu").getAsString());
                                tempModel.setLoc_dong(tempJson.get("loc_dong").getAsString());
                                tempModel.setFinish_yn(tempJson.get("finish_yn").getAsString());
                                tempModel.setWork_date(tempJson.get("work_date").getAsString());
                                tempModel.setReg_date(tempJson.get("reg_date").getAsString());
                                tempModel.setUser_nick(tempJson.get("user_nick").getAsString());
                                tempModel.setUser_name(tempJson.get("user_name").getAsString());
                                tempModel.setJob_type_str(tempJson.get("job_type_str").getAsString());
                                tempModel.setLoc_gu_str(tempJson.get("loc_gu_str").getAsString());
                                tempModel.setLoc_dong_str(tempJson.get("loc_dong_str").getAsString());
                                tempModel.setRemain_time(tempJson.get("remain_time").getAsString());
                                tempModel.setCom_name(tempJson.get("com_name").getAsString());
                                tempModel.setUser_type(user_type);
                                mAdapter.addItem( tempModel);
                            }
                        }

                        listView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

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

    private void requestContact(final IldangModel ildangModel, final int position) {
        ildang_list = new ArrayList<>();

        final IldangModel sendModel = new IldangModel();

        SharedPreferences mPref = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        sendModel.setCell_no(cell_no);
        sendModel.setJob_seq(ildangModel.getJob_seq());
        sendModel.setOrder_cell_no(order_cell_no);
        sendModel.setOrder_cell_no(ildangModel.getCell_no());

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.ordercontact(sendModel);
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
//                        mAdapter.removeItem(position);
                        mAdapter.finishItem(position);
                        mAdapter.notifyDataSetChanged();

                        String tel = "tel:"+sendModel.getOrder_cell_no();
                        startActivity(new Intent("android.intent.action.DIAL" , Uri.parse(tel)));

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


    private void ildangCancle(final IldangModel ildangModel, final int position) {
        ildang_list = new ArrayList<>();

        final IldangModel sendModel = new IldangModel();

        SharedPreferences mPref = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        sendModel.setCell_no(cell_no);
        sendModel.setJob_seq(ildangModel.getJob_seq());
        sendModel.setOrder_cell_no(order_cell_no);
        sendModel.setOrder_cell_no(ildangModel.getCell_no());

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.cancleildang(sendModel);
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
                        mAdapter.removeItem(position);
//                        mAdapter.finishItem(position);
                        mAdapter.notifyDataSetChanged();

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

    private void orderCancle(final IldangModel ildangModel, final int position) {
        ildang_list = new ArrayList<>();

        final IldangModel sendModel = new IldangModel();

        SharedPreferences mPref = this.getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String cell_no = mPref.getString(USER_INFO.CELL_NO , "none");
        sendModel.setCell_no(cell_no);
        sendModel.setJob_seq(ildangModel.getJob_seq());
        sendModel.setOrder_cell_no(order_cell_no);
        sendModel.setOrder_cell_no(ildangModel.getCell_no());

        RestService restService = ServiceGenerator.createService(RestService.class );

        Call<JsonObject> call = restService.cancleorder(sendModel);
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
                        mAdapter.removeItem(position);
//                        mAdapter.finishItem(position);
                        mAdapter.notifyDataSetChanged();

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
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.back_img_btn :
                backActivity();
                break;
        }
    }

    private void backActivity() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IldangModel ildang = mAdapter.getItem(position);

        SharedPreferences mPref = getSharedPreferences("USER_INFO" , Context.MODE_PRIVATE);
        String user_type = mPref.getString(USER_INFO.USER_TYPE , "none");

        if(user_type.equals("1")) {
            // 기술자
            procUser_1(ildang , position);
        } else if(user_type.equals("2")){
            // 오더주
            procUser_2(ildang , position);
        }



    }

    private void procUser_1(IldangModel ildang , int position) {
        if(ildang.getFinish_yn().equals("N")) {
            // 취소할 경우
            String title = "일당 매칭";
            StringBuffer sb = new StringBuffer();
            sb.append(ildang.getWork_date()).append("\n");
            sb.append(ildang.getJob_type_str() + "-" + ildang.getLoc_gu_str() +  "(" + ildang.getLoc_dong_str()+")").append("\n");
            sb.append("해당 일당등록을 취소하시겠습니까?");
            order_cell_no = ildang.getCell_no();
            showConfirmDialog(title , sb.toString() , ildang , position , true);
        } else if (ildang.getFinish_yn().equals("Y")){
            // 오더주연락할 경우
            String title = "일당 매칭";
            StringBuffer sb = new StringBuffer();
            sb.append(ildang.getWork_date()).append("\n");
            sb.append(ildang.getJob_type_str() + "-" + ildang.getLoc_gu_str() +  "(" + ildang.getLoc_dong_str()+")").append("\n");
            sb.append(ildang.getUser_nick() + " 님 에게 연락하시겠습니까?");
            order_cell_no = ildang.getCell_no();

            showConfirmDialog(title , sb.toString() , ildang , position , true);
        } else {
            return ;
        }
    }

    private void procUser_2(IldangModel ildang , int position) {
        if(ildang.getFinish_yn().equals("Y")) {
            // 오더주가 컨택하고 기술자로부터 연락이 오기전 단계
            String title = "일당 매칭";
            StringBuffer sb = new StringBuffer();
            sb.append(ildang.getWork_date()).append("\n");
            sb.append(ildang.getJob_type_str() + "-" + ildang.getLoc_gu_str() +  "(" + ildang.getLoc_dong_str()+")").append("\n");
            sb.append("해당 일당매칭을 취소하시겠습니까?");
            order_cell_no = ildang.getCell_no();
            showConfirmDialog(title , sb.toString() , ildang , position , false);
        } else {
            return;
        }
    }

    private void showConfirmDialog(String title, String message , final IldangModel model , final int position , final boolean isGisulja) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(isGisulja) {
                    // 기술자 로직
                    if(model.getFinish_yn().equals("Y")) {
                        requestContact(model , position);
                    } else if(model.getFinish_yn().equals("N")) {
                        // 대기중 취소
                        ildangCancle(model , position);
                    }
                } else {
                    // 오더주 로직
                    orderCancle(model , position);
                }
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

    private void showNetworkError() {
        CommonUtil.showDialog(this, "통신실패" , "네트워크 상태를 확인해 주세요.");
    }

    private void showDialogMessage(String title, String message) {
        CommonUtil.showDialog(this, title , message);
    }
}
