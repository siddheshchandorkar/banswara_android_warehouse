package com.banswara.warehouse.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.dashboard.DashboardActivity
import com.banswara.warehouse.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
	private lateinit var viewModel: LoginViewModel
	private lateinit var binding: ActivityLoginBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
		viewModel = LoginViewModel()
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
//		binding.btnSignIn.setOnClickListener {
//			Toast.makeText(this, viewModel.userName.value+","+viewModel.mobileNumber.value+","+viewModel.pin.value+",", Toast.LENGTH_LONG).show()
//		}
		
		viewModel.events.observe(this) {
			when (it) {
				LoginViewModel.LoginEvents.LOGIN -> TODO()
				is LoginViewModel.LoginEvents.SHOW_TOAST -> TODO()
				LoginViewModel.LoginEvents.SIGN_IN -> startActivity(
					Intent(
						this,
						DashboardActivity::class.java
					)
				)
			}
		}
	}
}