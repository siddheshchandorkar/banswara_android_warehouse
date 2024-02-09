package com.banswara.warehouse.dashboard

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.utils.PreferenceManager

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
	
	var fileListLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	var binningFileListLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	val events = MutableLiveData<DASHBOARD_EVENTS>()
	val isApiCalling: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val title: MutableLiveData<String> = MutableLiveData<String>("")
	val isDispatch: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val isBinning: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val user: LoginResponseModel? = PreferenceManager.getUser()
	
	init {
		fileListLiveData.value = (arrayListOf<BaseRowModel>())
		binningFileListLiveData.value = (arrayListOf<BaseRowModel>())
	}
	
	fun binningClick(view: View) {
		if (binningFileListLiveData.value!!.isNotEmpty()) {
			title.value = "Binning Process"
			isBinning.value = true
		} else
			events.value = DASHBOARD_EVENTS.MOVE_TO_BINNING("")
	}
	
	fun addFile(view: View) {
		events.value = DASHBOARD_EVENTS.MOVE_TO_BINNING("")
	}
	
	fun dispatchClick(view: View) {
		title.value = "Dispatch Process"
		isDispatch.value = true
	}
	
	sealed class DASHBOARD_EVENTS {
		data object FETCH_FILES : DASHBOARD_EVENTS()
		data object FETCH_BINNING_FILES : DASHBOARD_EVENTS()
		data object MOVE_TO_CHALLAN_LIST : DASHBOARD_EVENTS()
		data class MOVE_TO_BINNING(val fileName: String) : DASHBOARD_EVENTS()
	}
}