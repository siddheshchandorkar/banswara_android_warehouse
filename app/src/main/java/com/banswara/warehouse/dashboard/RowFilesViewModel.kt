package com.banswara.warehouse.dashboard

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.FileContentModel
import com.banswara.warehouse.utils.StatusRetention

class RowFilesViewModel(challanFileModel: FileContentModel) : BaseRowModel() {
	
	val fileName: ObservableField<String> = ObservableField(challanFileModel.fileContent)
	val challanQuantity: ObservableField<Int> = ObservableField(0)
	val status: ObservableField<StatusRetention.StatusValue> = ObservableField()
	override fun setLayoutID() {
		layoutID = R.layout.row_files
	}
}