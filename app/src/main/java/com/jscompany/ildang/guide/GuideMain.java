package com.jscompany.ildang.guide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jscompany.ildang.Common.CommonUtil;
import com.jscompany.ildang.R;
import com.jscompany.ildang.listview.NotiAdapter;
import com.jscompany.ildang.model.GuideModel;
import com.jscompany.ildang.model.NotificationModel;
import com.jscompany.ildang.notification.NotificationDetail;

public class GuideMain extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemClickListener , AbsListView.OnScrollListener{

    public static int TIME_OUT = 1001;

    private ListView listView;
    private ProgressDialog progressDialog;

    private GuideAdapter mAdapter;
    private View view;

    private TextView empty_view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_main);

        listView = findViewById(R.id.guide_list_view);
        mAdapter = new GuideAdapter();

        empty_view = findViewById(R.id.empty_list);

        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(this);

        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        doWork();

    }

    private void doWork() {



        GuideModel model = new GuideModel();
        model.setGuide_seq(1);
        model.setType("기술자");
        model.setTitle("일당등록 방법");
        model.setContent("일하기 위해 나를 등록하는 방법");
        mAdapter.addItem(model);

        model = new GuideModel();
        model.setGuide_seq(2);
        model.setType("기술자");
        model.setTitle("일당매칭 확인하기");
        model.setContent("나에게 들어온 오더를 확인 하는 방법");
        mAdapter.addItem(model);

        model = new GuideModel();
        model.setGuide_seq(10);
        model.setType("오더주");
        model.setTitle("일당 오더 방법");
        model.setContent("기술자분에게 오더 하는 방법");
        mAdapter.addItem(model);


        model = new GuideModel();
        model.setGuide_seq(20);
        model.setType("공통");
        model.setTitle("광고 등록하기");
        model.setContent("광고 등록하는 방법");
        mAdapter.addItem(model);

        model = new GuideModel();
        model.setGuide_seq(21);
        model.setType("공통");
        model.setTitle("나의 광고 확인/삭제 방법");
        model.setContent("나의 광고리스트 확인 및 삭제 방법");
        mAdapter.addItem(model);

        mAdapter.notifyDataSetChanged();
        listView.setAdapter(mAdapter);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(this , GuideDetail.class);
        intent.putExtra("guide_seq" , mAdapter.getItem(position).getGuide_seq());
        startActivity(intent);

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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

}
