package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class ChallanFileModel(
	@SerializedName("vFileName")
	val fileName: String, //Use to fetch files
	@SerializedName("vFileContent")
	val fileContent: String, //use to read file
	@SerializedName("vErrorMsg")
	val errorMsg: String
)
