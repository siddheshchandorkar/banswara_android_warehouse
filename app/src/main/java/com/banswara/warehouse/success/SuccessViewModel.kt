package com.banswara.warehouse.success

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel

class SuccessViewModel(isLoginSuccess : Boolean, app : Application): AndroidViewModel(app) {
	
	val isFromLogin = ObservableField(isLoginSuccess)
	val buttonText = ObservableField("")
	val desc = ObservableField("")
	
	init {
		if (isLoginSuccess){
			buttonText.set("Back To Login")
			desc.set("Your account has been created successfully!")
		}else{
			buttonText.set("Back To Homepage")
			desc.set("Items shipped to Ramesh Trader successfully!")
		}
	}
}