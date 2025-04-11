package com.suzhe.playdemo.component.discovery


import android.os.Bundle
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.databinding.FragmentDiscoveryBinding
import kotlin.random.Random


class DiscoveryFragment : BaseViewModelFragment<FragmentDiscoveryBinding>() {
    override fun initDatum() {
        super.initDatum()
        binding.apply {
            textView.text = Random(1).toString()
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