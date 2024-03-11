package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
	@SerializedName("nPinNo")
	val pinNo: Long,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("bActiveDevice")
	val status: Boolean = true
)
