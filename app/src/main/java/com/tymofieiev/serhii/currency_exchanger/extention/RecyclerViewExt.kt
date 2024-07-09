package com.tymofieiev.serhii.currency_exchanger.extention

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.tymofieiev.serhii.currency_exchanger.R


/*
* Created by Serhii Tymofieiev
*/
fun RecyclerView.addGradientItemDecoration() {
    val decorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    decorator.setDrawable(
        ContextCompat.getDrawable(
            context,
            R.drawable.divider_gradient_item_decoration
        )!!
    )
    addItemDecoration(decorator)
}