package com.suzhe.lib.crash

import android.content.Context
import xcrash.ICrashCallback
import xcrash.TombstoneManager
import xcrash.XCrash
import java.io.File

/**
 * 崩溃监控管理器
 * 封装 XCrash 的初始化和崩溃日志处理
 */
object CrashManager {

    private var isInitialized = false

    /**
     * 初始化崩溃监控
     * 应在 Application.attachBaseContext() 中调用
     *
     * @param context Application Context
     * @param callback 崩溃回调（可选）
     */
    fun init(context: Context, callback: CrashCallback? = null) {
        if (isInitialized) return

        val params = XCrash.InitParameters()

        // 设置崩溃回调
        callback?.let { cb ->
            val javaCallback = ICrashCallback { logPath, emergency ->
                cb.onJavaCrash(logPath, emergency)
            }
            val nativeCallback = ICrashCallback { logPath, emergency ->
                cb.onNativeCrash(logPath, emergency)
            }
            val anrCallback = ICrashCallback { logPath, emergency ->
                cb.onAnr(logPath, emergency)
            }
            params.setJavaCallback(javaCallback)
            params.setNativeCallback(nativeCallback)
            params.setAnrCallback(anrCallback)
        }

        XCrash.init(context, params)
        isInitialized = true
    }

    /**
     * 简单初始化（无回调）
     */
    fun init(context: Context) {
        init(context, null)
    }

    /**
     * 获取所有崩溃日志文件
     */
    fun getAllTombstones(): Array<File> {
        return TombstoneManager.getAllTombstones()
    }

    /**
     * 删除所有崩溃日志
     */
    fun deleteAllTombstones() {
        getAllTombstones().forEach { it.delete() }
    }

    /**
     * 遍历处理崩溃日志
     *
     * @param handler 日志处理器
     */
    fun processTombstones(handler: (File) -> Unit) {
        getAllTombstones().forEach { file ->
            try {
                handler(file)
            } catch (e: Exception) {
                // 忽略处理异常
            }
        }
    }

    /**
     * 崩溃回调接口
     */
    interface CrashCallback {
        /**
         * Java 崩溃回调
         */
        fun onJavaCrash(logPath: String?, emergency: String?)

        /**
         * Native 崩溃回调
         */
        fun onNativeCrash(logPath: String?, emergency: String?)

        /**
         * ANR 回调
         */
        fun onAnr(logPath: String?, emergency: String?)
    }

    /**
     * 空实现的崩溃回调
     */
    open class SimpleCrashCallback : CrashCallback {
        override fun onJavaCrash(logPath: String?, emergency: String?) {}
        override fun onNativeCrash(logPath: String?, emergency: String?) {}
        override fun onAnr(logPath: String?, emergency: String?) {}
    }
}
