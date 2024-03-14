package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class FetchPartialFileRequestModel(
	@SerializedName("UserId")
	val userId: Int,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("vFileName")
	val fileName: String,
	@SerializedName("vSourceFile")
	val sourceFile: String ? ="",
	@SerializedName("vDestinationFile")
	val destinationFile: String? = "",
	@SerializedName("bActiveDevice")
	val activeDevice: Boolean? = true
)