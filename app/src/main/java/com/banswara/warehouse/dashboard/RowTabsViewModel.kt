package com.banswara.warehouse.dashboard

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel

class RowTabsViewModel(val context: Context, val list: ArrayList<BaseRowModel>) :
	BaseRowModel() {
	
	var listLiveData: MutableLiveData<ArrayList<BaseRowModel>> =
		MutableLiveData<ArrayList<BaseRowModel>>()
	
	init {
		listLiveData.value = list
	}
	
	override fun setLayoutID() {
		layoutID = R.layout.row_viewpager
	}
	
	
}