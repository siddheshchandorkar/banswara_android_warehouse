package com.banswara.warehouse.login

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
	@SerializedName("nPinNo")
	val pinNo: Long,
	@SerializedName("vDeviceId")
	val deviceId: String
)
