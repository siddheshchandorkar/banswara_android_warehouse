package com.banswara.warehouse.login

import android.app.Application
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.R

class LoginViewModel(val app : Application) : AndroidViewModel(app) {
	
	val userName = MutableLiveData<String>()
	val userNameError = MutableLiveData<String>()
	val mobileNumber = MutableLiveData<String>()
	val isLogin = MutableLiveData<Boolean>(true)
	val isSignInSuccess = MutableLiveData<Boolean>(false)
	val deviceId = MutableLiveData<String>()
	val pin = MutableLiveData<String>()
	val events = MutableLiveData<LoginEvents>()
	
	
	fun signIn(view: View) {
		if (checkSignInValidations()) {
			events.value = LoginEvents.SIGN_IN
		}
	}
	
	fun createAccount(view: View) {
		isLogin.value = false
	}
	
	
	
	fun logIn(view: View) {
		if(checkLoginValidations()){
			events.value = LoginEvents.LOGIN
		}
	}
	
	private fun checkLoginValidations(): Boolean {
		if (TextUtils.isEmpty(userName.value)) {
			events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_user_name))
			return false
		}else if (TextUtils.isEmpty(pin.value)) {
			events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_pin))
			return false
		}else if (pin.value!!.length< 4) {
			events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_4_digit_valid_pin))
			return false
		}
		return true
	}
	
	private fun checkSignInValidations(): Boolean {
		if (TextUtils.isEmpty(userName.value)) {
			events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_user_name))
			return false
		}else if (TextUtils.isEmpty(mobileNumber.value)) {
			events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_mobile_no))
			return false
		}else if (mobileNumber.value!!.length< 10) {
			events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_valid_mobile))
			return false
		}else if (TextUtils.isEmpty(pin.value)) {
			events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_pin))
			return false
		}else if (pin.value!!.length< 4) {
			events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_4_digit_valid_pin))
			return false
		}
		
		return true
	}
	
	fun backToLogin(view: View) {
		isLogin.value = true
		isSignInSuccess.value = false
	}
	
	sealed class LoginEvents {
		
		data object LOGIN : LoginEvents()
		data object SIGN_IN : LoginEvents()
		data class SHOW_TOAST(val message: String) : LoginEvents()
	}
}