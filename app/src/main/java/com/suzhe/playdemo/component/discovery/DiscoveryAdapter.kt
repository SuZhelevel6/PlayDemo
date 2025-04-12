package com.suzhe.playdemo.component.discovery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.suzhe.playdemo.component.article.ArticleFragment

class DiscoveryAdapter(fragmentActivity: FragmentActivity, private val datum: Array<String>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return datum.size
    }

    override fun createFragment(position: Int): Fragment {
        return ArticleFragment(position.toString())
    }

}