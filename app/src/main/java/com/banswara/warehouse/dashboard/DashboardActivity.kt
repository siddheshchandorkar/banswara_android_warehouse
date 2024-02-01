package com.banswara.warehouse.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.databinding.ActivityDashboardBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.product_list.RowDashboardViewModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.utils.Utils
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DashboardActivity : AppCompatActivity() {
	
	private lateinit var viewModel: DashboardViewModel
	private lateinit var binding: ActivityDashboardBinding
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
		viewModel = DashboardViewModel(application)
		binding.vm = viewModel
		binding.lifecycleOwner = this
		val options = GmsBarcodeScannerOptions.Builder()
			.setBarcodeFormats(
				Barcode.FORMAT_QR_CODE,
				Barcode.FORMAT_AZTEC)
			.build()
		val scanner = GmsBarcodeScanning.getClient(this, options)
		
		viewModel.events.observe(this) {
			when (it) {
				DashboardViewModel.DASHBOARD_EVENTS.FETCH_FILE_CONTENT -> {
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.isApiCalling.postValue(true)
						RetrofitRepository.instance.fetchContent(
							viewModel.user!!.userId,
							Utils.getDeviceId(contentResolver),
							viewModel.fileName.value!!
						)
					}
				}

				DashboardViewModel.DASHBOARD_EVENTS.FETCH_FILES -> {
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.isApiCalling.postValue(true)
						RetrofitRepository.instance.fetchFiles(
							viewModel.user!!.userId,
							Utils.getDeviceId(contentResolver)
						)
					}

				}
				
				DashboardViewModel.DASHBOARD_EVENTS.SCAN -> {
					
						scanner.startScan()
						.addOnSuccessListener { barcode ->
							var allChallanScan = true
							// Task completed successfully
							barcode.rawValue?.let {code ->
								viewModel.challanListLiveData.value!!.forEach { row ->
									if(row is RowDashboardViewModel){
										if(code == row.challanNo.get()){
											Toast.makeText(this, code+" is present", Toast.LENGTH_LONG).show()
											row.isSelected.set(true)
										}
										if (row.isSelected.get() ==false){
											allChallanScan =false
										}
									}
									
								}
							}
							viewModel.allScanned.value = allChallanScan
							
						}
						.addOnCanceledListener {
							// Task canceled
						}
						.addOnFailureListener { e ->
							// Task failed with an exception
						}
					
				}
				
				DashboardViewModel.DASHBOARD_EVENTS.PROCESS_FILE -> {
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.isApiCalling.postValue(true)
						RetrofitRepository.instance.uploadScannedFile(
							viewModel.user!!.userId,
							Utils.getDeviceId(contentResolver),
							viewModel.fileName.value!!
						)
					}
				}
			}
		}
		
		
		RetrofitRepository.instance.apiLiveData.observe(this) { it ->
			when (it) {
				is RetrofitRepository.RequestType.FETCH_FILES -> {
					viewModel.isApiCalling.value = false
					val files = it.fetchFilesResponseModel
				}

				is RetrofitRepository.RequestType.FETCH_FILE_CONTENT -> {
					viewModel.isApiCalling.value = false
					val list = arrayListOf<BaseRowModel>()
					it.fetchContentResponseModel.forEach {content ->
						list.add(RowDashboardViewModel(content))
					}
					viewModel.challanListLiveData.value=(list)
					
				}
				
				is RetrofitRepository.RequestType.PROCESS_FILE -> {
					viewModel.isApiCalling.value = false
					viewModel.challanListLiveData.value= arrayListOf()
					viewModel.events.value = DashboardViewModel.DASHBOARD_EVENTS.FETCH_FILES
					
				}
				
				else -> {}
			}
		}
		
	}
	
	
}