package com.banswara.warehouse.dashboard

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.databinding.ActivityDashboardBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.RowDashboardViewModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.utils.Utils
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
			}
		}
		
		
		RetrofitRepository.instance.apiLiveData.observe(this) { it ->
			when (it) {
				is RetrofitRepository.RequestType.FETCH_FILES -> {
					viewModel.isApiCalling.value = false
					val files = it.fetchFilesResponseModel
					val arrayAdapter = ArrayAdapter(this, R.layout.row_dropdown, files)
					binding.autoCompleteTextView.setAdapter(arrayAdapter)
				}

				is RetrofitRepository.RequestType.FETCH_FILE_CONTENT -> {
					viewModel.isApiCalling.value = false
					val list = arrayListOf<BaseRowModel>()
					it.fetchContentResponseModel.forEach {content ->
						list.add(RowDashboardViewModel(content))
					}
					viewModel.challanListLiveData.value=(list)
					
					Log.d("Siddhesh", "Check List : "+viewModel.challanListLiveData.value)
				}

				else -> TODO()
			}
		}
		
	}
	
	
}