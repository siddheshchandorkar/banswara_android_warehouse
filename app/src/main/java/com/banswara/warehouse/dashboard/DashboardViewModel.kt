package com.banswara.warehouse.dashboard

import android.app.Application
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.utils.PreferenceManager

class DashboardViewModel(val app: Application) : AndroidViewModel(app) {
	
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
	var tabsLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	
	init {
		fileListLiveData.value = (arrayListOf<BaseRowModel>())
		binningFileListLiveData.value = (arrayListOf<BaseRowModel>())
		tabsLiveData.value = arrayListOf<BaseRowModel>()
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
		isDispatch.value = true
		/*fileListLiveData.value?.let {
			if (it.isNotEmpty()) {
				title.value = "Dispatch Process"
				isDispatch.value = true
			}else{
				Toast.makeText(app, "Please Complete atleast 1 Binning process", Toast.LENGTH_SHORT).show()
			}
		}?:run {
			Toast.makeText(app, "Please Complete atleast 1 Binning process", Toast.LENGTH_SHORT).show()
		}*/
		
		
	}
	
	sealed class DASHBOARD_EVENTS {
		data object FETCH_FILES : DASHBOARD_EVENTS()
		data object FETCH_BINNING_FILES : DASHBOARD_EVENTS()
		data object MOVE_TO_CHALLAN_LIST : DASHBOARD_EVENTS()
		data class MOVE_TO_BINNING(val fileName: String) : DASHBOARD_EVENTS()
	}
}