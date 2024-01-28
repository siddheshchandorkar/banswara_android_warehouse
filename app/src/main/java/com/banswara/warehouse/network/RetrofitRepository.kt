package com.banswara.warehouse.network

import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.LoginRequestModel
import com.banswara.warehouse.model.SignUpRequestModel
import com.banswara.warehouse.model.BaseResponseModel
import com.banswara.warehouse.model.LoginResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*
* All the api calls should handled here
*/
class RetrofitRepository {
	private val retrofitService = RetrofitFactory.makeRetrofitService()
	var apiLiveData = MutableLiveData<RequestType?>()
	
	private object HOLDER {
		val INSTANCE = RetrofitRepository()
	}
	
	companion object {
		val instance: RetrofitRepository by lazy { HOLDER.INSTANCE }
	}
	
	
	suspend fun callLoginApi(pin: Long, deviceId: String) {
		
		retrofitService.login(LoginRequestModel(pin, deviceId))
			.enqueue(object : Callback<LoginResponseModel> {
				
				override fun onResponse(
					call: Call<LoginResponseModel>,
					response: Response<LoginResponseModel>
				) {
					try {
						response.body()?.let {
							apiLiveData.value = RequestType.LOGIN(it)
						}
					} catch (t: Throwable) {
						//set null list in case of crash
						apiLiveData.value = null
						t.printStackTrace()
					}
				}
				
				override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
					//set null list in case of failure
					apiLiveData.value = null
					t.printStackTrace()
				}
			})
	}
	
	suspend fun callSignUpApi(pin: Long, deviceId: String,mobile: String, userName: String) {
		
		retrofitService.signUp(SignUpRequestModel(true, pin, deviceId, mobile, userName))
			.enqueue(object : Callback<BaseResponseModel> {
				
				override fun onResponse(
					call: Call<BaseResponseModel>,
					response: Response<BaseResponseModel>
				) {
					try {
						response.body()?.let {
							apiLiveData.value = RequestType.SIGN_UP(it)
						}
					} catch (t: Throwable) {
						//set null list in case of crash
						apiLiveData.value = null
						t.printStackTrace()
					}
				}
				
				override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
					//set null list in case of failure
					apiLiveData.value = null
					t.printStackTrace()
				}
			})
	}
	
	sealed class RequestType {
		 data class LOGIN(val baseResponseModel: BaseResponseModel) : RequestType()
		 data class SIGN_UP(val baseResponseModel: BaseResponseModel) : RequestType()
		 data class DEVICE_CHANGE(val baseResponseModel: BaseResponseModel) : RequestType()
	}
	
}





