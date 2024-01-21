package com.banswara.warehouse.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.banswara.warehouse.R
import com.banswara.warehouse.databinding.ActivityDashboardBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.DashboardModel

class DashboardActivity : AppCompatActivity() {
	
	private lateinit var viewModel: DashboardViewModel
	private lateinit var binding: ActivityDashboardBinding
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
		viewModel = DashboardViewModel()
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		val list = mutableListOf<BaseRowModel>()
		list.add(RowDashboardViewModel( DashboardModel("1")))
		list.add(RowDashboardViewModel( DashboardModel("2")))
		list.add(RowDashboardViewModel( DashboardModel("3")))
		list.add(RowDashboardViewModel( DashboardModel("4")))
		viewModel.challanListLiveData.value = list

	}
}