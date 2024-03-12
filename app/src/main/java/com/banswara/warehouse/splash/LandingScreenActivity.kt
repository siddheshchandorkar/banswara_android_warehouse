package com.banswara.warehouse.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.banswara.warehouse.R
import com.banswara.warehouse.dashboard.DashboardActivity
import com.banswara.warehouse.login.LoginActivity
import com.banswara.warehouse.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LandingScreenActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash_screen)
		PreferenceManager.init(this)
		
		
	}
	
	override fun onResume() {
		super.onResume()
		
		CoroutineScope(Dispatchers.Main).launch {
			delay(2000)
			PreferenceManager.getUser()?.let {
				it.isLogout?.let {logout->
					if(!logout){
						startActivity(Intent(this@LandingScreenActivity, DashboardActivity::class.java))
					}else{
						startActivity(Intent(this@LandingScreenActivity, LoginActivity::class.java))
					}
				}?:run {
					startActivity(Intent(this@LandingScreenActivity, LoginActivity::class.java))
				}
				
				
			}?:run {
				startActivity(Intent(this@LandingScreenActivity, LoginActivity::class.java))
			}
//			startActivity(Intent(this@LandingScreenActivity, LoginActivity::class.java))
			
			finish()
		}
		
	}
}