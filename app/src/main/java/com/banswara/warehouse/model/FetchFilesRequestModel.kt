package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class FetchFilesRequestModel(
	@SerializedName("UserId")
	val userId: Int,
	@SerializedName("vDeviceId")
	val deviceId: String
)
