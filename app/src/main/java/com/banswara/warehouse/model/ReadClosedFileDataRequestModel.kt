package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class ReadClosedFileDataRequestModel(
	@SerializedName("vFileName")
	val fileName: String
)
