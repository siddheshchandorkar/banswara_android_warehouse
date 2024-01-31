// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	id("com.android.application") version "8.2.0" apply false
	id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}
buildscript {
	repositories {
		google()
		mavenCentral()
		maven("https://jitpack.io")
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
		classpath("com.android.tools.build:gradle:4.1.3")
		classpath("com.google.gms:google-services:4.4.0")
		// NOTE: Do not place your application dependencies here; they belong
		// in the individual module build.gradle files
	}
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}