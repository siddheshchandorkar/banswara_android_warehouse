package com.banswara.warehouse.dashboard

import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.DashboardModel

class RowDashboardViewModel(val dashboardModel: DashboardModel) : BaseRowModel(){
	
	val challanNo = ObservableField(dashboardModel.challanNo)
	override fun setLayoutID() {
		layoutID = R.layout.row_dashboard
	}
	
	
}