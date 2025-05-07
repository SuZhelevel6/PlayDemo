package com.suzhe.playdemo.component.brvah.animation

import ImmediateIndexScroller
import SmoothIndexScroller
import SnapPreference
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.suzhe.playdemo.base.activity.BaseTitleActivity
import com.suzhe.playdemo.data.DataServer
import com.suzhe.playdemo.databinding.ActivityAnimationUseBinding

class AnimationUseActivity : BaseTitleActivity<ActivityAnimationUseBinding>() {

    private lateinit var recyclerView: RecyclerView
    private val mAdapter: AnimationAdapter = AnimationAdapter().apply {
        // 打开 Adapter 的动画
        animationEnable = true
        // 是否是首次显示时候加载动画
        isAnimationFirstOnly = false
    }

    override fun initViews() {
        super.initViews()
        // 初始化 RecyclerView
        initRecyclerView()
        // 初始化下拉菜单
        initMenu()
    }

    override fun initDatum() {
        super.initDatum()
        // 设置数据
        mAdapter.submitList(DataServer.getStringItems100())
    }

    override fun initListeners() {
        super.initListeners()
        binding.btnTest.setOnClickListener {
            // 平滑滚动到指定位置并居中对齐
            val smoothIndexScroller = SmoothIndexScroller(recyclerView)
            smoothIndexScroller.scrollToPosition(20, SnapPreference.SNAP_TO_CENTER)

            // 即时滚动到指定位置并居中对齐
            val immediateIndexScroller = ImmediateIndexScroller(recyclerView)
            immediateIndexScroller.scrollToPosition(20, SnapPreference.SNAP_TO_CENTER)
        }

    }

    fun initRecyclerView() {
        recyclerView = binding.rv
        recyclerView.adapter = mAdapter
        // 无需设置布局管理器，因为 xml 中已经设置了
    }

    /**
     * Init menu
     * 初始化下拉菜单
     */
    private fun initMenu() {
        binding.spinner.setItems(
            "AlphaIn",
            "ScaleIn",
            "SlideInBottom",
            "SlideInLeft",
            "SlideInRight",
            "Custom1",
            "Custom2"
        )
        binding.spinner.setOnItemSelectedListener { _, position, _, _ ->
            when (position) {
                0 -> mAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.AlphaIn)
                1 -> mAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.ScaleIn)
                2 -> mAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInBottom)
                3 -> mAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInLeft)
                4 -> mAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInRight)
                5 -> mAdapter.itemAnimation = CustomAnimation1()
                6 -> mAdapter.itemAnimation = CustomAnimation2()
                else -> {}
            }
            mAdapter.notifyDataSetChanged()
        }
    }
}