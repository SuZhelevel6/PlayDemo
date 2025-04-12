package com.suzhe.playdemo.component.discovery


import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.databinding.FragmentDiscoveryBinding


class DiscoveryFragment : BaseViewModelFragment<FragmentDiscoveryBinding>() {

    private val tabTitles =
        arrayOf(
            "零碎文章",
            "安卓系统",
            "UI界面"
        )

    override fun initDatum() {
        super.initDatum()
        binding.apply {
            pager.adapter = DiscoveryAdapter(hostActivity, tabTitles)
            for (i in tabTitles.indices) {
                tab.addTab(tab.newTab().setText(tabTitles[i]))
            }
            tab.selectTab(tab.getTabAt(0))

            // 绑定TabLayout和ViewPager2
            pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tab.selectTab(tab.getTabAt(position))
                }
            })

            tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val index = tab.position
                    if (pager.currentItem != index) {
                        pager.setCurrentItem(index, false)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
    }

    override fun initListeners() {
        super.initListeners()

    }

    override fun pageId(): String? {
        return "Discovery"
    }

    companion object {
        fun newInstance(): DiscoveryFragment {
            val args = Bundle()

            val fragment = DiscoveryFragment()
            fragment.arguments = args
            return fragment
        }
    }

}