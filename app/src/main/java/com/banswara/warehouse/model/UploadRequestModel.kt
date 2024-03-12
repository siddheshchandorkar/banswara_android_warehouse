package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class UploadRequestModel(
	@SerializedName("UserId")
	val userId: String,
	@SerializedName("bActiveDevice")
	val activeDevice: Boolean,
	@SerializedName("vDT")
	val transactionDate: String,
	@SerializedName("vLocation")
	val location: String,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("oChallanList")
	val challanList: List<BinningChallanModel>
)
