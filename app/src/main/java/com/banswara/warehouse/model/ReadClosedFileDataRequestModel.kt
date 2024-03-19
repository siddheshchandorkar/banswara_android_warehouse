package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class ReadClosedFileDataRequestModel(
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("vFileName")
	val fileName: String
)
