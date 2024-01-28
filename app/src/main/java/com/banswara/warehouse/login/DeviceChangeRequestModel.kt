package com.banswara.warehouse.login

import com.google.gson.annotations.SerializedName

data class DeviceChangeRequestModel(
	@SerializedName("UserId")
	val userId: Int,
	@SerializedName("bActiveStatus")
	val activeStatus: Boolean,
	@SerializedName("nPinNo")
	val pinNo: Long,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("vMobile")
	val mobileNumber: String,
	@SerializedName("vUserName")
	val userName: String
)
