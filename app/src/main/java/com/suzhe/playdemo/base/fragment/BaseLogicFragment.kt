package com.suzhe.playdemo.base.fragment

import com.suzhe.lib.base.fragment.BaseLogicFragment as LibBaseLogicFragment
import com.suzhe.playdemo.base.activity.BaseLogicActivity

/**
 * 继承自 lib-base 的 BaseLogicFragment
 */
abstract class BaseLogicFragment : LibBaseLogicFragment() {

    /**
     * 获取 hostActivity 并转换为 app 模块的 BaseLogicActivity
     * 用于访问 app 模块特有的方法（如 onTip, onException）
     */
    protected val appHostActivity: BaseLogicActivity
        get() = requireActivity() as BaseLogicActivity

    open fun onTip(data: Int) {
        appHostActivity.onTip(data)
        onError()
    }

    open fun onException(data: Throwable) {
        appHostActivity.onException(data)
        onError()
    }

    protected open fun pageId(): String? {
        return null
    }
}
