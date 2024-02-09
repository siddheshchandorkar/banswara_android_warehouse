package com.banswara.warehouse.dashboard

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.binning.BinningActivity
import com.banswara.warehouse.database.WareHouseDB
import com.banswara.warehouse.databinding.ActivityDashboardBinding
import com.banswara.warehouse.dispatch.DispatchListActivity
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.ChallanFileModel
import com.banswara.warehouse.network.RetrofitRepository
import com.banswara.warehouse.utils.PreferenceManager
import com.banswara.warehouse.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DashboardActivity : AppCompatActivity(), RowFilesViewModel.FileClick,
	RowBinningFilesViewModel.FileClick {
	
	private lateinit var viewModel: DashboardViewModel
	private lateinit var binding: ActivityDashboardBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
		viewModel = DashboardViewModel(application)
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		setSupportActionBar(binding.toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back))
		if (!TextUtils.isEmpty(PreferenceManager.getUser()?.userName)) {
			supportActionBar?.title = "Hello, " + PreferenceManager.getUser()?.userName
		} else {
			supportActionBar?.title = getString(R.string.banswara_warehouse)
		}
		
		viewModel.title.observe(this){
			if(!TextUtils.isEmpty(it)){
				supportActionBar?.title = it
			} else if (!TextUtils.isEmpty(PreferenceManager.getUser()?.userName)) {
				supportActionBar?.title = "Hello, " + PreferenceManager.getUser()?.userName
			} else {
				supportActionBar?.title = getString(R.string.banswara_warehouse)
			}
		}
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
				
				DashboardViewModel.DASHBOARD_EVENTS.FETCH_BINNING_FILES -> {
					//To fetch local binning files
					CoroutineScope(Dispatchers.IO).launch {
						val list = WareHouseDB.getDataBase(this@DashboardActivity)?.wareHouseDao()
							?.getBinningFileList()
						val tempList = arrayListOf<BaseRowModel>()
						
						list?.let {
							it.forEach {
								tempList.add(RowBinningFilesViewModel(it, this@DashboardActivity))
							}
						}
						viewModel.binningFileListLiveData.postValue(tempList)
						
					}
				}
				
				is DashboardViewModel.DASHBOARD_EVENTS.MOVE_TO_BINNING -> {
					val binningIntent = Intent(this, BinningActivity::class.java)
					if (!TextUtils.isEmpty(it.fileName)) {
						binningIntent.putExtra(BinningActivity.KEY_FILE_NAME, it.fileName)
					}
					startActivity(binningIntent)
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
						CoroutineScope(Dispatchers.IO).launch {
							Log.d("Siddhesh",
								"File Inserted : " + WareHouseDB.getDataBase(this@DashboardActivity)
									?.wareHouseDao()?.insertFile(file)
							)
						}
					}
					viewModel.fileListLiveData.value = (list)
				}
				
				else -> {}
			}
		}
		onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				if (viewModel.isDispatch.value!!) {
					viewModel.isDispatch.value = false
					viewModel.title.value = "Hi, " + PreferenceManager.getUser()?.userName
					
				} else if (viewModel.isBinning.value!!) {
					viewModel.isBinning.value = false
					viewModel.title.value = "Hi, " + PreferenceManager.getUser()?.userName
					
				} else
					finish()
			}
		})
	}
	
	
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				if (viewModel.isDispatch.value!!) {
					viewModel.isDispatch.value = false
					viewModel.title.value = "Hi, " + PreferenceManager.getUser()?.userName
					
				} else if (viewModel.isBinning.value!!) {
					viewModel.isBinning.value = false
					viewModel.title.value = "Hi, " + PreferenceManager.getUser()?.userName
					
				} else
					finish()
			}
		}
		return super.onOptionsItemSelected(item)
	}
	
	
	override fun onFileSelect(challanFileModel: ChallanFileModel) {
		challanFileModel.fileStatus = "In Progress"
		CoroutineScope(Dispatchers.IO).launch {
			WareHouseDB.getDataBase(this@DashboardActivity)?.wareHouseDao()
				?.updateFileStatus(challanFileModel)
		}
		val intent = Intent(this, DispatchListActivity::class.java)
		intent.putExtra(DispatchListActivity.KEY_FILE_NAME, challanFileModel.fileName)
		startActivity(intent)
	}
	
	override fun onResume() {
		super.onResume()
		viewModel.events.value = DashboardViewModel.DASHBOARD_EVENTS.FETCH_FILES
		viewModel.events.value = DashboardViewModel.DASHBOARD_EVENTS.FETCH_BINNING_FILES
		
	}
	
	override fun onFileSelect(fileName: String) {
		viewModel.events.value = DashboardViewModel.DASHBOARD_EVENTS.MOVE_TO_BINNING(fileName)
	}
	
	
}