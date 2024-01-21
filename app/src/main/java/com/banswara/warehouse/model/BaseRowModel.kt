package com.banswara.warehouse.model

import android.content.Context
import androidx.lifecycle.MutableLiveData

abstract class BaseRowModel {

    val TAG: String = javaClass.name

    companion object {

        val STATE_API_CALLED = "API_CALLED"
        val STATE_API_COMPLETE = "API_COMPLETE"
    }

    val currentState: MutableLiveData<String> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    var layoutID: Int = 0
        protected set
    lateinit var appContext: Context

    init {
        this.setLayoutID()
    }

    abstract fun setLayoutID()
}
