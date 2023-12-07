package com.m391.quiz.ui.authentication.information.shared

import android.annotation.SuppressLint
import android.widget.CheckBox
import android.widget.RadioButton
import com.m391.quiz.R
import com.m391.quiz.models.CheckItem
import com.m391.quiz.utils.BaseRecyclerViewAdapter
import com.m391.quiz.utils.DataBindingViewHolder

class ItemAdapter(
    private val checkBoxOrRadioButton: Boolean,
    private val callback: (item: CheckItem) -> Unit
) :
    BaseRecyclerViewAdapter<CheckItem>(callback) {
    override fun getLayoutRes(viewType: Int) =
        if (checkBoxOrRadioButton) R.layout.check_item_with_check_box
        else R.layout.check_item_with_radio_button

    override fun onBindViewHolder(holder: DataBindingViewHolder<CheckItem>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        if (checkBoxOrRadioButton)
            holder.itemView.findViewById<CheckBox>(R.id.checkbox)
                .setOnCheckedChangeListener { _, isChecked ->
                    val newItem = item.copy(isChecked = isChecked)
                    callback.invoke(newItem)
                }
        else holder.itemView.findViewById<RadioButton>(R.id.radio_button)
            .setOnClickListener {
                callback.invoke(item)
            }
    }
}