package com.banswara.warehouse.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.banswara.warehouse.R
import com.banswara.warehouse.dashboard.DashboardActivity
import com.banswara.warehouse.login.LoginActivity
import com.banswara.warehouse.utils.PreferenceManager

class SplashScreenActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash_screen)
		PreferenceManager.init(this)
		
		Handler(Looper.getMainLooper()).postDelayed({
			
				PreferenceManager.getUser()?.let {
					startActivity(Intent(this, DashboardActivity::class.java))
					
				}?:run {
					startActivity(Intent(this, LoginActivity::class.java))
				}
			
		},2000)
	}
}