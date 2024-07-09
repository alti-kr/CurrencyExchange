package com.tymofieiev.serhii.currency_exchanger.ui.currency_exchanger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.BalanceListItemModel
import com.tymofieiev.serhii.currency_exchanger.databinding.BalanceItemBinding
import com.tymofieiev.serhii.currency_exchanger.extention.toFormattedString


/*
* Created by Serhii Tymofieiev
*/
class BalanceAdapter : ListAdapter<BalanceListItemModel, BalanceAdapter.VH>(object :
    DiffUtil.ItemCallback<BalanceListItemModel>() {
    override fun areItemsTheSame(
        oldItem: BalanceListItemModel,
        newItem: BalanceListItemModel
    ): Boolean {
        return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(
        oldItem: BalanceListItemModel,
        newItem: BalanceListItemModel
    ): Boolean {
        return oldItem == newItem
    }

}) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(BalanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind()
    }

    inner class VH(val binding: BalanceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(bindingAdapterPosition)
            val textHolder = item.balance.toFormattedString(2) + " " + item.symbol
            binding.itemText.text = textHolder

        }
    }
}