package com.suzhe.playdemo.base.activity

import android.net.http.HttpException
import com.blankj.utilcode.util.NetworkUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.suzhe.playdemo.AppContext
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.model.BaseViewModel
import com.suzhe.playdemo.ext.longToast
import com.suzhe.playdemo.ext.shortToast
import com.suzhe.playdemo.utils.SuperDarkUtil
import org.apache.commons.lang3.StringUtils
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * 本项目的通用逻辑，例如：背景颜色等
 */
open class BaseLogicActivity : BaseCommonActivity() {
//    private var loadingDialog: WeakReference<IOSLoadingDialog>? = null

    /**
     * 获取界面方法
     *
     * @return
     */
    protected val hostActivity: BaseLogicActivity
        protected get() = this

    override fun initViews() {
        super.initViews()
        if (SuperDarkUtil.isDark(this)) {
            //状态栏文字白色
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        } else {
            //状态栏文字黑色
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }
    }

    /**
     * 初始化通用ViewModel逻辑
     */
    protected fun initViewModel(viewModel: BaseViewModel) {
        //关闭界面
        viewModel.finishPage.observe(this) {
            finish()
        }

        //本地提示
        viewModel.tip.observe(this) {
            onTip(it)
        }

        //异常
        viewModel.exception.observe(this) {
            onException(it)
        }

        //网络响应业务失败
//        viewModel.response.observe(this) {
//            onResponse(it)
//        }

        //加载提示
        viewModel.loading.observe(this) {
            if (StringUtils.isNotBlank(it)) showLoading(it) else hideLoading()
        }
    }

    open fun onTip(data: Int) {
        data.shortToast()
        onError()
    }

//    open fun onResponse(data: BaseResponse) {
//        when (data.status) {
//            401 -> {
//                R.string.error_not_auth.longToast()
//                AppContext.instance.logout()
//            }
//
//            403 -> {
//                R.string.error_not_permission.longToast()
//            }
//
//            404 -> {
//                R.string.error_not_found.longToast()
//            }
//        }
//        (data.message ?: getString(R.string.error_unknown)).longToast()
//        onError()
//    }

    open fun onException(data: Throwable) {
        if (NetworkUtils.isAvailableByPing()) {
            //有网络
            R.string.error_load.longToast()
        } else {
            //提示，你的网络好像不太好
            R.string.error_network_not_connect.longToast()
        }

        onError()
    }

    open fun onError() {

    }

    /**
     * 只要用户登录了，才会执行代码块
     *
     * @param data
     */
//    fun loginAfter(data: Runnable) {
//        if (PreferenceUtil.isLogin()) {
//            //已经登录了
//            data.run()
//        } else {
//            hostActivity.toLogin()
//        }
//    }

//    fun toLogin() {
//        startActivity(LoginHomeActivity::class.java)
//    }

    //region 加载提示
    /**
     * 显示加载对话框
     */
    open fun showLoading(data: Int) {
        showLoading(getString(data))
    }

    /**
     * 显示加载对话框
     */
    open fun showLoading(data: String = getString(R.string.loading)) {
        Timber.d("showLoading: " + data)

    }

    /**
     * 隐藏加载对话框
     */
    fun hideLoading() {

    }
    //endregion

    /**
     * 返回页面标识
     * @return
     */
    protected open fun pageId(): String? {
        return null
    }
    //endregion
}