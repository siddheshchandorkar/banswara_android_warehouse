package com.banswara.warehouse.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.banswara.warehouse.R
import com.banswara.warehouse.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash_screen)
		Handler(Looper.getMainLooper()).postDelayed({
			startActivity(Intent(this, LoginActivity::class.java))
		},3000)
	}
}