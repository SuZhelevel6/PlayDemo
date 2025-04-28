package com.suzhe.playdemo.component.brvah.drag

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.dragswipe.listener.DragAndSwipeDataCallback
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.kongzue.dialogx.dialogs.PopTip
import com.suzhe.playdemo.R

class DragAndSwipeAdapter : BaseQuickAdapter<String, QuickViewHolder>(), DragAndSwipeDataCallback {

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

    override fun dataMove(fromPosition: Int, toPosition: Int) {
        move(fromPosition, toPosition)
    }

    override fun dataRemoveAt(position: Int) {
        removeAt(position)
    }

}