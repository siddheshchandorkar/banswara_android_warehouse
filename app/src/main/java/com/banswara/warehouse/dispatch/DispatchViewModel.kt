package com.banswara.warehouse.dispatch

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.utils.PreferenceManager

class DispatchViewModel(file :String, app: Application): AndroidViewModel(app) {
	
	var challanListLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	val events = MutableLiveData<EVENTS>()
	var fileName: MutableLiveData<String> = MutableLiveData<String>(file)
	
	val isApiCalling: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val allScanned: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val user: LoginResponseModel? = PreferenceManager.getUser()
	
	init {
		challanListLiveData.value = (arrayListOf<BaseRowModel>())
		events.value = EVENTS.FETCH_FILE_CONTENT
	}
	
	fun onShipped(view  : View){
		events.value = EVENTS.PROCESS_FILE
	}
	sealed class EVENTS {
		data object FETCH_FILE_CONTENT : EVENTS()
		data object PROCESS_FILE : EVENTS()
		data object MOVE_TO_SUCCESS : EVENTS()
//		data class SCAN(val challanRow: RowChallanViewModel) : EVENTS()
	}
}