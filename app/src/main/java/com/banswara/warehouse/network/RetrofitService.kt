package com.banswara.warehouse.network

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
	
	@POST("/Api/AppApi/Fn_Login")
	fun login(@Body loginRequestModel: LoginRequestModel): Call<LoginResponseModel>
	
	//Binning Process
	@POST("/Api/AppApi/Fn_Insert_Challan")
	fun uploadBinningFile(@Body uploadRequestModel: UploadRequestModel): Call<BaseResponseModel>
	
	@POST("/Api/AppApi/Fn_Insert_Challan")
	fun fetchChallan(@Body uploadRequestModel: UploadRequestModel): Call<BaseResponseModel>
	
	@POST("/Api/AppApi/Fn_Read_Directory_File")
	fun fetchFiles(@Body fetchFilesRequestModel: FetchFilesRequestModel): Call<List<ChallanFileModel>>
	
	@POST("/Api/AppApi/Fn_Read_Text_File")
	fun readFileData(@Body readFileDataRequestModel: ReadFileDataRequestModel): Call<List<FileContentModel>>
	
	//Dispatch api
	@POST("/Api/AppApi/Fn_Copy_File_Process")
	fun dispatchFile(@Body readFileDataRequestModel: ReadFileDataRequestModel): Call<ProcessFileResponseModel>
	
	@POST("/Api/AppApi/Fn_Change_User_Device")
	fun changeUserDevice(@Body deviceChangeRequestModel: DeviceChangeRequestModel): Call<BaseResponseModel>
	
}