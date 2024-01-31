package com.banswara.warehouse.dashboard

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.utils.PreferenceManager

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
	
	var challanListLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	val events = MutableLiveData<DASHBOARD_EVENTS>()
	var fileName: MutableLiveData<String> = MutableLiveData<String>("")
	
	val isApiCalling: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val allScanned: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val user: LoginResponseModel? = PreferenceManager.getUser()
	
	init {
		events.value = DASHBOARD_EVENTS.FETCH_FILES
		challanListLiveData.value = (arrayListOf<BaseRowModel>())
	}
	
	fun fetchFileContent(view: View) {
		events.value = DASHBOARD_EVENTS.FETCH_FILE_CONTENT
	}
	fun processScanFile(view: View) {
		events.value = DASHBOARD_EVENTS.PROCESS_FILE
	}
	
	fun scanCode(view: View) {
		events.value = DASHBOARD_EVENTS.SCAN
	}
	
	sealed class DASHBOARD_EVENTS {
		data object FETCH_FILES : DASHBOARD_EVENTS()
		data object FETCH_FILE_CONTENT : DASHBOARD_EVENTS()
		data object PROCESS_FILE : DASHBOARD_EVENTS()
		data object SCAN : DASHBOARD_EVENTS()
	}
}