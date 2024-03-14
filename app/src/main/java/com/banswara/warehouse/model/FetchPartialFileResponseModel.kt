package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class FetchPartialFileResponseModel(
	@SerializedName("UserId")
	val userId: Int,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("vFileName")
	val fileName: String,
	@SerializedName("vStatus")
	val status: String,
	@SerializedName("vErrorMsg")
	val errorMsg: String,
	@SerializedName("oChallanList")
	val challanList: List<BinningChallanModel>
)