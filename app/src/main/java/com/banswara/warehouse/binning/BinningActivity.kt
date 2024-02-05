package com.banswara.warehouse.binning

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.databinding.ActivityBinningBinding

class BinningActivity : AppCompatActivity() {
	
	private lateinit var viewModel: BinningViewModel
	private lateinit var binding: ActivityBinningBinding


//	private var successLauncher = registerForActivityResult<Intent, ActivityResult>(
//		ActivityResultContracts.StartActivityForResult()
//	) { result: ActivityResult? ->
//		startActivity(Intent(this, DashboardActivity::class.java))
//		finish()
//
//	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_binning)
		
		intent?.let {
			if (it.hasExtra(KEY_FILE_NAME)) {
				val fileName = it.getStringExtra(KEY_FILE_NAME) ?: ""
			}
		}
		viewModel = BinningViewModel(application)
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		supportActionBar?.let {
			if (supportActionBar == null) {
				setSupportActionBar(binding.toolbar)
				it.setDisplayHomeAsUpEnabled(true)
				it.setDisplayShowHomeEnabled(true)
				it.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back))
			}
		}
		
		
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				//TODO Siddhesh save details in db
				finish()
				
			}
		}
		return super.onOptionsItemSelected(item)
	}
	
	
	companion object {
		const val KEY_FILE_NAME = "file_name"
	}
}