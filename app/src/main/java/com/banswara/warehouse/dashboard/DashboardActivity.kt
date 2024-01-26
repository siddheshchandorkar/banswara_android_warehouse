package com.banswara.warehouse.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.banswara.warehouse.R
import com.banswara.warehouse.databinding.ActivityDashboardBinding
import com.banswara.warehouse.model.BaseRowModel
import com.banswara.warehouse.model.DashboardModel

class DashboardActivity : AppCompatActivity(), RowDashboardViewModel.OnClick {
	
	private lateinit var viewModel: DashboardViewModel
	private lateinit var binding: ActivityDashboardBinding
	val list2 = arrayListOf<BaseRowModel>()
	val list = arrayListOf<BaseRowModel>()
	
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
		viewModel = DashboardViewModel()
		binding.vm = viewModel
		binding.lifecycleOwner = this
		
		list.add(RowDashboardViewModel( DashboardModel("4"),this))
		list.add(RowDashboardViewModel( DashboardModel("5"),this))
		list.add(RowDashboardViewModel( DashboardModel("6"),this))
		list.add(RowDashboardViewModel( DashboardModel("7"),this))
		list.add(RowDashboardViewModel( DashboardModel("0"),this))
		list.add(RowDashboardViewModel( DashboardModel("1"),this))
		list.add(RowDashboardViewModel( DashboardModel("2"),this))
		list.add(RowDashboardViewModel( DashboardModel("3"),this))
		
		
		list2.add(RowDashboardViewModel( DashboardModel("0"),this))
		list2.add(RowDashboardViewModel( DashboardModel("1"),this))
		list2.add(RowDashboardViewModel( DashboardModel("2"),this))
		list2.add(RowDashboardViewModel( DashboardModel("3"),this))
		
		val listDefault = arrayListOf<BaseRowModel>()
		listDefault.addAll(list)
		viewModel.challanListLiveData.value = listDefault

	}
	
	override fun onClickAtPosition(position: Int) {
		val listDefault = arrayListOf<BaseRowModel>()
		if(position == 6){
			
			listDefault.addAll(list2)
		}else{
			list.forEach{ item ->
				if(item is RowDashboardViewModel){
					if(item.dashboardModel.challanNo.toInt() == position){
						item.isSelected.set(true)
					}else{
						item.isSelected.set(false)
					}
				}
				
			}
			listDefault.addAll(list)
		}
//		viewModel.challanListLiveData.value!!.clear()
		viewModel.challanListLiveData.postValue( listDefault)
		
	}
	
	
}