package com.suzhe.playdemo.component.brvah.animation

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.kongzue.dialogx.dialogs.PopTip
import com.suzhe.playdemo.R

/**
 * 使用BaseQuickAdapter的注意点
 * 1. 如果 ViewHolder 很简单，可以直接使用 QuickViewHolder 并指定视图
 * 2. 不需要 getItemCount 方法
 * 3. 传入数据可以使用参数传递，也可以使用 submitList 方法
 * 4. BaseQuickAdapter 泛型参数的第一个参数是 具体的数据类型 而不是 List
 */
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