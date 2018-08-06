package com.ismartautocare.service;

import com.ismartautocare.Item;
import com.ismartautocare.SOAnswersMember;
import com.ismartautocare.SOMemberType;
import com.ismartautocare.SOResponseOnService;
import com.ismartautocare.SOResponseOnServiceStatus;
import com.ismartautocare.SOResponseOnServiceTest;
import com.squareup.moshi.ToJson;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by programmer on 1/10/18.
 */

public interface SOService {

//    @Headers({"Content-Type : application/x-www-form-urlencoded","Authorization : application/x-www-form-urlencoded"})
    @GET("/api/car")
    Call<SOAnswersResponse> getCar(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader);

    @GET("/api/members/type")
    Call<SOMemberType> getMemberType(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader);

    @GET("/api/members")
    Call<SOResponseOnService> getMember(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader);

    @GET("/api/services")
    Call<SOAnswersResponse> getServices(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader);

    @GET("/api/onservices")
    Call<SOResponseOnServiceTest> getOnServices(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader);

    @GET("/api/profile")
    Call<ResponseObject> getProfile(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader);

    @GET("/api/members")
    Call<SOAnswersMember> getMemberPlate(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader, @Query("plate") String plate);

    @GET("/api/members")
    Call<SOAnswersMember> getMemberPlateMember(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader, @Query("query") String plate);

    @GET("/api/members")
    Call<SOAnswersResponse> getMemberPlateSearch(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader,@Query("plate") String plate);

    @GET("/api/members_detail")
    Call<ResponseObject> getMemberDeatil(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader,@Query("id") String plate);

    @GET("/api/onservices/history")
    Call<SOResponseOnService> getOnServiceHistory(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader,@Query("date") String date);

    @GET("/api/members_detail")
    Call<SOResponseOnService> getMemberHistory(@Header("Content-Type") String content_type, @Header("Authorization") String authHeader,@Query("id") String member);

    @FormUrlEncoded
    @POST("/api/oauth/token/")
    Call<Item> Login(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("grant_type") String grant_type,
            @Field("username") String uname,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/api/onservices/")
    Call<ResponseObject> Onservices(
            @Header("Authorization") String authHeader,
            @Field("plate_number") String plate_number,
            @Field("plate_province") String plate_province,
            @Field("driver_name") String driver_name,
            @Field("driver_phone") String driver_phone,
            @Field("car_model") String car_model,
            @Field("plate_id") String plate_id,
            @Field("member_id") String member_id,
            @Field("subtotal") String subtotal,
            @Field("discount") String discount,
            @Field("discount_detail") String discount_detail,
            @Field("total") String total,
            @Field("served_by") String served_by,
            @Field("payment_by") String payment_by,
            @Field("services") ArrayList<String> services,
            @Field("price") ArrayList<Double>  price,
            @Field("period") ArrayList<Integer>  period
    );

    @FormUrlEncoded
    @POST("/api/onservices/status/")
    Call<SOResponseOnServiceStatus> OnservicesStatus(
            @Header("Authorization") String authHeader,
            @Field("id") String onservices,
            @Field("status") String status,
            @Field("sms") Boolean sms
    );

    @FormUrlEncoded
    @POST("/api/members/create/")
    Call<ResponseObject> CreateMembers(
            @Header("Content-Type") String content_type,
            @Header("Authorization") String authHeader,
            @Field("code") String code,
            @Field("name") String name,
            @Field("contact") String contact,
            @Field("email") String email,
            @Field("dob") String dob,
            @Field("address") String address,
            @Field("memberTypeID") String memberTypeID,
            @Field("size") String size,
            @Field("paidby") String paidby,
            @Field("sms") Boolean sms
    );

}