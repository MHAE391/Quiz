package com.m391.quiz.utils

import android.annotation.SuppressLint
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import java.util.logging.Handler

abstract class BaseRecyclerViewAdapter<T>(private val callback: ((item: T) -> Unit)? = null) :
    RecyclerView.Adapter<DataBindingViewHolder<T>>() {

    private var _items: MutableList<T> = mutableListOf()


    /**
     * Returns the _items data
     */
    private val items: List<T>
        get() = this._items

    override fun getItemCount() = _items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil
            .inflate<ViewDataBinding>(layoutInflater, getLayoutRes(viewType), parent, false)

        binding.lifecycleOwner = getLifecycleOwner()

        return DataBindingViewHolder(binding)
    }


    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            callback?.invoke(item)
        }
    }

    fun getItem(position: Int) = _items[position]

    /**
     * Adds data to the actual Dataset
     *
     * @param items to be merged
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<T>) {
        _items.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: T, position: Int) {
        _items.add(position, item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(item: T) {
        _items.remove(item)
        notifyDataSetChanged()
    }

    /**
     * Clears the _items data
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        _items.clear()
        notifyDataSetChanged()
    }

    @LayoutRes
    abstract fun getLayoutRes(viewType: Int): Int

    open fun getLifecycleOwner(): LifecycleOwner? {
        return null
    }
}