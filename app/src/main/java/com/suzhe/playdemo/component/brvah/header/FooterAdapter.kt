package com.suzhe.playdemo.component.brvah.header

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseSingleItemAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.suzhe.playdemo.R

class FooterAdapter(
) : BaseSingleItemAdapter<Any, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.top_view, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, item: Any?) {}
}