package com.banswara.warehouse.database

import android.content.Context
import androidx.room.*
import com.banswara.warehouse.model.ChallanFileModel
import com.banswara.warehouse.model.FileContentModel
import com.banswara.warehouse.model.LoginResponseModel


@Database(entities = [LoginResponseModel::class, ChallanFileModel::class, FileContentModel::class], version = 1, exportSchema = true)
abstract class WareHouseDB : RoomDatabase() {
    abstract fun wareHouseDao(): WareHouseDao

    companion object {
        private var wareHouseDBInstance: WareHouseDB? = null
        private const val DB_NAME = "ware_house.db"

        fun getDataBase(context: Context): WareHouseDB? {
            if (wareHouseDBInstance == null) {

                synchronized(WareHouseDB::class) {
                    wareHouseDBInstance = Room.databaseBuilder(
                        context,
                        WareHouseDB::class.java, DB_NAME
                    ).allowMainThreadQueries().build()
                }
            }
            return wareHouseDBInstance
        }

        fun destroyDataBase() {
            wareHouseDBInstance = null
        }
    }
}