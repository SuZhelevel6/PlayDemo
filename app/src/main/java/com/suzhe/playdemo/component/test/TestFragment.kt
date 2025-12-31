package com.suzhe.playdemo.component.test

import android.os.Bundle
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.databinding.FragmentTestBinding

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TestFragment : BaseViewModelFragment<FragmentTestBinding>() {


    override fun initViews() {
        super.initViews()
        binding.liquidGlassView.bind(binding.contentContainer)
    }

    companion object {
        fun newInstance(): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }
}