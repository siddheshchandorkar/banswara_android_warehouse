package com.banswara.warehouse.utils

import androidx.annotation.IntDef

object StatusRetention {
	
	const val PENDING = 0
	const val IN_PROGRESS = 1
	const val SCANNED = 2
	
	@Retention(AnnotationRetention.SOURCE)
	@IntDef(
		PENDING,
		IN_PROGRESS,
		SCANNED)
	annotation class StatusValue
}