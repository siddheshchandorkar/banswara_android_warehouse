package com.banswara.warehouse.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.banswara.warehouse.BR
import com.banswara.warehouse.model.BaseRowModel


class RecyclerViewBindingAdapter(val data: ArrayList<BaseRowModel>) :
    RecyclerView.Adapter<RecyclerViewBindingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val dataViewBinding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, i, viewGroup, false)
        return ViewHolder(dataViewBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.bind(data[i])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].layoutID
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder(private val mDataViewBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(mDataViewBinding.root) {

        fun bind(dataModel: Any) {
            mDataViewBinding.setVariable(BR.vm, dataModel)
            mDataViewBinding.executePendingBindings()
        }
    }
    
    fun setData( list: List<BaseRowModel>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}
