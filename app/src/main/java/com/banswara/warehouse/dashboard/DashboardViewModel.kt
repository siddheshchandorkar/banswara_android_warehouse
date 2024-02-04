package com.banswara.warehouse.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.utils.PreferenceManager

class DashboardViewModel(app: Application) : AndroidViewModel(app) {
	
	var fileListLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	val events = MutableLiveData<DASHBOARD_EVENTS>()
	val isApiCalling: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val user: LoginResponseModel? = PreferenceManager.getUser()
	
	init {
		fileListLiveData.value = (arrayListOf<BaseRowModel>())
	}
	
	sealed class DASHBOARD_EVENTS {
		data object FETCH_FILES : DASHBOARD_EVENTS()
		data object MOVE_TO_CHALLAN_LIST : DASHBOARD_EVENTS()
	}
}