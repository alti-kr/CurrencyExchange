package com.tymofieiev.serhii.currency_exchanger.ui.currency_picker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.BalanceListItemModel
import com.tymofieiev.serhii.currency_exchanger.databinding.CurrencyPickerItemBinding


/*
* Created by Serhii Tymofieiev
*/
class CurrencyPickerAdapter(val onClick: (String) -> Unit)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<String>()
    @SuppressLint("NotifyDataSetChanged")
    fun setup(items: List<String>) {
        // val diffCallback = DiffUtil.calculateDiff(ItemDiffCallback(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
        //diffCallback.dispatchUpdatesTo(this)
    }
    inner class VH(private val binding: CurrencyPickerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var boundItem: String? = null
        init {
            binding.root.setOnClickListener {
                if(boundItem != null){
                    onClick(boundItem!!)
                }
            }
        }

        fun bind(item: String) {
            this.boundItem = item
            binding.data =  this.boundItem
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            CurrencyPickerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VH).bind(items[position])
    }


}