package com.banswara.warehouse.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
* Retrofit Service Class
*/
object RetrofitFactory {
	
//	private const val BASE_URL = "https://a8f330ca-466d-4fe5-86bf-ce1cff788cb4.mock.pstmn.io" //TODO Siddhesh this is mock server url
	private const val BASE_URL = "https://1187461a-eb7c-45eb-8d6c-ae472c3a64de.mock.pstmn.io" //TODO Siddhesh this is mock server url
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