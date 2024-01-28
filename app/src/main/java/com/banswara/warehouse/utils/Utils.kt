package com.banswara.warehouse.utils

import android.content.ContentResolver
import android.provider.Settings

object Utils{
	fun getDeviceId(contentResolver: ContentResolver): String {
		return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
	}
}
