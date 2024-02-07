package com.banswara.warehouse.binning

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.database.WareHouseDB
import com.banswara.warehouse.databinding.ActivityBinningBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.BinningModel
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random


class BinningActivity : AppCompatActivity() {
	
	private lateinit var viewModel: BinningViewModel
	private lateinit var binding: ActivityBinningBinding
	private var capture: CaptureManager? = null


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
		
		capture = CaptureManager(this, binding.zxingBarcodeScanner)
		capture?.initializeFromIntent(intent, savedInstanceState)
		capture?.setShowMissingCameraPermissionDialog(false)
		capture?.decode()
		changeLaserVisibility()
		val list = arrayListOf<BaseRowModel>()
		
		viewModel.challanListLiveData.observe(this) {
			viewModel.submitEnable.value = it.isNotEmpty()
		}
		
		binding.zxingBarcodeScanner.decodeContinuous { barcode ->
			if (viewModel.locationValidation()) {
				
				Toast.makeText(this, barcode.text, Toast.LENGTH_LONG).show()
//			beepManager?.playBeepSoundAndVibrate()
				var contains = false
				list.forEach {
					if (it is RowBinningViewModel) {
						if (it.binningModel.fileContent.equals(barcode.text)) {
							contains = true
						}
					}
				}
				if (!contains) {
					val binningModel: BinningModel
					if (!TextUtils.isEmpty(viewModel.fileName.value)) {
						binningModel = BinningModel(
							fileContent = barcode.text,
							fileName = viewModel.fileName.value!!
						)
					} else {
						binningModel = BinningModel(
							fileContent = barcode.text,
							fileName = viewModel.first.value!! + "-" + viewModel.second.value!! + "-" + viewModel.third.value!!
						)
					}
					list.add(RowBinningViewModel(binningModel))
					viewModel.challanListLiveData.value = (list)
					
					CoroutineScope(Dispatchers.IO).launch {
						WareHouseDB.getDataBase(this@BinningActivity)?.wareHouseDao()
							?.insertBinningChallan(binningModel)
					}
					
				}
			}
			
			
		}
		
		viewModel.events.observe(this) {
			when (it) {
				
				BinningViewModel.EVENTS.FETCH_FILE_CONTENT -> {
//					viewModel.user?.let { user ->
//						CoroutineScope(Dispatchers.IO).launch {
//							viewModel.isApiCalling.postValue(true)
//							RetrofitRepository.instance.fetchFiles(
//								user.userId,
//								Utils.getDeviceId(contentResolver)
//							)
//						}
//					}
				}
				
				is BinningViewModel.EVENTS.SHOW_TOAST -> {
					Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
				}
				
				
				else -> {}
			}
		}
		
	}
	
	private fun changeMaskColor() {
		val rnd = Random()
		val color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
		binding.zxingBarcodeScanner.viewFinder.setMaskColor(color)
	}
	
	private fun changeLaserVisibility() {
		binding.zxingBarcodeScanner.viewFinder.setLaserVisibility(true)
	}
	
	override fun onResume() {
		super.onResume()
		capture?.onResume()
	}
	
	override fun onPause() {
		super.onPause()
		capture?.onPause()
	}
	
	override fun onDestroy() {
		super.onDestroy()
		capture?.onDestroy()
	}
	
	
	override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
		return binding.zxingBarcodeScanner.onKeyDown(keyCode, event) || super.onKeyDown(
			keyCode,
			event
		)
	}
	
	
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String?>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		capture?.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
		const val PERMISSION_REQUEST_CODE = 200
		
	}
}