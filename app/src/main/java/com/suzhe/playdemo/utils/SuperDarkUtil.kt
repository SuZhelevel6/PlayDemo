package com.suzhe.playdemo.utils

import android.content.Context
import android.content.res.Configuration

/**
 * 深色模型工具类，用在BaseLogicActivity中动态改变状态栏文字白色
 */
object SuperDarkUtil {
    /**
     * 是否是深色模型
     */
    fun isDark(context: Context): Boolean {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}