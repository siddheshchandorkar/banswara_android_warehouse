package com.banswara.warehouse.model

import com.google.gson.annotations.SerializedName

open class BaseResponseModel{
    @SerializedName("vErrorMsg")
    open val errorMsg:String = ""
}
