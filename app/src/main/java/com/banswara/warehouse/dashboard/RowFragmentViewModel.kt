package com.banswara.warehouse.dashboard

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.ChallanFileModel

class RowFragmentViewModel(
	val context: Context,
	val list: ArrayList<ChallanFileModel>,
	val searchRequired: Boolean? = false,val fileClick: RowFilesViewModel.FileClick
) : ViewModel() {
	
	var listLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	var searchText: MutableLiveData<String> = MutableLiveData<String>()
	var searchRequire: MutableLiveData<Boolean> = MutableLiveData<Boolean>(searchRequired)
	val tempList = arrayListOf<BaseRowModel>()
	
	init {
		
		list.forEach {
			tempList.add(RowFilesViewModel(context, it, fileClick))
		}
		listLiveData.value = arrayListOf<BaseRowModel>()
		listLiveData.value?.addAll(tempList)
	}
	
	var textWatcher = object : TextWatcher {
		override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
		
		
		}
		
		override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
		
		}
		
		override fun afterTextChanged(text: Editable?) {
			
			text?.let {
				if (!TextUtils.isEmpty(it.toString())) {
					val temp = arrayListOf<BaseRowModel>()
					
					list.forEach { row ->
							if (!TextUtils.isEmpty(row.fileName) &&
								row.fileName.contains(it.toString())
							) {
								temp.add(RowFilesViewModel(context, row, fileClick))
							}
						
					}
					
					listLiveData.value = temp
					
				} else {
					listLiveData.value = arrayListOf<BaseRowModel>()
					listLiveData.value?.addAll(tempList)
				}
			}?:{
				listLiveData.value = arrayListOf<BaseRowModel>()
				listLiveData.value?.addAll(tempList)
				
			}
			
		}
		
	}
	
}