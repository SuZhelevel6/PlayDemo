package com.suzhe.playdemo.base.fragment

import android.content.Intent
import com.suzhe.lib.base.fragment.BaseCommonFragment as LibBaseCommonFragment
import com.suzhe.lib.common.constants.Constants

/**
 * 继承自 lib-base 的 BaseCommonFragment
 */
abstract class BaseCommonFragment : LibBaseCommonFragment() {

    /**
     * 启动界面，可以传递一个字符串参数
     */
    protected fun startActivityExtraId(clazz: Class<*>, id: String) {
        val intent = Intent(activity, clazz).apply {
            putExtra(Constants.ID, id)
        }
        startActivity(intent)
    }
}
