package com.suzhe.feature.brvah

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.suzhe.feature.brvah.databinding.ItemImageTextBinding
import com.suzhe.feature.brvah.databinding.ItemTextBinding

/**
 *
 */
class BRVAHExampleAdapter(data: List<BRVAHEntity>) : BaseMultiItemAdapter<BRVAHEntity>(data) {

    // 普通条目类型数
    class ItemVH(val viewBinding: ItemImageTextBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 分组标题类型
    class HeaderVH(val viewBinding: ItemTextBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 初始化块，用于配置适配器的多种条目类型
    init {
        // 调用此方法，设置普通条目类型
        addItemType(ITEM_TYPE, object : OnMultiItemAdapterListener<BRVAHEntity, ItemVH> {

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): ItemVH {
                // 将 ItemImageTextBinding 对应的布局文件inflate到父视图中
                val viewBinding =
                    ItemImageTextBinding.inflate(LayoutInflater.from(context), parent, false)
                // 返回创建的 ItemVH 对象
                return ItemVH(viewBinding)
            }

            override fun onBind(holder: ItemVH, position: Int, item: BRVAHEntity?) {
                if (item == null) return
                // 设置 textView
                holder.viewBinding.textView.text = item.name
                // 设置 icon
                holder.viewBinding.icon.setImageResource(item.imageResource)
            }
        })
        // 设置分组标题条目类型
            .addItemType(SECTION_TYPE, object : OnMultiItemAdapterListener<BRVAHEntity, HeaderVH> {
                override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): HeaderVH {
                    // 将 ItemTextBinding 对应的布局文件inflate到父视图中
                    val viewBinding =
                        ItemTextBinding.inflate(LayoutInflater.from(context), parent, false)
                    // 返回创建的 HeaderVH 对象
                    return HeaderVH(viewBinding)
                }

                override fun onBind(holder: HeaderVH, position: Int, item: BRVAHEntity?) {
                    if (item == null) return
                    // 将分组标题对应的 more 视图隐藏
                    holder.viewBinding.more.visibility = View.GONE
                    // 设置 header 文字
                    holder.viewBinding.header.text = item.sectionTitle
                }

                override fun isFullSpanItem(itemType: Int): Boolean {
                    // 返回 true，表示分组标题条目占据整行
                    return true
                }

            })
            // 设置根据位置和列表判断条目类型的逻辑
            .onItemViewType { position, list ->
                // 如果当前 position 对应的 list 元素的 isSection 属性为 true，
                // 则返回分组标题条目类型 SECTION_TYPE
                if (list[position].isSection) {
                    SECTION_TYPE
                } else {
                    // 否则返回普通条目类型 ITEM_TYPE
                    ITEM_TYPE
                }
            }
    }

    // 伴生对象，用于定义类级别的常量
    companion object {
        // 定义普通条目类型的常量值
        private const val ITEM_TYPE = 10
        // 定义分组标题条目类型的常量值
        private const val SECTION_TYPE = 11
    }
}