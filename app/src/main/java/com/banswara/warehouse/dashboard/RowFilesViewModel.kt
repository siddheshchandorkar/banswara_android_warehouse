package com.banswara.warehouse.dashboard

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.banswara.warehouse.R
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.ChallanFileModel
import com.banswara.warehouse.utils.StatusRetention
import java.text.SimpleDateFormat
import java.util.Date

class RowFilesViewModel(val context : Context,val challanFileModel: ChallanFileModel, val fileClick: FileClick) : BaseRowModel() {
	
	val fileName: ObservableField<String> = ObservableField(challanFileModel.fileName)
	val date: ObservableField<String> = ObservableField(dateInFormat(challanFileModel.createdDate))
	val status: ObservableField<String> = ObservableField(challanFileModel.status)
	val statusColor: ObservableField<Drawable> = ObservableField()
	
	init {
		when(challanFileModel.status){
			"Pending" ->{
				statusColor.set(ContextCompat.getDrawable(context, R.drawable.bg_round_orange))
			}
			"InProgress" ->{
				statusColor.set(ContextCompat.getDrawable(context, R.drawable.bg_round_dark_green))
				
			}
			"Closed" ->{
				statusColor.set(ContextCompat.getDrawable(context, R.drawable.bg_round_dark_red))
				
			}
			else -> {
				statusColor.set(ContextCompat.getDrawable(context, R.drawable.bg_round_orange))
				
			}
		}
	}
	override fun setLayoutID() {
		layoutID = R.layout.row_files
	}
	
	fun onFileSelected(view : View){
		fileClick.onFileSelect(challanFileModel = challanFileModel)
	}
	
	interface FileClick{
		fun onFileSelect(challanFileModel: ChallanFileModel)
	}
	
	fun dateInFormat(inputDate : String): String {
		try {
			val sdfInput = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a")
			val sdfOutput = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
			val date = sdfInput.parse(inputDate)
			return sdfOutput.format(date)
			
		} catch (e: Exception) {
			e.printStackTrace()
			return ""
		}
	}
	
}