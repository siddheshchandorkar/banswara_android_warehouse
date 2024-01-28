package com.banswara.warehouse.dashboard

import com.google.gson.annotations.SerializedName

data class ReadFileDataRequestModel(
	@SerializedName("UserId")
	val userId: Int,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("vFileName")
	val fileName: String
)
