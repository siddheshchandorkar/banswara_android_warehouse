package com.banswara.warehouse.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.banswara.warehouse.dashboard.RowFilesViewModel
import com.banswara.warehouse.dashboard.RowFragment
import com.banswara.warehouse.model.ChallanFileModel

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val list : ArrayList<ArrayList<ChallanFileModel>>,val fileClick: RowFilesViewModel.FileClick) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return RowFragment.getInstance(list = list.get(0), true, fileClick = fileClick)
            1 -> return RowFragment.getInstance(list = list.get(1), false, fileClick)
            2 -> return RowFragment.getInstance(list = list.get(2), true,fileClick)
        }
        return RowFragment.getInstance(list = list.get(0), true, fileClick = fileClick)
        
    }
}