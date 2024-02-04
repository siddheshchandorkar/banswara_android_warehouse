package com.banswara.warehouse.product_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.banswara.warehouse.databinding.ActivityProductListBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.success.SuccessActivity
import com.banswara.warehouse.utils.StatusRetention
import com.banswara.warehouse.utils.Utils
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListActivity : AppCompatActivity(), RowChallanViewModel.ChallanClick {
	
	private lateinit var viewModel: ProductListViewModel
	private lateinit var binding: ActivityProductListBinding
	
	
	private var successLauncher = registerForActivityResult<Intent, ActivityResult>(
		ActivityResultContracts.StartActivityForResult()
	) { result: ActivityResult? ->
		startActivity(Intent(this, DashboardActivity::class.java))
		finish()
		
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_product_list)
		
		intent?.let {
			if (it.hasExtra(KEY_FILE_NAME)) {
				val fileName = it.getStringExtra(KEY_FILE_NAME) ?: ""
				viewModel = ProductListViewModel(fileName, application)
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
		
		val options = GmsBarcodeScannerOptions.Builder()
			.setBarcodeFormats(
				Barcode.FORMAT_QR_CODE,
				Barcode.FORMAT_AZTEC
			)
			.build()
		val scanner = GmsBarcodeScanning.getClient(this, options)
		
		viewModel.events.observe(this) {
			when (it) {
				ProductListViewModel.EVENTS.FETCH_FILE_CONTENT -> {
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.isApiCalling.postValue(true)
						RetrofitRepository.instance.fetchContent(
							viewModel.user!!.userId,
							Utils.getDeviceId(contentResolver),
							viewModel.fileName.value!!
						)
					}
				}
				
				
				is ProductListViewModel.EVENTS.SCAN -> {
					
					scanner.startScan()
						.addOnSuccessListener { barcode ->
							Log.d("ProductListActivity", "Scan Success")
							var allChallanScan = true
							// Task completed successfully
							barcode.rawValue?.let { code ->
								if (code == it.challanRow.challanNo.get()) {
									
									Toast.makeText(this, code + " is Scanned", Toast.LENGTH_LONG)
										.show()
									val list: ArrayList<BaseRowModel> =
										viewModel.challanListLiveData.value!!
									list.remove(it.challanRow as BaseRowModel)
									it.challanRow.status.set(StatusRetention.SCANNED)
									list.add(it.challanRow)
									viewModel.challanListLiveData.value = list
									CoroutineScope(Dispatchers.Default).launch {
										it.challanRow.challanFileModel.fileStatus = StatusRetention.STATUS_SCANNED
										 WareHouseDB.getDataBase(this@ProductListActivity)?.wareHouseDao()?.updateChallanStatus(it.challanRow.challanFileModel)
									}
								}
								
								viewModel.challanListLiveData.value?.forEach {
									if ((it as RowChallanViewModel).status.get() != StatusRetention.SCANNED) {
										allChallanScan = false
									}
								}
								viewModel.allScanned.value = allChallanScan
								
							}
						}
						.addOnCanceledListener {
							// Task canceled
							Log.d("ProductListActivity", "Scan Cancel")
						}
						.addOnFailureListener { e ->
							// Task failed with an exception
							Log.d("ProductListActivity", "Scan Failed : " + e.message)
							
						}
					
				}
				
				ProductListViewModel.EVENTS.PROCESS_FILE -> {
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.isApiCalling.postValue(true)
						RetrofitRepository.instance.uploadScannedFile(
							viewModel.user!!.userId,
							Utils.getDeviceId(contentResolver),
							viewModel.fileName.value!!
						)
					}
				}
				
				ProductListViewModel.EVENTS.MOVE_TO_SUCCESS -> {
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
						CoroutineScope(Dispatchers.Default).launch {
							WareHouseDB.getDataBase(this@ProductListActivity)?.wareHouseDao()?.insertChallan(content)
						}
					}
					viewModel.challanListLiveData.value = (list)
					
				}
				
				is RetrofitRepository.RequestType.PROCESS_FILE -> {
					viewModel.isApiCalling.value = false
					viewModel.challanListLiveData.value = arrayListOf()
					viewModel.events.value = ProductListViewModel.EVENTS.MOVE_TO_SUCCESS
					
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
	
	
	companion object {
		const val KEY_FILE_NAME = "file_name"
	}
	
	override fun onChallanClick(challanRow: RowChallanViewModel) {
		
		viewModel.events.value = ProductListViewModel.EVENTS.SCAN(challanRow)
	}
}