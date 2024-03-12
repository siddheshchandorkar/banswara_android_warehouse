package com.banswara.warehouse.success

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.databinding.ActivitySuccessBinding

class SuccessActivity : AppCompatActivity() {
	private lateinit var viewModel: SuccessViewModel
	private lateinit var binding: ActivitySuccessBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_success)
		
		intent?.let {
			val from = it.getBooleanExtra(KEY_FROM_LOGIN, false)
			var file =""
				if(it.hasExtra(KEY_FILE_NAME)){
					file = it.getStringExtra(KEY_FILE_NAME)?:""
				}
			viewModel = SuccessViewModel(isLoginSuccess = from, app = application, file = file)
		}
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		binding.llLoginBack.setOnClickListener {
			finish()
		}
		
	}
	
	companion object{
		const val KEY_FROM_LOGIN ="from"
		const val KEY_FILE_NAME ="file_name"
	}
	
	
}