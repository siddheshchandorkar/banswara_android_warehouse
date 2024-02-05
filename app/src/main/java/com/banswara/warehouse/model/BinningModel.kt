package com.banswara.warehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.banswara.warehouse.utils.StatusRetention
import com.google.gson.annotations.SerializedName

@Entity(tableName = "binning_details")
data class BinningModel(
	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "challan_number")
	@SerializedName("vFileContent")
	var fileContent: String = "", //use to read file
	
	@ColumnInfo(name = "file_name", defaultValue = "")
	@SerializedName("vFileName")
	var fileName: String = "", //Use to fetch files
){
	override fun toString(): String {
		return fileContent
	}
}
