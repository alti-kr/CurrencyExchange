package com.tymofieiev.serhii.currency_exchanger.ui.currency_exchanger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tymofieiev.serhii.currency_exchanger.data.domain.models.ExchangeOperationsModel
import com.tymofieiev.serhii.currency_exchanger.databinding.ExchangeOperationItemBinding
import com.tymofieiev.serhii.currency_exchanger.extention.toFormattedString


/*
* Created by Serhii Tymofieiev
*/
class ExchangeOperationsAdapter :
    ListAdapter<ExchangeOperationsModel, ExchangeOperationsAdapter.VH>(object :
        DiffUtil.ItemCallback<ExchangeOperationsModel>() {
        override fun areItemsTheSame(
            oldItem: ExchangeOperationsModel,
            newItem: ExchangeOperationsModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ExchangeOperationsModel,
            newItem: ExchangeOperationsModel
        ): Boolean {
            return oldItem == newItem
        }

    }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ExchangeOperationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind()
    }

    inner class VH(val binding: ExchangeOperationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(bindingAdapterPosition)
            var textHolder = ""
            with(binding) {
                textHolder = item.sumSell.toFormattedString() + " " + item.currencySellSymbol
                tv01.text = textHolder
                textHolder = item.sumBuy.toFormattedString() + " " + item.currencyBuySymbol
                tv11.text = textHolder
                textHolder = item.sumFee.toFormattedString() + " " + item.currencySellSymbol
                tv03.text = textHolder
                textHolder = item.sumTotal.toFormattedString() + " " + item.currencySellSymbol
                tv13.text = textHolder
            }
        }
    }
}