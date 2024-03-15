package com.banswara.warehouse.login

import android.app.Application
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.R
import com.banswara.warehouse.utils.PreferenceManager

class LoginViewModel(val app: Application) : AndroidViewModel(app) {
	
	val userName = MutableLiveData<String>()
	val userNameError = MutableLiveData<String>()
	val mobileNumber = MutableLiveData<String>()
	val isLogin = MutableLiveData<Boolean>(false)
	val isDeviceNotRegistered = MutableLiveData<Boolean>(false)
	val loginEnable = MutableLiveData<Boolean>(false)
	val signInEnable = MutableLiveData<Boolean>(false)
	val pin = MutableLiveData<String>()
	val confirmPin = MutableLiveData<String>()
	val events = MutableLiveData<LoginEvents>()
	
	init {
		if (PreferenceManager.getUser() == null) {
			isLogin.value = false
		} else {
			PreferenceManager.getUser()?.let {
				it.isLogout?.let { logout ->
					isLogin.value = logout
				} ?: {
					isLogin.value = false
				}
			} ?: {
				isLogin.value = false
			}
		}
		
	}
	
	fun signIn(view: View) {
		if (checkSignInValidations(true)) {
			events.value = LoginEvents.SIGN_IN
		}
	}
	
	fun createAccount(view: View) {
		isLogin.value = false
		userName.value = ""
		mobileNumber.value = ""
		pin.value = ""
		confirmPin.value = ""
	}
	
	
	fun logIn(view: View) {
		if (checkLoginValidations(true)) {
			events.value = LoginEvents.LOGIN
		}
	}
	
	fun checkLoginValidations(showError: Boolean): Boolean {
		if (TextUtils.isEmpty(pin.value)) {
			if (showError)
				events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_pin))
			return false
		} else if (pin.value!!.length < 4) {
			if (showError)
				events.value =
					LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_4_digit_valid_pin))
			return false
		}
		return true
	}
	
	fun checkSignInValidations(showError: Boolean): Boolean {
		if (TextUtils.isEmpty(userName.value)) {
			if (showError)
				events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_user_name))
			return false
		} else if (TextUtils.isEmpty(mobileNumber.value)) {
			if (showError)
				events.value =
					LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_mobile_no))
			return false
		} else if (mobileNumber.value!!.length < 10) {
			if (showError)
				events.value =
					LoginEvents.SHOW_TOAST(app.getString(R.string.error_valid_mobile))
			return false
		} else if (TextUtils.isEmpty(pin.value)) {
			if (showError)
				events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_pin))
			return false
		} else if (pin.value!!.length < 4) {
			if (showError)
				events.value =
					LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_4_digit_valid_pin))
			return false
		} else if (TextUtils.isEmpty(confirmPin.value)) {
			if (showError)
				events.value =
					LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_confirm_pin))
			return false
		} else if (confirmPin.value!!.length < 4) {
			if (showError)
				events.value =
					LoginEvents.SHOW_TOAST(app.getString(R.string.error_enter_4_digit_valid_confirm_pin))
			return false
		} else if (!confirmPin.value.equals(pin.value)) {
			if (showError)
				events.value = LoginEvents.SHOW_TOAST(app.getString(R.string.pin_not_match))
			return false
		}
		
		return true
	}
	sealed class LoginEvents {
		
		data object LOGIN : LoginEvents()
		data object SIGN_IN : LoginEvents()
		data class SHOW_TOAST(val message: String) : LoginEvents()
	}
}