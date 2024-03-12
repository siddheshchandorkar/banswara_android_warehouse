package com.banswara.warehouse.utils

import android.content.Context
import android.content.SharedPreferences
import com.banswara.warehouse.model.LoginResponseModel
import com.google.gson.Gson

object PreferenceManager {
	
	private const val name = "warehouse_sp"
	
	private const val KEY_USER_DETAILS = "user_details"
	
	private var sSharedPreference: SharedPreferences? =null
	
	fun init(context: Context) {
		if(sSharedPreference == null){
			sSharedPreference = context.getSharedPreferences(name, 0)
		}
	}
	
	fun saveUser(user: LoginResponseModel?){
		user?.let {
			val userString = Gson().toJson(user, LoginResponseModel::class.java)
			sSharedPreference?.edit()?.putString(KEY_USER_DETAILS,userString)?.apply()
		}?:run {
			sSharedPreference?.edit()?.putString(KEY_USER_DETAILS,"")?.apply()
		}
	}
	fun logout(){
		
		val user = getUser()
		user?.isLogout = true
		saveUser(user)
	}
	
	fun getUser(): LoginResponseModel? {
		return Gson().fromJson(
			sSharedPreference?.getString(KEY_USER_DETAILS, ""),
			LoginResponseModel::class.java
		)
	}
	
	
}