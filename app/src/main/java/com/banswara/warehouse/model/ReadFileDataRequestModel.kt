package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class ReadFileDataRequestModel(
	@SerializedName("UserId")
	val userId: Int,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("vFileName")
	val fileName: String
)
