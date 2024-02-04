package com.banswara.warehouse.utils

import androidx.annotation.IntDef

object StatusRetention {
	
	const val PENDING = 0
	const val IN_PROGRESS = 1
	const val SCANNED = 2
	
	const val STATUS_PENDING = "Pending"
	const val STATUS_IN_PROGRESS = "In Progress"
	const val STATUS_SCANNED = "Scanned"
	
	@Retention(AnnotationRetention.SOURCE)
	@IntDef(
		PENDING,
		IN_PROGRESS,
		SCANNED)
	annotation class StatusValue
	
	fun getStatus(value: Int) : String{
		return when(value){
			PENDING -> STATUS_PENDING
			IN_PROGRESS -> STATUS_IN_PROGRESS
			SCANNED -> STATUS_SCANNED
			else -> ""
		}
	}
}