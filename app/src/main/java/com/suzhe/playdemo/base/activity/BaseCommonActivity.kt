package com.suzhe.playdemo.base.activity

import android.content.Intent
import android.os.Build
import com.suzhe.lib.base.activity.BaseCommonActivity as LibBaseCommonActivity
import com.suzhe.lib.common.constants.Constants

/**
 * 通用界面逻辑
 * 继承自 lib-base 的 BaseCommonActivity，扩展 app 特有功能
 */
open class BaseCommonActivity : LibBaseCommonActivity() {

    /**
     * 获取字符串类型Id
     */
    protected fun extraId(): String {
        return extraString(Constants.ID)
    }

    /**
     * 启动界面，可以传递一个字符串参数
     */
    protected fun startActivityExtraId(clazz: Class<*>, id: String) {
        val intent = Intent(this, clazz).apply {
            putExtra(Constants.ID, id)
        }
        startActivity(intent)
    }

    /**
     * 获取 data 对象
     */
    protected inline fun <reified T> extraData(): T {
        return intent.getParcelableExtra(Constants.DATA)!!
    }

    override fun setStatusBarColor(data: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = data
            window.navigationBarColor = data
        }
    }
}
