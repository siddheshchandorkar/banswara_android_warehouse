package com.banswara.warehouse.network

import com.banswara.warehouse.model.BaseResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

/*
* Retrofit Api Service
* */
interface RetrofitService {


    @GET("/pnb/v1/Agent/RetailerProgramMaster")
    fun signUp(): Call<BaseResponseModel>
//    @POST("signup")
//    fun signUp(@Body signUpRequest: UserModel): Call<BaseResponseModel>
//
//    @POST("retailer-goal")
//    fun goal(@Header("resource-owner-uid")  userkey: String, @Body goal: GoalRequestModel): Call<BaseResponseModel>
//
//    @POST("retailer-transaction")
//    fun transaction(@Header("resource-owner-uid")  userkey: String,@Body goal: TransactionRequestModel): Call<BaseResponseModel>
}