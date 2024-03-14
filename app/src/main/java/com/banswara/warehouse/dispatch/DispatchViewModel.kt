package com.banswara.warehouse.dispatch

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.utils.PreferenceManager

class DispatchViewModel(file :String, isProgress: Boolean? =false,app: Application): AndroidViewModel(app) {
	
	var challanListLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	val events = MutableLiveData<EVENTS>()
	var fileName: MutableLiveData<String> = MutableLiveData<String>(file)
	
	val isApiCalling: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val isPartialFile: MutableLiveData<Boolean> = MutableLiveData<Boolean>(isProgress)
	val allScanned: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val totalCount: MutableLiveData<Int> = MutableLiveData<Int>(0)
	val totalScanned: MutableLiveData<Int> = MutableLiveData<Int>(0)
	val detailsText: MutableLiveData<String> = MutableLiveData<String>("List Of Challans")
	val editable: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
	val close: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val saveEnable: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
	val user: LoginResponseModel? = PreferenceManager.getUser()
	
	init {
		challanListLiveData.value = (arrayListOf<BaseRowModel>())
		events.value = EVENTS.FETCH_FILE_CONTENT
	}
	
	fun onDispatch(view  : View){
		events.value = EVENTS.PROCESS_FILE
	}
	fun onSave(view  : View){
		events.value = EVENTS.SAVE_FILE
	}
	sealed class EVENTS {
		data object FETCH_FILE_CONTENT : EVENTS()
		data object PROCESS_FILE : EVENTS()
		data object SAVE_FILE : EVENTS()
		data object MOVE_TO_SUCCESS : EVENTS()
	}
}