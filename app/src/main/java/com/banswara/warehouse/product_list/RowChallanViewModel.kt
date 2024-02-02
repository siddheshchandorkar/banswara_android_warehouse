package com.banswara.warehouse.product_list

import android.view.View
import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.FileContentModel
import com.banswara.warehouse.utils.StatusRetention

class RowChallanViewModel(private val challanFileModel: FileContentModel, private val challanClick: ChallanClick) : BaseRowModel() {
	
	val challanNo: ObservableField<String> = ObservableField(challanFileModel.fileContent)
	val isSelected: ObservableField<Boolean> = ObservableField(false)
	val status: ObservableField<StatusRetention.StatusValue> = ObservableField()
	override fun setLayoutID() {
		layoutID = R.layout.row_challan
	}
	
	fun onChallanSelected(view : View){
		challanClick.onChallanClick(challanFileModel = challanFileModel)
	}
	
	interface ChallanClick{
		fun onChallanClick(challanFileModel: FileContentModel)
	}
}