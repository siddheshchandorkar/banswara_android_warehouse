package com.banswara.warehouse.database

import androidx.room.*

@Dao
interface WareHouseDao {

    @Query("select * from 'customer_details' ")
    fun getALLDetails(): List<CustomerDetailsModel>

//    @Query("SELECT * FROM `customer_details` WHERE transaction_date= :date")
//    fun getTransactionByDate(date: String): CustomerDetailsModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(transaction: CustomerDetailsModel): Long


}