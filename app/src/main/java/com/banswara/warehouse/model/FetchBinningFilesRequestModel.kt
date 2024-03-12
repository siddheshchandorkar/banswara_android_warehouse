package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class FetchBinningFilesRequestModel(
	@SerializedName("UserId")
	val userId: Int,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("bActiveDevice")
	val activeStatus: Boolean,
)
