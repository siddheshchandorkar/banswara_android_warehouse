package com.banswara.warehouse.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {
	
	private const val name = "warehouse_sp"
	
	private const val KEY_USER_DETAILS = "user_details"
	private const val KEY_DMT_NEW_EXPERIENCE_SHOWED = "dmt_new_ui_experience_showed"
	
	private var sSharedPreference: SharedPreferences? = null
	
	fun getSharedPreference(context: Context): SharedPreferences? {
		if(sSharedPreference == null){
			sSharedPreference = context.getSharedPreferences(name, 0)
		}
		return sSharedPreference
	}
	
	
	
}