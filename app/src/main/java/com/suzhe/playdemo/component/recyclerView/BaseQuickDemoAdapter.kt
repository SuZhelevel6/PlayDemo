package com.suzhe.playdemo.component.recyclerView

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.suzhe.playdemo.component.recyclerView.RecyclerViewDemoActivity.Companion.TYPE_IMAGE
import com.suzhe.playdemo.component.recyclerView.RecyclerViewDemoActivity.Companion.TYPE_IMAGE_TEXT
import com.suzhe.playdemo.component.recyclerView.RecyclerViewDemoActivity.Companion.TYPE_TEXT


/**
 * BaseQuickAdapter 的泛型需要传入具体的数据类型，而不是 List
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/BaseQuickAdapter
 */
class BaseQuickDemoAdapter() : BaseQuickAdapter<Any, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // 返回一个 ViewHolder
        return when (viewType) {
            // 如果条目类型是 TYPE_TEXT，加载文本条目的布局
            TYPE_TEXT -> TextViewHolder(parent)
            // 如果条目类型是 TYPE_IMAGE，加载图片条目的布局
            TYPE_IMAGE -> ImageViewHolder(parent)
            // 如果条目类型是 TYPE_IMAGE_TEXT，加载混合条目的布局
            TYPE_IMAGE_TEXT -> ImageTextViewHolder(parent)
            // 如果条目类型无效，抛出异常
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: Any?) {
        // 绑定数据到 ViewHolder
        when (getItemViewType(position)) {
            TYPE_TEXT -> (holder as TextViewHolder).bind(items[position] as String)
            TYPE_IMAGE -> (holder as ImageViewHolder).bind(items[position] as Int)
            TYPE_IMAGE_TEXT -> (holder as ImageTextViewHolder).bind(items[position] as Pair<Int, String>)
        }
    }

    override fun getItemViewType(position: Int, list: List<Any>): Int {
        // 返回自定义的 itemViewType
        return when (list[position]) {
            // 如果是字符串类型，返回文本条目类型
            is String -> TYPE_TEXT

            // 如果是整数类型，返回图片条目类型
            is Int -> TYPE_IMAGE

            // 如果是 Pair 类型（包含图片和文本），返回图片在左，文本在右条目类型
            is Pair<*, *> -> TYPE_IMAGE_TEXT

            // 如果条目类型无效，抛出异常
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }
}