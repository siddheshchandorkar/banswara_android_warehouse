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
			viewModel = SuccessViewModel(from,application)
		}
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		binding.llLoginBack.setOnClickListener {
			finish()
		}
		
	}
	
	companion object{
		const val KEY_FROM_LOGIN ="from"
	}
	
	
}