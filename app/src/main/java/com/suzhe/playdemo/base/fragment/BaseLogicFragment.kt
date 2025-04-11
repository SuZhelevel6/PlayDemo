package com.suzhe.playdemo.base.fragment

import com.suzhe.playdemo.base.activity.BaseLogicActivity

abstract class BaseLogicFragment : BaseCommonFragment() {
    /**
     * 获取界面方法
     *
     * @return
     */
    protected val hostActivity: BaseLogicActivity
        protected get() = requireActivity() as BaseLogicActivity


    open fun onTip(data: Int) {
        hostActivity.onTip(data)
        onError()
    }

    open fun onException(data: Throwable) {
        hostActivity.onException(data)
        onError()
    }

    open fun onError() {

    }

    //region 统计
    /**
     * 当界面显示了
     */
    override fun onResume() {
        super.onResume()
    }

    /**
     * 当页面暂停了
     * 例如：弹窗；或者切换到后台
     */
    override fun onPause() {
        super.onPause()
    }

    /**
     * 返回页面标识
     * @return
     */
    protected open fun pageId(): String? {
        return null
    }
    //endregion
}