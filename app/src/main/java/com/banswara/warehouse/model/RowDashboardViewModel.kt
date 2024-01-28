package com.banswara.warehouse.model

import androidx.databinding.ObservableField
import com.banswara.warehouse.R

class RowDashboardViewModel(challanFileModel: FileContentModel) : BaseRowModel() {
	
	val challanNo: ObservableField<String> = ObservableField(challanFileModel.fileContent)
	val isSelected: ObservableField<Boolean> = ObservableField(false)
	override fun setLayoutID() {
		layoutID = R.layout.row_dashboard
	}
	
}