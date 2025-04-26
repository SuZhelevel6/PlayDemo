package com.suzhe.playdemo.component.brvah.animation

import com.chad.library.adapter4.BaseQuickAdapter
import com.suzhe.playdemo.base.activity.BaseTitleActivity
import com.suzhe.playdemo.data.DataServer
import com.suzhe.playdemo.databinding.ActivityAnimationUseBinding

class AnimationUseActivity : BaseTitleActivity<ActivityAnimationUseBinding>() {

    private val mAnimationAdapter: AnimationAdapter = AnimationAdapter().apply {
        // 打开 Adapter 的动画
        animationEnable = true
        // 是否是首次显示时候加载动画
        isAnimationFirstOnly = false
    }

    override fun initViews() {
        super.initViews()

        binding.rv.adapter = mAnimationAdapter

        mAnimationAdapter.submitList(DataServer.getStringItems100())

        initMenu()

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
            "SlideInRight"
        )
        binding.spinner.setOnItemSelectedListener { _, position, _, _ ->
            when (position) {
                0 -> mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.AlphaIn)
                1 -> mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.ScaleIn)
                2 -> mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInBottom)
                3 -> mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInLeft)
                4 -> mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInRight)
                else -> {}
            }
            mAnimationAdapter.notifyDataSetChanged()
        }
    }
}