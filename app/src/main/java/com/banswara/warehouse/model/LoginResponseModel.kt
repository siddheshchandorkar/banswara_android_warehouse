package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
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
	val userName: String,
	@SerializedName("vErrorMsg")
	val errorMsg: String
)
