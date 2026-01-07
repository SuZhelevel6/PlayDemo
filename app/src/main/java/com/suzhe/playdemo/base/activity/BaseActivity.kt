package com.suzhe.playdemo.base.activity

import com.suzhe.lib.base.activity.BaseActivity as LibBaseActivity
import com.suzhe.playdemo.AppContext.LaunchTimeTracker

/**
 * 所有 Activity 父类
 * 继承自 lib-base 模块的 BaseActivity，添加项目特定的启动耗时统计
 */
open class BaseActivity : LibBaseActivity() {

    override fun onFirstFrameDrawn() {
        super.onFirstFrameDrawn()
        // 记录启动结束时间
        LaunchTimeTracker.recordEndTime()
        // 打印启动耗时
        LaunchTimeTracker.printLaunchTime()
        // 上报启动时间
        reportLaunchTime(LaunchTimeTracker.getLaunchDuration())
    }

    private fun reportLaunchTime(duration: Long) {
        // 这里可以添加上报逻辑，如 Firebase Analytics、自建统计系统等
    }
}