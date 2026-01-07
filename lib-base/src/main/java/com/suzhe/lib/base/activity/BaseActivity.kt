package com.suzhe.lib.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 所有 Activity 父类
 * 提供统一的生命周期钩子方法
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
     * 在 onCreate 方法后面调用
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // 首帧绘制后的回调
        window.decorView.post {
            onFirstFrameDrawn()
        }
        initViews()
        initDatum()
        initListeners()
    }

    /**
     * 首帧绘制完成回调
     * 子类可重写此方法处理启动耗时统计等逻辑
     */
    protected open fun onFirstFrameDrawn() {}
}
