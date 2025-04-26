package com.suzhe.playdemo.component.brvah.header

import com.chad.library.adapter4.QuickAdapterHelper
import com.suzhe.playdemo.base.activity.BaseTitleActivity
import com.suzhe.playdemo.component.brvah.animation.AnimationAdapter
import com.suzhe.playdemo.data.DataServer
import com.suzhe.playdemo.databinding.ActivityHeaderUseBinding

class HeaderUseActivity : BaseTitleActivity<ActivityHeaderUseBinding>() {

    private val mAnimationAdapter: AnimationAdapter = AnimationAdapter()

    private val helper by lazy(LazyThreadSafetyMode.NONE) {
        QuickAdapterHelper.Builder(mAnimationAdapter)
            .build()
    }

    override fun initViews() {
        super.initViews()

        binding.rv.adapter = helper.adapter

        mAnimationAdapter.submitList(DataServer.getStringItems5())
    }

    override fun initListeners() {
        super.initListeners()
        binding.btnAddHeader.apply {
            setOnClickListener {
                addHeader()
            }
        }

        binding.btnAddFooter.apply {
            setOnClickListener {
                addFooter()
            }
        }

        binding.btnRemoveHeader.apply {
            setOnClickListener {
                var targetAdapter = helper.adapter.adapters[0]
                if (targetAdapter is HeaderAdapter) {
                    helper.removeAdapter(targetAdapter)
                }
            }
        }

        binding.btnRemoveFooter.apply {
            setOnClickListener {
                var targetAdapter = helper.adapter.adapters[helper.adapter.adapters.size - 1]
                if (targetAdapter is FooterAdapter) {
                    helper.removeAdapter(targetAdapter)
                }
            }
        }
    }

    private fun addHeader() {
        helper.addBeforeAdapter(0, HeaderAdapter())
    }

    private fun addFooter() {
        helper.addAfterAdapter(FooterAdapter())
    }


}