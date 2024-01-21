package com.banswara.warehouse.dashboard

import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.model.BaseRowModel

class DashboardViewModel {
	
	var challanListLiveData: MutableLiveData<MutableList<BaseRowModel>> = MutableLiveData()
	
}