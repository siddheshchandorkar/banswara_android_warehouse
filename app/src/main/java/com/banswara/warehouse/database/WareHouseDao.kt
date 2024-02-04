package com.banswara.warehouse.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update
import com.banswara.warehouse.model.ChallanFileModel
import com.banswara.warehouse.model.LoginResponseModel
import com.banswara.warehouse.model.SignUpRequestModel

@Dao
interface WareHouseDao {

//    @Query("SELECT * FROM `customer_details` WHERE transaction_date= :date")
//    fun getTransactionByDate(date: String): CustomerDetailsModel

	//User for Sign Up
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(user: LoginResponseModel): Long
	
	//Insert Fetched File
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	@Transaction
	fun insertFiles(fileList: List<ChallanFileModel>)
	
	//Insert Fetched File
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	@Transaction
	fun insertFile(file: ChallanFileModel) : Long
	
	//Update File Status once OPEN & PROCESSED
	@Update(entity = ChallanFileModel::class)
	@Transaction
	fun updateFileStatus(file: ChallanFileModel)
	
	//Insert Challan Details
	
	//Update Challan Status
	
	//Get Challan list for corresponding file
	
	
	
	
}