package com.banswara.warehouse.network

import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.*
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
	
	suspend fun callSignUpApi(pin: Long, deviceId: String, mobile: String, userName: String) {
		
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
	suspend fun callDeviceChange(pin: Long, deviceId: String, mobile: String, userName: String) {
		
		retrofitService.changeUserDevice(DeviceChangeRequestModel(true, pin, deviceId, mobile, userName))
			.enqueue(object : Callback<BaseResponseModel> {
				
				override fun onResponse(
					call: Call<BaseResponseModel>,
					response: Response<BaseResponseModel>
				) {
					try {
						response.body()?.let {
							apiLiveData.value = RequestType.DEVICE_CHANGE(it)
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
	
	suspend fun fetchFiles(useId: Int, deviceId: String) {
		
		retrofitService.fetchFiles(FetchFilesRequestModel(useId, deviceId))
			.enqueue(object : Callback<List<ChallanFileModel>> {
				
				override fun onResponse(
					call: Call<List<ChallanFileModel>>,
					response: Response<List<ChallanFileModel>>
				) {
					try {
						response.body()?.let {
							apiLiveData.value = RequestType.FETCH_FILES(it)
						}
					} catch (t: Throwable) {
						//set null list in case of crash
						apiLiveData.value = null
						t.printStackTrace()
					}
				}
				
				override fun onFailure(call: Call<List<ChallanFileModel>>, t: Throwable) {
					//set null list in case of failure
					apiLiveData.value = null
					t.printStackTrace()
				}
			})
	}
	
	suspend fun fetchContent(useId: Int, deviceId: String, fileName: String) {
		
		retrofitService.readFileData(ReadFileDataRequestModel(useId, deviceId, fileName))
			.enqueue(object : Callback<List<FileContentModel>> {
				
				override fun onResponse(
					call: Call<List<FileContentModel>>,
					response: Response<List<FileContentModel>>
				) {
					try {
						response.body()?.let {
							apiLiveData.value = RequestType.FETCH_FILE_CONTENT(it)
						}
					} catch (t: Throwable) {
						//set null list in case of crash
						apiLiveData.value = null
						t.printStackTrace()
					}
				}
				
				override fun onFailure(call: Call<List<FileContentModel>>, t: Throwable) {
					//set null list in case of failure
					apiLiveData.value = null
					t.printStackTrace()
				}
			})
	}
	
	suspend fun uploadScannedFile(useId: Int, deviceId: String, fileName: String) {
		
		retrofitService.processFile(ReadFileDataRequestModel(useId, deviceId, fileName))
			.enqueue(object : Callback<ProcessFileResponseModel> {
				
				override fun onResponse(
					call: Call<ProcessFileResponseModel>,
					response: Response<ProcessFileResponseModel>
				) {
					try {
						response.body()?.let {
							apiLiveData.value = RequestType.PROCESS_FILE(it)
						}
					} catch (t: Throwable) {
						//set null list in case of crash
						apiLiveData.value = null
						t.printStackTrace()
					}
				}
				
				override fun onFailure(call: Call<ProcessFileResponseModel>, t: Throwable) {
					//set null list in case of failure
					apiLiveData.value = null
					t.printStackTrace()
				}
			})
	}
	
	sealed class RequestType {
		data class LOGIN(val loginResponse: LoginResponseModel) : RequestType()
		data class SIGN_UP(val baseResponseModel: BaseResponseModel) : RequestType()
		data class DEVICE_CHANGE(val baseResponseModel: BaseResponseModel) : RequestType()
		data class FETCH_FILES(val fetchFilesResponseModel: List<ChallanFileModel>) : RequestType()
		data class FETCH_FILE_CONTENT(val fetchContentResponseModel: List<FileContentModel>) :
			RequestType()
		data class PROCESS_FILE(val processFileResponseModel: ProcessFileResponseModel) : RequestType()
		
	}
	
}





