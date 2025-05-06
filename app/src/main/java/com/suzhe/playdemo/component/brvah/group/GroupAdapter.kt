package com.suzhe.playdemo.component.brvah.group

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.kongzue.dialogx.dialogs.PopTip
import com.suzhe.playdemo.R
import com.suzhe.playdemo.databinding.ItemGroupTypeBinding

/**
 * 每一组的Adapter
 *
 */
class GroupAdapter : BaseQuickAdapter<String, GroupAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: ItemGroupTypeBinding = ItemGroupTypeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        item: String?
    ) {
        holder.binding.tvTitle.text = item
        holder.binding.root.setOnClickListener {
            PopTip.show("点击了 $item")
        }

        when (position) {
            0 -> {
                // 第一个item，设置上圆角背景
                holder.binding.root.setBackgroundResource(R.drawable.ic_group_item_top_bg)

                // 设置点间距
                holder.binding.root.updateLayoutParams<MarginLayoutParams> {
                    topMargin = 15
                }

                holder.binding.lineView.visibility = ViewGroup.GONE
            }

            items.size - 1 -> {
                // 最后一个item，设置下圆角背景
                holder.binding.root.setBackgroundResource(R.drawable.ic_group_item_bottom_bg)
            }

            else -> {
                // 其他的，没有圆角的背景
                holder.binding.root.setBackgroundResource(R.drawable.ic_group_item_mid_bg)
            }
        }
    }
}