package com.banswara.warehouse.utils

import android.content.ContentResolver
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.Date

object Utils{
	fun getDeviceId(contentResolver: ContentResolver): String {
//		return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
		return "siddhesh#321" //TODO Siddhesh revert
	}
	
	fun currentDataInFormat(format: String): String {
		try {
			return SimpleDateFormat(format).format(Date())
			
		} catch (e: Exception) {
			e.printStackTrace()
			return ""
		}
	}
}
