package com.banswara.warehouse.login

import android.text.TextUtils
import android.view.View
import androidx.lifecycle.MutableLiveData

class LoginViewModel {
	
	val userName = MutableLiveData<String>()
	val userNameError = MutableLiveData<String>()
	val mobileNumber = MutableLiveData<String>()
	val deviceId = MutableLiveData<String>()
	val pin = MutableLiveData<String>()
	val events = MutableLiveData<LoginEvents>()
	
	
	fun signIn(view: View){
		if(checkSignInValidations()){
			events.value = LoginEvents.SIGN_IN
		}
	}
	
	private fun checkSignInValidations(): Boolean {
		if(TextUtils.isEmpty(userName.value)){
			userNameError.value = "Please Enter User"
			return false
		}
		
		return true
	}
	
	fun logIn(view: View){
		events.value = LoginEvents.LOGIN
	}
	
	sealed class LoginEvents{
		
		data object LOGIN : LoginEvents()
		data object SIGN_IN : LoginEvents()
		data class SHOW_TOAST(val message : String) : LoginEvents()
	}
}