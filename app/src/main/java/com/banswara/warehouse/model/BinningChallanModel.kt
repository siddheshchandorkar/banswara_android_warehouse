package com.banswara.warehouse.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.banswara.warehouse.utils.StatusRetention
import com.google.gson.annotations.SerializedName

data class BinningChallanModel(
	@SerializedName("vChallan")
	var challan: String = ""
)