package com.suzhe.playdemo.component.brvah.header

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.QuickAdapterHelper
import com.suzhe.playdemo.base.activity.BaseTitleActivity
import com.suzhe.playdemo.component.brvah.animation.AnimationAdapter
import com.suzhe.playdemo.data.DataServer
import com.suzhe.playdemo.databinding.ActivityHeaderUseBinding

/**
 * 使用 HeaderAdapter 和 FooterAdapter:
 * 1. 需要使用 QuickAdapterHelper 类来管理 Adapter
 * 2. 使用 addBeforeAdapter 和 addAfterAdapter 方法来添加 Header 和 Footer
 * 3. 实现 HeaderAdapter 和 FooterAdapter
 */
class HeaderUseActivity : BaseTitleActivity<ActivityHeaderUseBinding>() {

    private lateinit var recyclerView: RecyclerView
    private val mAdapter: AnimationAdapter = AnimationAdapter()
    private val helper by lazy(LazyThreadSafetyMode.NONE) {
        QuickAdapterHelper.Builder(mAdapter)
            .build()
    }

    override fun initViews() {
        super.initViews()
        // 初始化 RecyclerView
        recyclerView = binding.rv
        recyclerView.adapter = helper.adapter
        // 无需设置布局管理器，因为 xml 中已经设置了
    }

    override fun initDatum() {
        super.initDatum()
        mAdapter.submitList(DataServer.getStringItems5())
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