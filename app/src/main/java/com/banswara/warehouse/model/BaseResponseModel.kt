package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

data class BaseResponseModel(
    @SerializedName("vErrorMsg")
    val errorMsg:String
)
