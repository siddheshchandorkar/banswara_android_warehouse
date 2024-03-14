package com.banswara.warehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.banswara.warehouse.utils.StatusRetention
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "file_master", primaryKeys = arrayOf("id", "file_name"))
@Entity(tableName = "file_master")
data class ChallanFileModel(
	
	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "file_name")
	@SerializedName("vFileName")
	var fileName: String ="", //Use to fetch files
	
	
	@ColumnInfo(name = "file_status", defaultValue = StatusRetention.STATUS_PENDING)
	var fileStatus: String = "",
	
	@ColumnInfo(name = "status", defaultValue = StatusRetention.STATUS_PENDING)
	@SerializedName("vStatus")
	var status: String = "",
	
	@ColumnInfo(name = "created_date")
	@SerializedName("vFileCreatedDate")
	var createdDate: String = "",
	
	@ColumnInfo(name = "device_id")
	@SerializedName("vDeviceId")
	var deviceId: String = "",
	
	@Ignore
	@SerializedName("vErrorMsg")
	var errorMsg: String = ""
	
	) {
	override fun toString(): String {
		return fileName
	}
}
