package com.banswara.warehouse.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/*
* Retrofit Service Class
*/
object RetrofitFactory {
	
//	private const val BASE_URL = "https://a8f330ca-466d-4fe5-86bf-ce1cff788cb4.mock.pstmn.io" //TODO Siddhesh this is mock server url
//	private const val BASE_URL = "https://1187461a-eb7c-45eb-8d6c-ae472c3a64de.mock.pstmn.io" //TODO Siddhesh this is mock server url
//	private const val BASE_URL = "https://9d826a18-c475-4e8b-8611-5d96c30bde6d.mock.pstmn.io" //TODO Siddhesh this is mock server url
	private const val BASE_URL = "https://103.67.180.170:8021"
	fun makeRetrofitService(): RetrofitService {
		val interceptor = HttpLoggingInterceptor()
		interceptor.level = HttpLoggingInterceptor.Level.BODY
//		val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
		val okHttpClient = getUnsafeOkHttpClient().addInterceptor(interceptor)
		okHttpClient.readTimeout(60, TimeUnit.SECONDS);
		okHttpClient.connectTimeout(60, TimeUnit.SECONDS);
		val client = okHttpClient.build()
		
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.client(client)
			
			.build().create(RetrofitService::class.java)
	}
	private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
		return try {
			// Create a trust manager that does not validate certificate chains
			val trustAllCerts = arrayOf<TrustManager>(
				object : X509TrustManager {
					@Throws(CertificateException::class)
					override fun checkClientTrusted(
						chain: Array<X509Certificate>,
						authType: String
					) {
					}
					
					@Throws(CertificateException::class)
					override fun checkServerTrusted(
						chain: Array<X509Certificate>,
						authType: String
					) {
					}
					
					override fun getAcceptedIssuers(): Array<X509Certificate> {
						return arrayOf()
					}
				}
			)
			
			// Install the all-trusting trust manager
			val sslContext = SSLContext.getInstance("SSL")
			sslContext.init(null, trustAllCerts, SecureRandom())
			
			// Create an ssl socket factory with our all-trusting manager
			val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
			val builder = OkHttpClient.Builder()
			builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
			builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
			builder
		} catch (e: Exception) {
			throw RuntimeException(e)
		}
	}
	
	
}