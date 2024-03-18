package com.banswara.warehouse.dashboard

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel

class RowTabsViewModel(val context: Context, val list: ArrayList<BaseRowModel>, val searchRequired : Boolean? = false) :
	BaseRowModel() {
	
	var listLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	var filterListLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	var searchText : MutableLiveData<String> = MutableLiveData<String>()
	var searchRequire : MutableLiveData<Boolean> = MutableLiveData<Boolean>(searchRequired)
	init {
		listLiveData.value = list
//		filterListLiveData.value = list
	}
	
	var textWatcher = object : TextWatcher{
		override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
		
		
		}
		
		override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

		}
		
		override fun afterTextChanged(text: Editable?) {
			
			text?.let {
				if(!TextUtils.isEmpty(it.toString())){
					val tempList = arrayListOf<BaseRowModel>()
					
					list.forEach { row->
						if(row is RowFilesViewModel){
							if(!TextUtils.isEmpty(row.fileName.get()) &&
								row.fileName.get()!!.contains(it.toString())){
								tempList.add(row)
							}
						}
					}
					
					listLiveData.value = tempList
					
				}else{
					listLiveData.value = list
				}
			}

		}
		
	}
	
	override fun setLayoutID() {
		layoutID = R.layout.row_viewpager
	}
	
	
}