package com.banswara.warehouse.product_list

import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.FileContentModel

class RowDashboardViewModel(challanFileModel: FileContentModel) : BaseRowModel() {
	
	val challanNo: ObservableField<String> = ObservableField(challanFileModel.fileContent)
	val isSelected: ObservableField<Boolean> = ObservableField(false)
	override fun setLayoutID() {
		layoutID = R.layout.row_dashboard
	}
	
}