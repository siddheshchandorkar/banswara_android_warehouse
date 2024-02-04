package com.banswara.warehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_master")
data class LoginResponseModel(
	@PrimaryKey
	@ColumnInfo(name = "user_id")
	@SerializedName("UserId")
	val userId: Int,
	
	@ColumnInfo(name = "active_status")
	@SerializedName("bActiveStatus")
	val activeStatus: Boolean,
	
	@ColumnInfo(name = "pin_no")
	@SerializedName("nPinNo")
	val pinNo: Long,
	
	@ColumnInfo(name = "device_id")
	@SerializedName("vDeviceId")
	val deviceId: String,
	
	@ColumnInfo(name = "mobile")
	@SerializedName("vMobile")
	val mobileNumber: String,
	
	@ColumnInfo(name = "user_name")
	@SerializedName("vUserName")
	val userName: String,
	
	@SerializedName("vErrorMsg")
	var errorMsg: String = ""
)
