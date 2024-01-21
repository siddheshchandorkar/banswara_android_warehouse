package com.banswara.warehouse.network

import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*
* All the api calls should handled here
* */
class RetrofitRepository {
    private val retrofitService = RetrofitFactory.makeRetrofitService()
    var signupModelLiveData = MutableLiveData<BaseResponseModel?>()

    private object HOLDER {
        val INSTANCE = RetrofitRepository()
    }

    companion object {
        val instance: RetrofitRepository by lazy { HOLDER.INSTANCE }
    }


    suspend fun callLoginApi() {

        retrofitService.signUp().enqueue(object : Callback<BaseResponseModel> {

            override fun onResponse(
                call: Call<BaseResponseModel>,
                response: Response<BaseResponseModel>
            ) {
                try {
                    response.body()?.let {
                        signupModelLiveData.value = it
                    }
                } catch (t: Throwable) {
                    //set null list in case of crash
                    signupModelLiveData.value = null
                    t.printStackTrace()
                }
            }

            override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
                //set null list in case of failure
                signupModelLiveData.value = null
                t.printStackTrace()
            }
        })
    }
//    suspend fun transaction(uid:String,amount: Int, serviceId: Int) {
//        val transactionRequestModel= TransactionRequestModel(amount,serviceId)
//        retrofitService.transaction(uid,transactionRequestModel).enqueue(object : Callback<BaseResponseModel> {
//
//            override fun onResponse(
//                call: Call<BaseResponseModel>,
//                response: Response<BaseResponseModel>
//            ) {
//                try {
//                    response.body()?.let {
//                        signupModelLiveData.value = it
//                    }
//                } catch (t: Throwable) {
//                    //set null list in case of crash
//                    signupModelLiveData.value = null
//                    t.printStackTrace()
//                }
//            }
//
//            override fun onFailure(call: Call<BaseResponseModel>, t: Throwable) {
//                //set null list in case of failure
//                signupModelLiveData.value = null
//                t.printStackTrace()
//            }
//        })
//    }
}





