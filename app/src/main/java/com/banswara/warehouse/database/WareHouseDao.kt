package com.banswara.warehouse.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.banswara.warehouse.model.BinningModel
import com.banswara.warehouse.model.ChallanFileModel
import com.banswara.warehouse.model.FileContentModel
import com.banswara.warehouse.model.LoginResponseModel

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
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	@Transaction
	fun insertChallan(challan: FileContentModel) : Long
	
	//Update Challan Status
	@Update(entity = FileContentModel::class)
	@Transaction
	fun updateChallanStatus(fileContentModel: FileContentModel)
	
	//Get Challan list for corresponding file
	@Query("SELECT * FROM 'challan_details'  WHERE file_name= :fileName AND file_status= :status")
	fun getDispatchChallanByFile(fileName : String, status:String) : List<FileContentModel>
	
	
	//Binning process
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	@Transaction
	fun insertBinningChallan(file: BinningModel) : Long
	
	//Binning process
	@Query("SELECT DISTINCT file_name FROM 'binning_details'  ")
	fun getBinningFileList() : List<String>
	
	@Query("SELECT DISTINCT challan_number FROM 'binning_details'  WHERE file_name= :fileName")
	fun getBinningChallanByFile(fileName : String) : List<String>
	
	@Query("DELETE FROM 'binning_details'  WHERE file_name= :fileName")
	fun deleteBinningChallanByFile(fileName : String)
	
	
	
	
}