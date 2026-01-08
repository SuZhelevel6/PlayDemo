package com.suzhe.playdemo.base.activity

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.suzhe.lib.base.activity.BaseLogicActivity as LibBaseLogicActivity
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.model.BaseViewModel
import com.suzhe.playdemo.ext.longToast
import com.suzhe.playdemo.ext.shortToast
import com.suzhe.playdemo.utils.SuperDarkUtil
import org.apache.commons.lang3.StringUtils

/**
 * 本项目的通用逻辑，例如：背景颜色等
 * 继承自 lib-base 的 BaseLogicActivity
 */
open class BaseLogicActivity : LibBaseLogicActivity() {

    override fun initViews() {
        super.initViews()
        if (SuperDarkUtil.isDark(this)) {
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }
    }

    /**
     * 初始化通用 ViewModel 逻辑
     */
    protected fun initViewModel(viewModel: BaseViewModel) {
        viewModel.finishPage.observe(this) {
            finish()
        }

        viewModel.tip.observe(this) {
            onTip(it)
        }

        viewModel.exception.observe(this) {
            onException(it)
        }

        viewModel.loading.observe(this) {
            if (StringUtils.isNotBlank(it)) showLoading(it) else hideLoading()
        }
    }

    open fun onTip(data: Int) {
        data.shortToast()
        onError()
    }

    open fun onException(data: Throwable) {
        if (NetworkUtils.isAvailableByPing()) {
            R.string.error_load.longToast()
        } else {
            R.string.error_network_not_connect.longToast()
        }
        onError()
    }

    override fun showLoading(data: String) {
        LogUtils.d("showLoading: $data")
    }

    protected open fun pageId(): String? {
        return null
    }
}
