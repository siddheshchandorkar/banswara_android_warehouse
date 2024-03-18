package com.banswara.warehouse.dashboard

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.banswara.warehouse.R
import com.banswara.warehouse.databinding.RowViewpagerFragBinding
import com.banswara.warehouse.model.ChallanFileModel

class RowFragment : Fragment() {
	
	private lateinit var viewModel: RowFragmentViewModel
	private lateinit var binding: RowViewpagerFragBinding
	private lateinit var  fileClick: RowFilesViewModel.FileClick
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DataBindingUtil.inflate(inflater, R.layout.row_viewpager_frag, container, false)
		var list = ArrayList<ChallanFileModel>()
		var search = false
		arguments?.let {
			if(it.containsKey(KEY_LIST)){
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
					list = it.getParcelableArrayList(KEY_LIST, ChallanFileModel::class.java)!!
				}else{
					list = it.getParcelableArrayList(KEY_LIST)!!
				}
			}
			
			if(it.containsKey(KEY_SEARCH)){
				search = it.getBoolean(KEY_SEARCH, false)
			}
		}
		
		viewModel = RowFragmentViewModel(requireContext(), list, search, fileClick)
		
		binding.vm = viewModel
		binding.lifecycleOwner = viewLifecycleOwner
		
		
		return binding.root
	}
	
	companion object {
		const val KEY_LIST = "list"
		const val KEY_SEARCH = "search"
		
		fun getInstance(
			list: ArrayList<ChallanFileModel>,
			searchRequired: Boolean? = false,fileClick: RowFilesViewModel.FileClick
		): RowFragment {
			val frag = RowFragment()
			frag.fileClick = fileClick
			val bundle = Bundle()
			bundle.putParcelableArrayList(KEY_LIST, list)
			bundle.putBoolean(KEY_SEARCH, searchRequired ?: false)
			frag.arguments = bundle
			
			return frag
			
		}
	}
	
}