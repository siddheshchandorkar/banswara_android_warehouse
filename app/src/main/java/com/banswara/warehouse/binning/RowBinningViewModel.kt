package com.banswara.warehouse.binning

import android.view.View
import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.BinningModel
import com.banswara.warehouse.model.FileContentModel
import com.banswara.warehouse.utils.StatusRetention

class RowBinningViewModel(val binningModel: BinningModel) : BaseRowModel() {
	
	val challanNo: ObservableField<String> = ObservableField(binningModel.fileContent)
	override fun setLayoutID() {
		layoutID = R.layout.row_binning
	}
	
}