package com.banswara.warehouse.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_details")
data class CustomerDetailsModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "customer_name")
    var customerName: String = "",
    @ColumnInfo(name = "customer_mobile")
    var customerMobile: String = "",
    @ColumnInfo(name = "customer_email")
    var customerEmail: String = ""
)