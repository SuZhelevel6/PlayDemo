package com.suzhe.lib.base.activity

/**
 * 通用逻辑 Activity
 */
open class BaseLogicActivity : BaseCommonActivity() {

    protected val hostActivity: BaseLogicActivity
        get() = this

    open fun onError() {}

    open fun showLoading(data: Int) {
        showLoading(getString(data))
    }

    open fun showLoading(data: String = "Loading...") {}

    fun hideLoading() {}
}
