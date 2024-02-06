package com.banswara.warehouse.binning

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.utils.PreferenceManager

class BinningViewModel(app: Application) : AndroidViewModel(app) {
	
	var challanListLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	val events = MutableLiveData<EVENTS>()
	var fileName: MutableLiveData<String> = MutableLiveData<String>()
	
	val isApiCalling: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val allScanned: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
	val user: LoginResponseModel? = PreferenceManager.getUser()
	
	var first: MutableLiveData<String> = MutableLiveData<String>()
	var second: MutableLiveData<String> = MutableLiveData<String>()
	var third: MutableLiveData<String> = MutableLiveData<String>()
	
	init {
		challanListLiveData.value = (arrayListOf<BaseRowModel>())
		if(!TextUtils.isEmpty(fileName.value)){
			events.value = EVENTS.FETCH_FILE_CONTENT
		}
	}
	
	fun locationValidation() : Boolean{
		if(TextUtils.isEmpty(first.value)){
			events.value = EVENTS.SHOW_TOAST("Enter First Location")
			return false
		}else if(first.value!!.length< 2){
			events.value = EVENTS.SHOW_TOAST("The first location should be at least 2 digits.")
			return false
		}else if(TextUtils.isEmpty(second.value)){
			events.value = EVENTS.SHOW_TOAST("Enter Second Location")
			return false
		}else if(second.value!!.length< 2){
			events.value = EVENTS.SHOW_TOAST("The Second location should be at least 2 digits.")
			return false
		}else if(TextUtils.isEmpty(third.value)){
			events.value = EVENTS.SHOW_TOAST("Enter Third Location")
			return false
		}else if(third.value!!.length< 2){
			events.value = EVENTS.SHOW_TOAST("The Third location should be at least 2 digits.")
			return false
		}
			
			return true
	}
	
	fun onShipped(view: View) {
		if(locationValidation()){
			events.value = EVENTS.PROCESS_FILE
		}
	}
	
	sealed class EVENTS {
		data object FETCH_FILE_CONTENT : EVENTS()
		data object PROCESS_FILE : EVENTS()
		data object MOVE_TO_SUCCESS : EVENTS()
		data class SHOW_TOAST(val msg : String) : EVENTS()
//		data class SCAN(val challanRow: RowChallanViewModel) : EVENTS()
	}
}