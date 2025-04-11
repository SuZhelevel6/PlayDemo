package com.suzhe.playdemo.component.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.suzhe.playdemo.component.discovery.DiscoveryFragment
import com.suzhe.playdemo.component.test.DialogExampleFragment

class MainAdapter(fragmentActivity: FragmentActivity, private val count: Int) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> DialogExampleFragment.newInstance()
            2 -> DiscoveryFragment.newInstance()
            3 -> DiscoveryFragment.newInstance()
            else -> DiscoveryFragment.newInstance()
        }
    }
}