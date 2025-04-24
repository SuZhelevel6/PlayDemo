package com.suzhe.playdemo.component.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suzhe.playdemo.R
import com.suzhe.playdemo.databinding.ItemImageBinding
import com.suzhe.playdemo.databinding.ItemImageTextBinding
import com.suzhe.playdemo.databinding.ItemTextBinding


// 文本条目的 ViewHolder，负责绑定文本数据
class TextViewHolder(
    parent: ViewGroup,
    private val binding: ItemTextBinding = ItemTextBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(text: String) {
        binding.textView.text = text
    }
}

// 图片条目的 ViewHolder，负责绑定图片资源
class ImageViewHolder(
    parent: ViewGroup,
    private val binding: ItemImageBinding = ItemImageBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(imageRes: Int) {
        binding.imageView.setImageResource(imageRes)
    }
}

// 混合条目的 ViewHolder，负责绑定图片资源和文本数据
class ImageTextViewHolder(
    parent: ViewGroup,
    private val binding: ItemImageTextBinding = ItemImageTextBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Pair<Int, String>) {
        binding.imageViewLeft.setImageResource(item.first)
        binding.textViewRight.text = item.second
    }
}