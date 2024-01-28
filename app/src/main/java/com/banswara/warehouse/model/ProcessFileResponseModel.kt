package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class ProcessFileResponseModel(
	@SerializedName("UserId")
	val userId: Int,
	@SerializedName("vDeviceId")
	val deviceId: String,
	@SerializedName("vFileName")
	val fileName: String,
	@SerializedName("oFileList")
	val fileList: String,
	@SerializedName("vErrorMsg")
	val errorMsg: String
)
