package com.suzhe.playdemo.component.brvah.animation

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.kongzue.dialogx.dialogs.PopTip
import com.suzhe.playdemo.R

class AnimationAdapter() : BaseQuickAdapter<String, QuickViewHolder>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_text, parent)
    }

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: String?
    ) {
        holder.setText(R.id.header, item)
        holder.getView<TextView>(R.id.header).setOnClickListener {
            PopTip.show("点击了 $item")
        }
    }

}