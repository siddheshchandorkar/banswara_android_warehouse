package com.banswara.warehouse.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

open class BaseResponseModel{
    @ColumnInfo(name = "error_msg")
    @SerializedName("vErrorMsg")
    open val errorMsg:String = ""
}
