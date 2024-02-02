package com.banswara.warehouse.product_list

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.databinding.ActivityProductListBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.FileContentModel
import com.banswara.warehouse.network.RetrofitRepository
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
		
		val options = GmsBarcodeScannerOptions.Builder()
			.setBarcodeFormats(
				Barcode.FORMAT_QR_CODE,
				Barcode.FORMAT_AZTEC)
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
								if (code == it.challanNo) {
									Toast.makeText(this, code + " is present", Toast.LENGTH_LONG)
										.show()
									
								}
//								viewModel.challanListLiveData.value!!.forEach { row ->
//									if(row is RowChallanViewModel){
//										if(code == row.challanNo.get()){
//											Toast.makeText(this, code+" is present", Toast.LENGTH_LONG).show()
//											row.isSelected.set(true)
//										}
//										if (row.isSelected.get() ==false){
//											allChallanScan =false
//										}
//									}
//
//								}
							}
//							viewModel.allScanned.value = allChallanScan
							
						}
						.addOnCanceledListener {
							// Task canceled
							Log.d("ProductListActivity", "Scan Cancel")
						}
						.addOnFailureListener { e ->
							// Task failed with an exception
							Log.d("ProductListActivity", "Scan Failed : "+e.message)
							
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
				
				}
			}
		}
		
		RetrofitRepository.instance.apiLiveData.observe(this) { it ->
			when (it) {
				
				is RetrofitRepository.RequestType.FETCH_FILE_CONTENT -> {
					viewModel.isApiCalling.value = false
					val list = arrayListOf<BaseRowModel>()
					it.fetchContentResponseModel.forEach { content ->
						list.add(RowChallanViewModel(content, this))
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
	
	companion object {
		const val KEY_FILE_NAME = "file_name"
	}
	
	override fun onChallanClick(challanFileModel: FileContentModel) {
		viewModel.events.value = ProductListViewModel.EVENTS.SCAN(challanFileModel.fileContent)
	}
}