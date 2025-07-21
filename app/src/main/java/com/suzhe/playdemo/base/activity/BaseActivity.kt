package com.suzhe.playdemo.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.suzhe.playdemo.AppContext.LaunchTimeTracker

/**
 * 所有Activity父类
 */
open class BaseActivity : AppCompatActivity() {
    /**
     * 找控件
     */
    protected open fun initViews() {}

    /**
     * 设置数据
     */
    protected open fun initDatum() {}

    /**
     * 设置监听器
     */
    protected open fun initListeners() {}

    /**
     * 在onCreate方法后面调用
     * @param savedInstanceState
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // 设置内容视图后添加回调，确保UI完全加载
        window.decorView.post {
            // 记录启动结束时间
            LaunchTimeTracker.recordEndTime()
            // 打印启动耗时
            LaunchTimeTracker.printLaunchTime()

            // 也可以将启动时间上报到统计系统
            reportLaunchTime(LaunchTimeTracker.getLaunchDuration())
        }
        initViews()
        initDatum()
        initListeners()
    }

    private fun reportLaunchTime(duration: Long) {
        // 这里可以添加上报逻辑，如Firebase Analytics、自建统计系统等
//        Firebase.analytics.logEvent("app_launch_time", bundleOf(
//            "duration_ms" to duration
//        ))
    }
}