package com.suzhe.playdemo.component.guid


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class GuideAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val pageImages: List<Int>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return GuideFragment.newInstance(pageImages[position])
    }

    override fun getItemCount(): Int {
        return pageImages.size
    }
}