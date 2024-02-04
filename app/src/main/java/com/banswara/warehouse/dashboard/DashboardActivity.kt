package com.banswara.warehouse.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.database.WareHouseDB
import com.banswara.warehouse.databinding.ActivityDashboardBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.ChallanFileModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.product_list.ProductListActivity
import com.banswara.warehouse.utils.StatusRetention
import com.banswara.warehouse.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DashboardActivity : AppCompatActivity(), RowFilesViewModel.FileClick {
	
	private lateinit var viewModel: DashboardViewModel
	private lateinit var binding: ActivityDashboardBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
		viewModel = DashboardViewModel(application)
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		viewModel.events.observe(this) {
			when (it) {
				
				DashboardViewModel.DASHBOARD_EVENTS.FETCH_FILES -> {
					viewModel.user?.let { user ->
						CoroutineScope(Dispatchers.IO).launch {
							viewModel.isApiCalling.postValue(true)
							RetrofitRepository.instance.fetchFiles(
								user.userId,
								Utils.getDeviceId(contentResolver)
							)
						}
					}
				}
				
				else -> {}
			}
		}
		
		RetrofitRepository.instance.apiLiveData.observe(this) { it ->
			when (it) {
				is RetrofitRepository.RequestType.FETCH_FILES -> {
					viewModel.isApiCalling.value = false
					val list = arrayListOf<BaseRowModel>()
					
					
					it.fetchFilesResponseModel.forEach { file ->
						list.add(RowFilesViewModel(file, this))
						CoroutineScope(Dispatchers.Default).launch {
							Log.d("Siddhesh", "File Inserted : "+WareHouseDB.getDataBase(this@DashboardActivity)?.wareHouseDao()?.insertFile(file))
						}
					}
					viewModel.fileListLiveData.value = (list)
				}
				
				else -> {}
			}
		}
		
	}
	
	override fun onFileSelect(challanFileModel: ChallanFileModel) {
		challanFileModel.fileStatus = "In Progress"
		CoroutineScope(Dispatchers.Default).launch {
			Log.d("Siddhesh", "File Updated : "+WareHouseDB.getDataBase(this@DashboardActivity)?.wareHouseDao()?.updateFileStatus(challanFileModel))
		}
		val intent = Intent(this, ProductListActivity::class.java)
		intent.putExtra(ProductListActivity.KEY_FILE_NAME, challanFileModel.fileName)
		startActivity(intent)
	}
	
	override fun onResume() {
		super.onResume()
		viewModel.events.value = DashboardViewModel.DASHBOARD_EVENTS.FETCH_FILES
		
	}
	
	
}