plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
	id("kotlin-parcelize")
	
}

android {
	namespace = "com.banswara.warehouse"
	compileSdk = 34
	
	defaultConfig {
		applicationId = "com.banswara.warehouse"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"
		
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	
	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		dataBinding = true
		viewBinding = true
	}
}


dependencies {
	
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.google.android.material:material:1.11.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("androidx.databinding:databinding-runtime:8.2.1")
//	implementation("com.android.databinding:compiler:3.1.4")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	
	
	//Retrofit
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
	implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0")
	
	//ROOM DB
	implementation("androidx.room:room-ktx:2.6.1")
	implementation("androidx.room:room-runtime:2.6.1")
	annotationProcessor("androidx.room:room-compiler:2.6.1")
	kapt("androidx.room:room-compiler:2.6.1")
	
	//QR code scanner
	implementation ("com.google.android.gms:play-services-code-scanner:16.1.0")
//	implementation 'com.google.android.gms:play-services-base:18.3.0'
//	implementation 'com.google.android.gms:play-services-tflite-java:16.2.0-beta02'
	implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
	
	
}