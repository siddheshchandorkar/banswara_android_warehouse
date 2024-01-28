package com.banswara.warehouse.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
* Retrofit Service Class
*/
object RetrofitFactory {
	
	private const val BASE_URL = "https://f1651b69-29b0-4b1d-b9ff-2fb1e8be869e.mock.pstmn.io/" //TODO Siddhesh this is mock server url
	fun makeRetrofitService(): RetrofitService {
		val interceptor = HttpLoggingInterceptor()
		interceptor.level = HttpLoggingInterceptor.Level.BODY
		val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
		
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			.build().create(RetrofitService::class.java)
	}
	
	
}