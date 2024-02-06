package com.banswara.warehouse.dispatch

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.dashboard.DashboardActivity
import com.banswara.warehouse.database.WareHouseDB
import com.banswara.warehouse.databinding.ActivityDispatchBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.success.SuccessActivity
import com.banswara.warehouse.utils.StatusRetention
import com.banswara.warehouse.utils.Utils
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Arrays
import java.util.Random

class DispatchListActivity : AppCompatActivity(), RowChallanViewModel.ChallanClick {
	
	private lateinit var viewModel: DispatchViewModel
	private lateinit var binding: ActivityDispatchBinding
	private var capture: CaptureManager? = null
	
	
	private var successLauncher = registerForActivityResult<Intent, ActivityResult>(
		ActivityResultContracts.StartActivityForResult()
	) { result: ActivityResult? ->
		startActivity(Intent(this, DashboardActivity::class.java))
		finish()
		
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_dispatch)
		
		intent?.let {
			if (it.hasExtra(KEY_FILE_NAME)) {
				val fileName = it.getStringExtra(KEY_FILE_NAME) ?: ""
				viewModel = DispatchViewModel(fileName, application)
			}
		}
		
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
		
		
		binding.zxingBarcodeScanner.decodeContinuous { barcode ->
			
			var allChallanScan = true
			barcode.text?.let { code ->
				Toast.makeText(this, barcode.text, Toast.LENGTH_SHORT).show()
				capture?.onPause()
				
				try {
					Handler(mainLooper).post {
						val list: ArrayList<BaseRowModel> =
							viewModel.challanListLiveData.value!!
						viewModel.challanListLiveData.value?.forEach {
							if (it is RowChallanViewModel) {
								if (it.challanNo.get() == code && it.status.get() != StatusRetention.SCANNED) {
									list.remove(it)
									it.status.set(StatusRetention.SCANNED)
									list.add(it)
									it.challanFileModel.fileStatus = StatusRetention.STATUS_SCANNED
									WareHouseDB.getDataBase(this@DispatchListActivity)
										?.wareHouseDao()?.updateChallanStatus(it.challanFileModel)
									
								}
								
							}
						}
						
						viewModel.challanListLiveData.value = list
						viewModel.challanListLiveData.value?.forEach {
							if (it is RowChallanViewModel) {
								if (it.status.get() != StatusRetention.SCANNED) {
									allChallanScan = false
								}
							}
						}
						viewModel.allScanned.value = allChallanScan
						
						
					}
					
				}catch (e: Exception){
					e.printStackTrace()
				}
				
				finally {
					capture?.onResume()
				}
			}
		}
		
		viewModel.events.observe(this) {
			when (it) {
				DispatchViewModel.EVENTS.FETCH_FILE_CONTENT -> {
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.isApiCalling.postValue(true)
						RetrofitRepository.instance.fetchContent(
							viewModel.user!!.userId,
							Utils.getDeviceId(contentResolver),
							viewModel.fileName.value!!
						)
					}
				}
				
				DispatchViewModel.EVENTS.PROCESS_FILE -> {
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.isApiCalling.postValue(true)
						RetrofitRepository.instance.uploadScannedFile(
							viewModel.user!!.userId,
							Utils.getDeviceId(contentResolver),
							viewModel.fileName.value!!
						)
					}
				}
				
				DispatchViewModel.EVENTS.MOVE_TO_SUCCESS -> {
					val intent = Intent(this, SuccessActivity::class.java)
					intent.putExtra(SuccessActivity.KEY_FROM_LOGIN, false)
					successLauncher.launch(intent)
				}
			}
		}
		
		RetrofitRepository.instance.apiLiveData.observe(this) { it ->
			when (it) {
				
				is RetrofitRepository.RequestType.FETCH_FILE_CONTENT -> {
					viewModel.isApiCalling.value = false
					val list = arrayListOf<BaseRowModel>()
					it.fetchContentResponseModel.forEach { content ->
						content.fileName = viewModel.fileName.value!!
						content.fileStatus = StatusRetention.STATUS_PENDING
						list.add(RowChallanViewModel(content, this))
						CoroutineScope(Dispatchers.IO).launch {
							WareHouseDB.getDataBase(this@DispatchListActivity)?.wareHouseDao()?.insertChallan(content)
						}
					}
					viewModel.challanListLiveData.value = (list)
					
				}
				
				is RetrofitRepository.RequestType.PROCESS_FILE -> {
					viewModel.isApiCalling.value = false
					viewModel.challanListLiveData.value = arrayListOf()
					viewModel.events.value = DispatchViewModel.EVENTS.MOVE_TO_SUCCESS
					
				}
				
				else -> {}
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
	companion object {
		const val KEY_FILE_NAME = "file_name"
	}
	
	override fun onChallanClick(challanRow: RowChallanViewModel) {
		
//		viewModel.events.value = DispatchViewModel.EVENTS.SCAN(challanRow)
	}
}