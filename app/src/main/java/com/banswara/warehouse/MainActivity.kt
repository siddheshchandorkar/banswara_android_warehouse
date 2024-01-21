package com.banswara.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.banswara.warehouse.database.CustomerDetailsModel
import com.banswara.warehouse.database.WareHouseDB
import com.banswara.warehouse.login.LoginActivity
import com.banswara.warehouse.network.RetrofitRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		Handler(Looper.getMainLooper()).postDelayed({
//			val database: WareHouseDB? = WareHouseDB.getDataBase(this)
//			database?.let {
//				it.wareHouseDao().insert(CustomerDetailsModel(id = 1, customerName = "Siddhesh",
//					customerMobile = "7208124241",
//					customerEmail = "siddhesh@gmail.com"))
//			}
			startActivity(Intent(this, LoginActivity::class.java))
		},3000)
//		CoroutineScope(Dispatchers.IO).launch {
//			RetrofitRepository.instance.callLoginApi()
//		}
		
	}
}