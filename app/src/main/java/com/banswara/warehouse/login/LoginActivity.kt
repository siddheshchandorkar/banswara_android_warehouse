package com.banswara.warehouse.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.dashboard.DashboardActivity
import com.banswara.warehouse.databinding.ActivityLoginBinding
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.utils.PreferenceManager
import com.banswara.warehouse.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
	private lateinit var viewModel: LoginViewModel
	private lateinit var binding: ActivityLoginBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
		viewModel = LoginViewModel(application)
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		RetrofitRepository.instance.apiLiveData.observe(this) {
			when (it) {
				is RetrofitRepository.RequestType.DEVICE_CHANGE -> {
					Toast.makeText(this, "Device Changed Successfully", Toast.LENGTH_LONG).show()
				}
				
				is RetrofitRepository.RequestType.LOGIN -> {
					if (it.baseResponseModel is LoginResponseModel) {
						Toast.makeText(this, it.baseResponseModel.errorMsg, Toast.LENGTH_LONG)
							.show()
						PreferenceManager.saveUser(it.baseResponseModel)
					}
					
				}
				
				is RetrofitRepository.RequestType.SIGN_UP -> {
					viewModel.isSignInSuccess.value = true
					Toast.makeText(this, it.baseResponseModel.errorMsg, Toast.LENGTH_LONG).show()
				}
				
				else -> {
					Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show()
				}
			}
		}
		
		viewModel.events.observe(this) {
			when (it) {
				LoginViewModel.LoginEvents.LOGIN -> {
					
					CoroutineScope(Dispatchers.IO).launch {
						RetrofitRepository.instance.callLoginApi(
							viewModel.pin.value!!.toLong(),
							Utils.getDeviceId(contentResolver)
						)
					}
				}
				
				is LoginViewModel.LoginEvents.SHOW_TOAST -> Toast.makeText(
					this,
					it.message,
					Toast.LENGTH_LONG
				).show()
				
				LoginViewModel.LoginEvents.SIGN_IN -> {
					CoroutineScope(Dispatchers.IO).launch {
						RetrofitRepository.instance.callSignUpApi(
							viewModel.pin.value!!.toLong(),
							Utils.getDeviceId(contentResolver),
							viewModel.mobileNumber.value!!,
							viewModel.userName.value!!
						)
					}
				}
			
				
			}
		}
	}
}