package com.banswara.warehouse.utils

import android.view.View
import android.widget.EditText
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.banswara.warehouse.model.BaseRowModel

class BindingUtils {

    companion object {
        private const val ROW_DATA = "setRowData"
        private const val VISIBILITY = "android:visibility"
        private const val TITLE = "app:title"
        private const val ERROR = "error"
        private const val TEXT = "text"
        private const val ON_CLICK = "onClick"

        @JvmStatic
        @BindingAdapter(ROW_DATA)
        fun setRowLayoutData(recyclerView: RecyclerView, listData: ArrayList<BaseRowModel>) {
            if (listData == null) return
            var adapter = recyclerView.adapter
            if (adapter == null || listData.isEmpty()) {
                adapter = RecyclerViewBindingAdapter(listData)
                adapter.setHasStableIds(true)
                recyclerView.adapter = adapter
            } else {
                (adapter as RecyclerViewBindingAdapter ).setData(listData)
//                adapter.notifyDataSetChanged()
            }
        }

        @JvmStatic
        @BindingAdapter(VISIBILITY)
        fun setVisibility(view: View, visibility: Boolean) {
            if (visibility) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }

        }

        @JvmStatic
        @BindingAdapter(ERROR)
        fun bindError(editText: EditText, error: String?) {
            editText.error = error
        }
        
        @JvmStatic
        @BindingAdapter(TITLE)
        fun bindTitle(editText: Toolbar, text: String?) {
            editText.title = text?:""
        }

    }
}