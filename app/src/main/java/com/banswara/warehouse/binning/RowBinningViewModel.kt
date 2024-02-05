package com.banswara.warehouse.binning

import android.view.View
import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.FileContentModel
import com.banswara.warehouse.utils.StatusRetention

class RowBinningViewModel(val challanFileModel: FileContentModel, private val challanClick: ChallanClick) : BaseRowModel() {
	
	val challanNo: ObservableField<String> = ObservableField(challanFileModel.fileContent)
	override fun setLayoutID() {
		layoutID = R.layout.row_challan
	}
	
	fun onChallanSelected(view : View){
		challanClick.onChallanClick(this)
	}
	
	interface ChallanClick{
		fun onChallanClick(challanRow: RowBinningViewModel)
	}
}