package com.banswara.warehouse.dashboard

import android.view.View
import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.ChallanFileModel
import com.banswara.warehouse.utils.StatusRetention

class RowBinningFilesViewModel(private val binningFile : String, val fileClick: FileClick) : BaseRowModel() {
	
	val fileName: ObservableField<String> = ObservableField(binningFile)
	
	override fun setLayoutID() {
		layoutID = R.layout.row_binning_files
	}
	
	fun onFileSelected(view : View){
		fileClick.onFileSelect(binningFile)
	}
	
	interface FileClick{
		fun onFileSelect(fileName : String)
	}
	
}