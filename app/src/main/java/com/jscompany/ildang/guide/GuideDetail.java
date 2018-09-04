package com.jscompany.ildang.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jscompany.ildang.R;

public class GuideDetail extends AppCompatActivity implements  View.OnClickListener, AdapterView.OnItemClickListener{

    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    ImageView image5;
    ImageView image6;
    ImageView image7;
    ImageView image8;
    ImageView image9;
    ImageView image10;

    int resourceID = 0;

    private String packName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_detail);

        Intent intent = getIntent();
        long guide_seq = intent.getExtras().getLong("guide_seq");

        int guide_seq_int = (int) guide_seq;

        ImageButton back_img_btn = (ImageButton)findViewById(R.id.back_img_btn);
        back_img_btn.setOnClickListener(this);

        packName = this.getPackageName();

        image1 = findViewById(R.id.image_1);
        image2 = findViewById(R.id.image_2);
        image3 = findViewById(R.id.image_3);
        image4 = findViewById(R.id.image_4);
        image5 = findViewById(R.id.image_5);
        image6 = findViewById(R.id.image_6);
        image7 = findViewById(R.id.image_7);
        image8 = findViewById(R.id.image_8);
        image9 = findViewById(R.id.image_9);
        image10 = findViewById(R.id.image_10);

        image1.setVisibility(View.GONE);
        image2.setVisibility(View.GONE);
        image3.setVisibility(View.GONE);
        image4.setVisibility(View.GONE);
        image5.setVisibility(View.GONE);
        image6.setVisibility(View.GONE);
        image7.setVisibility(View.GONE);
        image8.setVisibility(View.GONE);
        image9.setVisibility(View.GONE);
        image10.setVisibility(View.GONE);



        switch (guide_seq_int) {

            case 1 :
            // 일당등록방법
                wayRegiIldang();
                break;
            case 2 :
                // 일당매칭확인방법
                wayIldangMatch();
                break;

            case 10:
                // 오더 방법
                wayOrder();
                break;

            case 20:
                // 광고등록하는 방법
                wayRegisterAdver();
                break;
            case 21:
                // 광고확인 및 삭제방법
                wayAdverAndDelete();
                break;
        }

    }

    private void wayRegisterAdver() {
        resourceID = getResources().getIdentifier("@drawable/register_adv_1" , "id" , packName);
        image1.setImageResource(resourceID);
        image1.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/register_adv_2" , "id" , packName);
        image2.setImageResource(resourceID);
        image2.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/register_adv_3" , "id" , packName);
        image3.setImageResource(resourceID);
        image3.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/register_adv_4" , "id" , packName);
        image4.setImageResource(resourceID);
        image4.setVisibility(View.VISIBLE);
    }


    private void wayAdverAndDelete() {
        resourceID = getResources().getIdentifier("@drawable/my_adv_hist_1" , "id" , packName);
        image1.setImageResource(resourceID);
        image1.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/my_adv_hist_2" , "id" , packName);
        image2.setImageResource(resourceID);
        image2.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/my_adv_hist_3" , "id" , packName);
        image3.setImageResource(resourceID);
        image3.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/my_adv_hist_4" , "id" , packName);
        image4.setImageResource(resourceID);
        image4.setVisibility(View.VISIBLE);
    }


    private void wayIldangMatch() {
        resourceID = getResources().getIdentifier("@drawable/ildang_match_1" , "id" , packName);
        image1.setImageResource(resourceID);
        image1.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/ildang_match_2" , "id" , packName);
        image2.setImageResource(resourceID);
        image2.setVisibility(View.VISIBLE);
    }

    private void wayOrder() {
        resourceID = getResources().getIdentifier("@drawable/order_1" , "id" , packName);
        image1.setImageResource(resourceID);
        image1.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/order_2" , "id" , packName);
        image2.setImageResource(resourceID);
        image2.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/order_3" , "id" , packName);
        image3.setImageResource(resourceID);
        image3.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/order_4" , "id" , packName);
        image4.setImageResource(resourceID);
        image4.setVisibility(View.VISIBLE);
    }
    private void wayRegiIldang() {
        resourceID = getResources().getIdentifier("@drawable/regi_ildang_1" , "id" , packName);
        image1.setImageResource(resourceID);
        image1.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/regi_ildang_2" , "id" , packName);
        image2.setImageResource(resourceID);
        image2.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/regi_ildang_3" , "id" , packName);
        image3.setImageResource(resourceID);
        image3.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/regi_ildang_4" , "id" , packName);
        image4.setImageResource(resourceID);
        image4.setVisibility(View.VISIBLE);

        resourceID = getResources().getIdentifier("@drawable/regi_ildang_5" , "id" , packName);
        image5.setImageResource(resourceID);
        image5.setVisibility(View.VISIBLE);
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

    private void backActivity() {
        super.onBackPressed();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
