package com.banswara.warehouse.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date

object Utils{
	fun getDeviceId(contentResolver: ContentResolver): String {
//		return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
		return "galaxys242023"
	}
	
	fun currentDataInFormat(format: String): String {
		try {
			return SimpleDateFormat(format).format(Date())
			
		} catch (e: Exception) {
			e.printStackTrace()
			return ""
		}
	}
	
	fun isInternetConnected(application: Application): Boolean {
		val networkConnected = isNetworkAvailable(application)
		if (!networkConnected) (
				Toast.makeText( application,
					"No internet, Please check your connection",
					Toast.LENGTH_SHORT).show()
				)
		return networkConnected
	}
	
	/**
	 * Don't use this method in other classes, use isInternetConnected method instead
	 */
	fun isNetworkAvailable(context: Context?): Boolean {
		val connectivityManager =
			context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val activeNetworkInfo = connectivityManager.activeNetworkInfo
		return activeNetworkInfo != null && activeNetworkInfo.isConnected
	}
	
}
