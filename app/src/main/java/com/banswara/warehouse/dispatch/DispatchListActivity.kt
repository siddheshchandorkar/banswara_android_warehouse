package com.banswara.warehouse.dispatch

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.banswara.warehouse.model.BinningChallanModel
import com.banswara.warehouse.model.FileContentModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.success.SuccessActivity
import com.banswara.warehouse.utils.PENDING
import com.banswara.warehouse.utils.StatusRetention
import com.banswara.warehouse.utils.StatusRetention.STATUS_SCANNED
import com.banswara.warehouse.utils.Utils
import com.journeyapps.barcodescanner.CaptureManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
			var isProgress = false
			if (it.hasExtra(KEY_FILE_IN_PROGRESS)) {
				isProgress = it.getBooleanExtra(KEY_FILE_IN_PROGRESS, false)
			}
			if (it.hasExtra(KEY_FILE_NAME)) {
				val fileName = it.getStringExtra(KEY_FILE_NAME) ?: ""
				viewModel = DispatchViewModel(fileName, isProgress, application)
				if (it.hasExtra(KEY_IS_EDITABLE)) {
					val edit = it.getBooleanExtra(KEY_IS_EDITABLE, true)
					viewModel.editable.value = edit
					
				}
				if (it.hasExtra(KEY_IS_CLOSE)) {
					val edit = it.getBooleanExtra(KEY_IS_CLOSE, false)
					viewModel.close.value = edit
				}
			}
			
			
		}
		
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		setSupportActionBar(binding.toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back))
		supportActionBar?.title = viewModel.fileName.value
		
		
		capture = CaptureManager(this, binding.zxingBarcodeScanner)
		capture?.initializeFromIntent(intent, savedInstanceState)
		capture?.setShowMissingCameraPermissionDialog(false)
		capture?.decode()
		changeLaserVisibility()
		RetrofitRepository.instance.apiLiveData.value = RetrofitRepository.RequestType.DEFAULT
		
		
		binding.zxingBarcodeScanner.decodeContinuous { barcode ->
			var allChallanScan = true
			barcode.text?.let { code ->
				Toast.makeText(this, barcode.text, Toast.LENGTH_SHORT).show()
				capture?.onPause()
				
				try {
					val list: ArrayList<BaseRowModel> = ArrayList()
					viewModel.challanListLiveData.value?.forEach {
						list.add(it)
					}
					
					list.forEach {
						if (it is RowChallanViewModel) {
							if (it.challanNo.get() == code && it.status.get() != StatusRetention.SCANNED) {
								it.status.set(StatusRetention.SCANNED)
								it.statusValue = StatusRetention.SCANNED
								it.challanFileModel.fileStatus = STATUS_SCANNED
								
								
								//status change to update in db
								CoroutineScope(Dispatchers.IO).launch {
									WareHouseDB.getDataBase(this@DispatchListActivity)
										?.wareHouseDao()?.updateChallanStatus(it.challanFileModel)
									
								}
								
								
							}
							if (it.status.get() != StatusRetention.SCANNED) {
								allChallanScan = false
							}
						}
					}
					
					Log.d("Siddhesh", "Checking status after scan " + allChallanScan)
					
					list.sortBy { (it as RowChallanViewModel).statusValue }
					
					viewModel.challanListLiveData.value = list
					viewModel.allScanned.value = allChallanScan
					
					capture?.onResume()
					
				} catch (e: Throwable) {
					capture?.onResume()
				}
				
			}
		}
		
		viewModel.challanListLiveData.observe(this) {
			CoroutineScope(Dispatchers.Main).launch {
				checkAllChallanScanned()
			}
		}
		
		viewModel.events.observe(this) {
			when (it) {
				DispatchViewModel.EVENTS.FETCH_FILE_CONTENT -> {
					if (Utils.isInternetConnected(application)) {
						CoroutineScope(Dispatchers.IO).launch {
							viewModel.isApiCalling.postValue(true)
							if(viewModel.close.value ==true){
								RetrofitRepository.instance.fetchDispatchFileDetails(
									Utils.getDeviceId(contentResolver),
									viewModel.fileName.value!!
								)
							}else{
								RetrofitRepository.instance.fetchContent(
									viewModel.user!!.userId,
									Utils.getDeviceId(contentResolver),
									viewModel.fileName.value!!
								)
							}
							
						}
					}
					
					
				}
				
				DispatchViewModel.EVENTS.PROCESS_FILE -> {
					if (checkAllChallanScanned()) {
						if (Utils.isInternetConnected(application)) {
							val challanList = ArrayList<BinningChallanModel>()
							viewModel.challanListLiveData.value?.forEach {
								if (it is RowChallanViewModel) {
									challanList.add(BinningChallanModel(it.challanFileModel.fileContent))
								}
							}
							CoroutineScope(Dispatchers.IO).launch {
								viewModel.isApiCalling.postValue(true)
								RetrofitRepository.instance.dispatchScannedFile(
									viewModel.user!!.userId,
									Utils.getDeviceId(contentResolver),
									viewModel.fileName.value!!, challanList
								)
							}
						}
					} else {
						Toast.makeText(this, "Please Scan all challan", Toast.LENGTH_SHORT).show()
					}
					
				}
				
				DispatchViewModel.EVENTS.SAVE_FILE -> {
					if (Utils.isInternetConnected(application)) {
						val challanList = ArrayList<BinningChallanModel>()
						viewModel.challanListLiveData.value?.forEach {
							if (it is RowChallanViewModel) {
								if (it.challanFileModel.fileStatus == STATUS_SCANNED) {
									challanList.add(BinningChallanModel(it.challanFileModel.fileContent))
								}
							}
						}
						if (challanList.isNotEmpty()) {
							CoroutineScope(Dispatchers.IO).launch {
								viewModel.isApiCalling.postValue(true)
								RetrofitRepository.instance.saveScannedFile(
									viewModel.user!!.userId,
									Utils.getDeviceId(contentResolver),
									viewModel.fileName.value!!, challanList
								)
							}
						} else {
							Toast.makeText(
								this,
								"Please scan atleast one challan",
								Toast.LENGTH_SHORT
							).show()
						}
						
					}
				}
				
				DispatchViewModel.EVENTS.MOVE_TO_SUCCESS -> {
					val intent = Intent(this, SuccessActivity::class.java)
					intent.putExtra(SuccessActivity.KEY_FROM_LOGIN, false)
					intent.putExtra(SuccessActivity.KEY_FILE_NAME, viewModel.fileName.value!!)
					successLauncher.launch(intent)
				}
			}
		}
		
		RetrofitRepository.instance.apiLiveData.observe(this) { it ->
			viewModel.isApiCalling.postValue(false)
			when (it) {
				
				is RetrofitRepository.RequestType.FETCH_FILE_CONTENT -> {
					viewModel.isApiCalling.value = false
					val list = arrayListOf<BaseRowModel>()
					
					it.fetchContentResponseModel.forEach { content ->
						if(content.errorMsg.equals("Directory is missing.")){
							Toast.makeText(this, content.errorMsg+" for "+viewModel.fileName.value, Toast.LENGTH_SHORT).show()
						}else{
							content.fileName = viewModel.fileName.value!!
							val row = RowChallanViewModel(content, this)
							
							if(viewModel.close.value ==true){
								row.status.set(StatusRetention.SCANNED)
								row.statusValue = StatusRetention.SCANNED
								row.challanFileModel.fileStatus = STATUS_SCANNED
								
							}else{
								row.status.set(StatusRetention.PENDING)
								row.statusValue = StatusRetention.PENDING
								row.challanFileModel.fileStatus = StatusRetention.STATUS_PENDING
							}
							
							list.add(row)
							CoroutineScope(Dispatchers.IO).launch {
								WareHouseDB.getDataBase(this@DispatchListActivity)?.wareHouseDao()
									?.insertChallan(content)
							}
						}
						
					}
					viewModel.challanListLiveData.value = (list)
					if(viewModel.close.value ==false){
						if (viewModel.isPartialFile.value == true) {
							viewModel.isApiCalling.postValue(true)
							CoroutineScope(Dispatchers.IO).launch {
								RetrofitRepository.instance.fetchPartialChallan(
									viewModel.user!!.userId,
									Utils.getDeviceId(contentResolver),
									viewModel.fileName.value!!
								)
							}
						} else {
							CoroutineScope(Dispatchers.IO).launch {
								checkDBDetails()
							}
						}
					}
					
				}
				
				is RetrofitRepository.RequestType.FETCH_PARTIAL_FILE -> {
					viewModel.isApiCalling.value = false
					CoroutineScope(Dispatchers.Main).launch {
						val list = arrayListOf<BaseRowModel>()
						viewModel.challanListLiveData.value?.forEach {
							list.add(it)
						}
						it.partialFileResponseModel.challanList?.forEach { content ->
							viewModel.challanListLiveData.value?.indexOfFirst {
								(it as RowChallanViewModel).challanFileModel.fileContent == content.challan
							}?.let { index ->
								if(index!=-1){
									val file = FileContentModel(
										content.challan,
										viewModel.fileName.value!!,
										STATUS_SCANNED
									)
									CoroutineScope(Dispatchers.IO).launch {
										WareHouseDB.getDataBase(this@DispatchListActivity)
											?.wareHouseDao()
											?.insertChallan(file)
										
									}
									val row = RowChallanViewModel(file, this@DispatchListActivity)
									row.status.set(StatusRetention.SCANNED)
									row.statusValue = StatusRetention.SCANNED
									row.challanFileModel.fileStatus = STATUS_SCANNED
									
									list.set(
										index,
										row
									)
									Log.d(
										"Siddhesh",
										"Check Scanned index ${content.challan}: " + (list.get(index) as RowChallanViewModel).challanFileModel
									)
								}
								
							}
							
						}
						list.sortBy { (it as RowChallanViewModel).statusValue }
						viewModel.challanListLiveData.value = (list)
						checkDBDetails()
					}
				}
				
				is RetrofitRepository.RequestType.SAVE_PARTIAL_FILE -> {
					Toast.makeText(
						this,
						it.savePartialFile.errorMsg,
						Toast.LENGTH_SHORT
					).show()
					if (it.savePartialFile.errorMsg.equals("File save successfully.")) {
						finish()
					}
					
				}
				
				is RetrofitRepository.RequestType.DISPATCH_FILE -> {
					if (it.processFileResponseModel.errorMsg.equals("File process successfully.")) {
						viewModel.isApiCalling.value = false
						viewModel.challanListLiveData.value = arrayListOf()
						viewModel.events.value = DispatchViewModel.EVENTS.MOVE_TO_SUCCESS
					} else {
						Toast.makeText(
							this,
							it.processFileResponseModel.errorMsg,
							Toast.LENGTH_SHORT
						).show()
					}
					
					
				}
				
				is RetrofitRepository.RequestType.DEFAULT -> {
				
				}
				
				else -> {}
			}
		}
	}
	
	private fun checkAllChallanScanned(): Boolean {
		var allChallanScan = true
		var totalCount = 0
		var totalScanned = 0
		try {
			viewModel.challanListLiveData.value?.let {
				totalCount = it.size
				val list: ArrayList<BaseRowModel> = ArrayList()
				it.forEach {
					list.add(it)
				}
				
				list.forEach {
					if (it is RowChallanViewModel) {
						if (it.status.get() == StatusRetention.SCANNED) {
							totalScanned++
						} else {
							allChallanScan = false
						}
					}
				}
				
				viewModel.allScanned.postValue(allChallanScan)
				viewModel.totalCount.postValue(totalCount)
				viewModel.totalScanned.postValue(totalScanned)
				viewModel.detailsText.postValue(
					getString(
						R.string.list_of_challans_scanned_total,
						totalScanned.toString(),
						totalCount.toString()
					)
				)
			}
		} catch (e: Throwable) {
			e.printStackTrace()
			viewModel.allScanned.postValue(false)
			
		}
		return allChallanScan
	}
	
	fun checkDBDetails() {
		val dbList = WareHouseDB.getDataBase(this@DispatchListActivity)
			?.wareHouseDao()
			?.getDispatchChallanByFile(viewModel.fileName.value ?: "", STATUS_SCANNED)
		val list = arrayListOf<BaseRowModel>()
		viewModel.challanListLiveData.value?.forEach {
			list.add(it)
		}
		dbList?.forEach { content ->
			Log.d("Siddhesh", "Checking DB ${content.fileContent}: " + content.fileStatus)
			list?.indexOfFirst {
				(it as RowChallanViewModel).challanFileModel.fileContent == content.fileContent
			}?.let { index ->
				if (index != -1) {
					
					val file = FileContentModel(
						content.fileContent,
						viewModel.fileName.value!!,
						STATUS_SCANNED
					)
					
					val row = RowChallanViewModel(file, this@DispatchListActivity)
					row.status.set(StatusRetention.SCANNED)
					row.statusValue = StatusRetention.SCANNED
					row.challanFileModel.fileStatus = STATUS_SCANNED
					
					list.set(index, row)
					
					CoroutineScope(Dispatchers.IO).launch {
						WareHouseDB.getDataBase(this@DispatchListActivity)
							?.wareHouseDao()
							?.insertChallan(file)
						
					}
				}
				
			}
		}
		list.sortBy { (it as RowChallanViewModel).statusValue }
		viewModel.challanListLiveData.postValue(list)
		
	}
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
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
		const val KEY_FILE_IN_PROGRESS = "in_progress"
		const val KEY_IS_EDITABLE = "is_editable"
		const val KEY_IS_CLOSE = "is_close"
	}
	
	override fun onChallanClick(challanRow: RowChallanViewModel) {

//		viewModel.events.value = DispatchViewModel.EVENTS.SCAN(challanRow)
	}
}