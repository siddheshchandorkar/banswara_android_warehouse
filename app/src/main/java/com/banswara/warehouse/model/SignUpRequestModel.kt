package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class SignUpRequestModel(
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("nPinNo")
	val pinNo: Long,
	@SerializedName("vUserName")
	val userName: String,
	@SerializedName("vMobile")
	val mobileNumber: String,
	@SerializedName("bActiveStatus")
	val activeStatus: Boolean = true
)
