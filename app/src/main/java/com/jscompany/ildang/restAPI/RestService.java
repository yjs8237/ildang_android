package com.jscompany.ildang.restAPI;

import android.graphics.Point;

import com.google.gson.JsonObject;
import com.jscompany.ildang.model.AdverModel;
import com.jscompany.ildang.model.CodeModel;
import com.jscompany.ildang.model.IldangModel;
import com.jscompany.ildang.model.IlgamModel;
import com.jscompany.ildang.model.NotificationModel;
import com.jscompany.ildang.model.PointModel;
import com.jscompany.ildang.model.QnaModel;
import com.jscompany.ildang.model.UserInfoModel;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {

    @GET("/")
    Call<ResponseBody> getResponse (@Path("cell_no") String cell_no );

    @POST("user/connect")
    Call<JsonObject> getConnectCount(@Body UserInfoModel model);

    @POST("user/getinfo")
    Call<JsonObject> getUserInfo(@Body UserInfoModel model);

    @POST("user/register")
    Call<JsonObject> registerUser(@Body UserInfoModel model);

    @POST("user/changeinfo")
    Call<JsonObject> updateUserInfo(@Body UserInfoModel model);

    @POST("user/changePassword")
    Call<JsonObject> changePassword(@Body UserInfoModel model);

    @POST("user/login")
    Call<JsonObject> login(@Body UserInfoModel model);

    @POST("user/token")
    Call<JsonObject> token(@Body UserInfoModel model);

    // 일당 매칭 내역 가져오기
    @POST("ildang/mymatchlist")
    Call<JsonObject> mymatchlist(@Body IldangModel model);

    // 공지사항 리스트 가져오기
    @POST("noti/requestlist")
    Call<JsonObject> notilist(@Body NotificationModel model);



    // 지역 정보 가져오기 ( 일당등록 )
    @POST("code/location")
    Call<JsonObject> getLocationList(@Body CodeModel model);

    // 일당매칭하기
    @POST("ildang/match")
    Call<JsonObject> requestIldangMatch(@Body IldangModel model);

    // 오더주에게 연락하기
    @POST("ildang/ordercontact")
    Call<JsonObject> ordercontact(@Body IldangModel model);

    // 일당 취소
    @POST("ildang/cancleildang")
    Call<JsonObject> cancleildang(@Body IldangModel model);

    // 일당등록
    @POST("ildang/register")
    Call<JsonObject> registerIldang(@Body IldangModel model);

    // 일당 등록 건수 가져오기
    @POST("ildang/count")
    Call<JsonObject> selectIldangCount(@Body IldangModel model);

    // 일당 기술자 목록 가져오기
    @POST("ildang/ildanglist")
    Call<JsonObject> selectIldangList(@Body IldangModel model);

    // 일당 기술자 목록 가져오기
    @POST("user/pointrequest")
    Call<JsonObject> pointrequest(@Body PointModel model);




    // 포인트 내역 가져오기
    @POST("user/pointlist")
    Call<JsonObject> pointlist(@Body PointModel model);

    // 문의 등록하기
    @POST("user/qnarequest")
    Call<JsonObject> qnarequest(@Body QnaModel model);

    // 문의 내역 조회
    @POST("user/qnalist")
    Call<JsonObject> qnalist(@Body QnaModel model);

    // 문의 내역 상세 조회
    @POST("user/qnadetail")
    Call<JsonObject> qnadetail(@Body QnaModel model);

    // 광고등록하기
    @POST("adv/registerAdver")
    Call<JsonObject> registerAdver(@Body AdverModel model);

    // 광고 리스트 가져오기
    @POST("adv/list")
    Call<JsonObject> adverList();

    // 나의 광고 리스트 가져오기
    @POST("adv/mylist")
    Call<JsonObject> mylist(@Body AdverModel model);

    // 일감나누기 리스트 가져오기
    @POST("adv/ilgamlist")
    Call<JsonObject> ilgamlist(@Body AdverModel model);

    // 광고 상세내용 가져오기
    @POST("adv/detail")
    Call<JsonObject> adverDetail(@Body AdverModel model);

    // 광고 삭제
    @POST("adv/delete")
    Call<JsonObject> deleteAdver(@Body AdverModel model);

}
