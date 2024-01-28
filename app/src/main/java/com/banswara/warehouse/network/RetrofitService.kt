package com.banswara.warehouse.network

import com.banswara.warehouse.dashboard.FetchFilesRequestModel
import com.banswara.warehouse.dashboard.ReadFileDataRequestModel
import com.banswara.warehouse.login.DeviceChangeRequestModel
import com.banswara.warehouse.login.LoginRequestModel
import com.banswara.warehouse.login.SignUpRequestModel
import com.banswara.warehouse.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/*
* Retrofit Api Service
* */
interface RetrofitService {
	
	@POST("/Api/AppApi/Fn_Insert_User")
	fun signUp(@Body signUpRequestModel: SignUpRequestModel): Call<BaseResponseModel>
	
	@POST("/Api/AppApi/Fn_Fetch_User")
	fun login(@Body loginRequestModel: LoginRequestModel): Call<LoginResponseModel>
	
	@POST("/Api/AppApi/Fn_Read_Directory_File")
	fun fetchFiles(@Body fetchFilesRequestModel: FetchFilesRequestModel): Call<FetchFilesResponseModel>
	
	@POST("/Api/AppApi/Fn_Read_Text_File")
	fun readFileData(@Body readFileDataRequestModel: ReadFileDataRequestModel): Call<FetchFilesResponseModel>
	
	@POST("/Api/AppApi/Fn_Copy_File_Process")
	fun processFile(@Body readFileDataRequestModel: ReadFileDataRequestModel): Call<ProcessFileResponseModel>
 
	@POST("/Api/AppApi/Fn_Change_User_Device")
	fun changeUserDevice(@Body deviceChangeRequestModel: DeviceChangeRequestModel): Call<LoginResponseModel>
	
}