package com.banswara.warehouse.dashboard

import android.view.View
import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.DashboardModel

class RowDashboardViewModel(public val dashboardModel: DashboardModel, val onClick : OnClick) : BaseRowModel(){
	
	val challanNo = ObservableField(dashboardModel.challanNo)
	val isSelected = ObservableField(false)
	override fun setLayoutID() {
		layoutID = R.layout.row_dashboard
	}
	
	
	fun click(view: View){
		onClick.onClickAtPosition(dashboardModel.challanNo.toInt())
	}
	
	interface OnClick{
		fun onClickAtPosition(position : Int)
	}
}