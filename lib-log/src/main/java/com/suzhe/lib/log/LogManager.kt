package com.suzhe.lib.log

import com.blankj.utilcode.util.LogUtils

/**
 * 日志管理器
 * 封装 LogUtils 的初始化和配置
 */
object LogManager {

    private var isInitialized = false

    /**
     * 初始化日志模块
     *
     * @param globalTag 全局日志标签
     * @param isLogEnabled 是否启用日志（Release 版本建议关闭）
     * @param isSaveLogToFile 是否保存日志到文件
     */
    fun init(
        globalTag: String,
        isLogEnabled: Boolean = true,
        isSaveLogToFile: Boolean = false
    ) {
        if (isInitialized) return

        LogUtils.getConfig()
            .setGlobalTag(globalTag)
            .setLogSwitch(isLogEnabled)
            .setLog2FileSwitch(isSaveLogToFile)
            .setBorderSwitch(false)
            .setStackDeep(1)

        isInitialized = true
    }

    /**
     * 设置日志开关
     */
    fun setLogEnabled(enabled: Boolean) {
        LogUtils.getConfig().setLogSwitch(enabled)
    }

    /**
     * 设置文件日志开关
     */
    fun setLog2FileEnabled(enabled: Boolean) {
        LogUtils.getConfig().setLog2FileSwitch(enabled)
    }
}

/**
 * 日志工具类
 * 提供简洁的日志方法
 */
object L {

    fun v(vararg contents: Any?) {
        LogUtils.v(*contents)
    }

    fun d(vararg contents: Any?) {
        LogUtils.d(*contents)
    }

    fun i(vararg contents: Any?) {
        LogUtils.i(*contents)
    }

    fun w(vararg contents: Any?) {
        LogUtils.w(*contents)
    }

    fun e(vararg contents: Any?) {
        LogUtils.e(*contents)
    }

    fun e(e: Throwable?, vararg contents: Any?) {
        if (e != null) {
            LogUtils.e(e, *contents)
        } else {
            LogUtils.e(*contents)
        }
    }

    fun json(json: String?) {
        LogUtils.json(json)
    }

    fun xml(xml: String?) {
        LogUtils.xml(xml)
    }

    /**
     * 带标签的日志
     */
    fun tag(tag: String): TagLogger {
        return TagLogger(tag)
    }

    class TagLogger(private val tag: String) {
        fun v(vararg contents: Any?) = LogUtils.vTag(tag, *contents)
        fun d(vararg contents: Any?) = LogUtils.dTag(tag, *contents)
        fun i(vararg contents: Any?) = LogUtils.iTag(tag, *contents)
        fun w(vararg contents: Any?) = LogUtils.wTag(tag, *contents)
        fun e(vararg contents: Any?) = LogUtils.eTag(tag, *contents)
    }
}
