package com.banswara.warehouse.dashboard

import android.view.View
import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.ChallanFileModel
import com.banswara.warehouse.utils.StatusRetention

class RowFilesViewModel(val challanFileModel: ChallanFileModel, val fileClick: FileClick) : BaseRowModel() {
	
	val fileName: ObservableField<String> = ObservableField(challanFileModel.fileName)
	val status: ObservableField<StatusRetention.StatusValue> = ObservableField()
	
	override fun setLayoutID() {
		layoutID = R.layout.row_files
	}
	
	fun onFileSelected(view : View){
		fileClick.onFileSelect(challanFileModel = challanFileModel)
	}
	
	interface FileClick{
		fun onFileSelect(challanFileModel: ChallanFileModel)
	}
	
}