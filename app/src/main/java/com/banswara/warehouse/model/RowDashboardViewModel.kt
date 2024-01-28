package com.banswara.warehouse.model

import android.view.View
import androidx.databinding.ObservableField
import com.banswara.warehouse.R

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