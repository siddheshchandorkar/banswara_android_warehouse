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
import com.banswara.warehouse.dashboard.RowBinningFilesViewModel
import com.banswara.warehouse.database.WareHouseDB
import com.banswara.warehouse.databinding.ActivityBinningBinding
import com.banswara.warehouse.dispatch.RowChallanViewModel
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.BinningChallanModel
import com.banswara.warehouse.model.BinningModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.utils.StatusRetention
import com.banswara.warehouse.utils.Utils
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
		viewModel = BinningViewModel(application)
		
		intent?.let {
			if (it.hasExtra(KEY_FILE_NAME)) {
				val fileName = it.getStringExtra(KEY_FILE_NAME) ?: ""
				if (!TextUtils.isEmpty(fileName)) {
					viewModel.fileName.value = fileName
					val values = fileName.split("-")
					if (values.size == 3) {
						viewModel.first.value = values[0]
						viewModel.second.value = values[1]
						viewModel.third.value = values[2]
					}
					
					//fetch db data for this file
					CoroutineScope(Dispatchers.IO).launch {
						val list = WareHouseDB.getDataBase(this@BinningActivity)?.wareHouseDao()
							?.getBinningChallanByFile(fileName)
						val tempList = arrayListOf<BaseRowModel>()
						
						list?.let {
							it.forEach {
								tempList.add(RowBinningViewModel(BinningModel(it, fileName)))
							}
						}
						viewModel.challanListLiveData.postValue(tempList)
					}
				}
				
			}
		}
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		setSupportActionBar(binding.toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back))
		if (!TextUtils.isEmpty(viewModel.fileName.value)) {
			supportActionBar?.title = viewModel.fileName.value
		} else {
			supportActionBar?.title = "Binning Process"
		}
		
		
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
			capture?.onPause()
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
			
			capture?.onResume()
			
		}
		
		viewModel.events.observe(this) {
			when (it) {
				
				BinningViewModel.EVENTS.UPLOAD_FILE -> {
					viewModel.user?.let { user ->
						CoroutineScope(Dispatchers.IO).launch {
							
							val challans = ArrayList<BinningChallanModel>()
							viewModel.challanListLiveData.value?.forEach {
								if (it is RowBinningViewModel) {
									challans.add(BinningChallanModel(it.challanNo.get() ?: ""))
								}
							}
							
							viewModel.isApiCalling.postValue(true)
							RetrofitRepository.instance.uploadBinningFile(
								useId = user.userId.toString(),
								deviceId = Utils.getDeviceId(contentResolver),
								location = viewModel.first.value!! + "-" + viewModel.second.value!! + "-" + viewModel.third.value!!,
								date = "",
								challans
							)
						}
					}
				}
				
				is BinningViewModel.EVENTS.SHOW_TOAST -> {
					Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
				}
				
				
				else -> {}
			}
		}
		
		RetrofitRepository.instance.apiLiveData.observe(this) { it ->
			when (it) {
				
				is RetrofitRepository.RequestType.UPLOAD_BINNING_FILE -> {
					
					if(it.uploadResponse.errorMsg.equals("Challan added successfully.", true)){
						Toast.makeText(this, it.uploadResponse.errorMsg, Toast.LENGTH_SHORT).show()
						//delete binning details for this file
						CoroutineScope(Dispatchers.IO).launch {
							WareHouseDB.getDataBase(this@BinningActivity)?.wareHouseDao()
								?.deleteBinningChallanByFile(viewModel.first.value!! + "-" + viewModel.second.value!! + "-" + viewModel.third.value!!)
						}
						finish()
					}else{
						Toast.makeText(this, it.uploadResponse.errorMsg, Toast.LENGTH_SHORT).show()
					}
					
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