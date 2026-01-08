package com.suzhe.lib.base.activity

import android.content.Intent
import android.os.Build

/**
 * 通用界面逻辑
 */
open class BaseCommonActivity : BaseActivity() {
    /**
     * 启动界面
     */
    protected fun startActivity(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    /**
     * 启动界面并关闭当前界面
     */
    fun startActivityAfterFinishThis(clazz: Class<*>) {
        startActivity(clazz)
        finish()
    }

    /**
     * 启动界面，可以传递一个字符串参数
     */
    protected fun startActivityExtraId(clazz: Class<*>, id: String, key: String = "id") {
        val intent = Intent(this, clazz).apply {
            putExtra(key, id)
        }
        startActivity(intent)
    }

    /**
     * 获取字符串
     */
    protected fun extraString(key: String): String {
        return extraStringOrNull(key)!!
    }

    protected fun extraStringOrNull(key: String): String? {
        return intent.getStringExtra(key)
    }

    protected fun extraInt(key: String): Int {
        return intent.getIntExtra(key, -1)
    }

    /**
     * 设置状态栏颜色
     */
    protected open fun setStatusBarColor(data: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = data
            window.navigationBarColor = data
        }
    }
}
