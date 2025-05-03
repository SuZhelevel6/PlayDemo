package com.suzhe.playdemo.component.brvah.empty

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.QuickAdapterHelper
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.dialogs.WaitDialog
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.activity.BaseTitleActivity
import com.suzhe.playdemo.component.brvah.animation.AnimationAdapter
import com.suzhe.playdemo.data.DataServer
import com.suzhe.playdemo.databinding.ActivityEmptyViewUseBinding

class EmptyViewUseActivity : BaseTitleActivity<ActivityEmptyViewUseBinding>() {

    private var status = false

    private lateinit var recyclerView: RecyclerView
    private val mAdapter: AnimationAdapter = AnimationAdapter()
    private val helper by lazy(LazyThreadSafetyMode.NONE) {
        QuickAdapterHelper.Builder(mAdapter)
            .build()
    }

    private val errorView: View
        get() {
            val errorView = layoutInflater.inflate(R.layout.error_view, FrameLayout(this), false)
            errorView.setOnClickListener { onRefresh() }
            return errorView
        }

    override fun initViews() {
        super.initViews()
        recyclerView = binding.rv
        recyclerView.adapter = helper.adapter

        // 打开空布局功能
        mAdapter.isStateViewEnable = true

        onRefresh()
    }

    override fun initListeners() {
        super.initListeners()
        binding.btnReset.setOnClickListener {
            onRefresh()
        }
    }

    private fun onRefresh() {

        // 加载状态
        val waitDialog = WaitDialog.show("正在加载...")
            .setOnBackPressedListener {
                PopTip.show("操作进行中...")
                    .setButton("取消等待") { _, _ ->
                        WaitDialog.dismiss()
                        false
                    }
                false
            }

        // 加载完成
        recyclerView.postDelayed({

            waitDialog.doDismiss()

            if (status) { // 模拟网络错误
                // 方式二：传入View
                mAdapter.submitList(null)
                mAdapter.stateView = errorView
            } else {
                mAdapter.submitList(DataServer.getStringItems5())
            }
            status = !status
        }, 1000)
    }

}