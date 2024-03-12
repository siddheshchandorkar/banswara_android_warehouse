package com.banswara.warehouse.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.dashboard.DashboardActivity
import com.banswara.warehouse.database.WareHouseDB
import com.banswara.warehouse.databinding.ActivityLoginBinding
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.utils.PreferenceManager
import com.banswara.warehouse.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
	private lateinit var viewModel: LoginViewModel
	private lateinit var binding: ActivityLoginBinding
	
	private var successLauncher = registerForActivityResult<Intent, ActivityResult>(
		ActivityResultContracts.StartActivityForResult()
	) { result: ActivityResult? ->
		startActivity(Intent(this, DashboardActivity::class.java))
		finish()
		
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
		viewModel = LoginViewModel(application)
		binding.vm = viewModel
		binding.lifecycleOwner = this
		RetrofitRepository.instance.apiLiveData.value = RetrofitRepository.RequestType.DEFAULT
		setObservers()
		
	}
	
	private fun setObservers() {
		
		viewModel.userName.observe(this) {
			if (viewModel.isLogin.value!!) {
				viewModel.loginEnable.value = viewModel.checkLoginValidations(false)
			} else {
				viewModel.signInEnable.value = viewModel.checkSignInValidations(false)
			}
			
		}
		viewModel.mobileNumber.observe(this) {
			viewModel.signInEnable.value = viewModel.checkSignInValidations(false)
		}
		viewModel.pin.observe(this) {
			if (viewModel.isLogin.value!!) {
				viewModel.loginEnable.value = viewModel.checkLoginValidations(false)
			} else {
				viewModel.signInEnable.value = viewModel.checkSignInValidations(false)
			}
		}
		
		onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				if(!viewModel.isLogin.value!!) {
					viewModel.isLogin.value = true
				}else{
					finish()
				}
			}
		})
		
		//Login Screen Events Observers
		viewModel.events.observe(this) {
			when (it) {
				LoginViewModel.LoginEvents.LOGIN -> {
					if(Utils.isInternetConnected(application)){
						
						CoroutineScope(Dispatchers.IO).launch {
						RetrofitRepository.instance.callLoginApi(
							viewModel.pin.value!!.toLong(),
							Utils.getDeviceId(contentResolver)
						)
					}
					}
				}
				
				is LoginViewModel.LoginEvents.SHOW_TOAST -> Toast.makeText(
					this,
					it.message,
					Toast.LENGTH_LONG
				).show()
				
				LoginViewModel.LoginEvents.SIGN_IN -> {
					if(Utils.isInternetConnected(application)){
						CoroutineScope(Dispatchers.IO).launch {
							if(viewModel.isDeviceNotRegistered.value == false){
								RetrofitRepository.instance.callSignUpApi(
									viewModel.pin.value!!.toLong(),
									Utils.getDeviceId(contentResolver),
									viewModel.mobileNumber.value!!,
									viewModel.userName.value!!
								)
							}else{
								RetrofitRepository.instance.callDeviceChange(
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
		
		//Api Events Observer
		RetrofitRepository.instance.apiLiveData.observe(this) {
			when (it) {
				is RetrofitRepository.RequestType.DEVICE_CHANGE -> {
					if(it.baseResponseModel.errorMsg.equals("Change device id successfully.", true)){
						Toast.makeText(this, "Change device id successfully. Please login with PIN.", Toast.LENGTH_LONG).show()
						viewModel.isLogin.value =true
						viewModel.pin.value = ""
					}else{
						Toast.makeText(this, it.baseResponseModel.errorMsg, Toast.LENGTH_LONG).show()
						
					}
				}
				
				is RetrofitRepository.RequestType.LOGIN -> {
					
					if (it.loginResponse.errorMsg.equals("Success", true)) {
						WareHouseDB.getDataBase(this)?.wareHouseDao()?.insert(it.loginResponse)
						
						Toast.makeText(this, "Login Success", Toast.LENGTH_LONG)
							.show()
						PreferenceManager.saveUser(it.loginResponse)
						PreferenceManager.getUser()?.let {
							startActivity(Intent(this, DashboardActivity::class.java))
							finish()
						}
					} else if (it.loginResponse.errorMsg.equals(
							"Device id is not register",
							true
						)
					) {
						viewModel.pin.value = ""
						viewModel.isLogin.value = false
						viewModel.isDeviceNotRegistered.value  = true
						Toast.makeText(
							this,
							"This device not registered. Please Sign Up with this device",
							Toast.LENGTH_LONG
						).show()
						
					} else {
						Toast.makeText(this, it.loginResponse.errorMsg, Toast.LENGTH_LONG)
							.show()
					}
					
					
				}
				
				is RetrofitRepository.RequestType.SIGN_UP -> {
					if(it.baseResponseModel.errorMsg.equals("User added successfully.")){
						Toast.makeText(this, "Sign-Up Successful, Please login now", Toast.LENGTH_LONG)
							.show()
						viewModel.isLogin.value = true
						viewModel.userName.value = ""
						viewModel.pin.value = ""
					}else{
						Toast.makeText(this, it.baseResponseModel.errorMsg, Toast.LENGTH_LONG)
							.show()
					}
					
				}
				
				else -> {
					Toast.makeText(this, "Please Try Again", Toast.LENGTH_LONG).show()
				}
			}
		}
		
	}
	
}