package com.suzhe.playdemo

import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.color.DynamicColors
import com.kongzue.dialogx.DialogX
import com.suzhe.playdemo.component.main.MainActivity
import com.suzhe.playdemo.proxy.ProxyInstrumentation
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xcrash.TombstoneManager


/**
 * 全局Application
 */
class AppContext : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    //region 生命周期方法
    /**
     * 附加应用程序的上下文。
     * 用于初始化 XCrash 崩溃监控。
     * @param base 应用的基础 Context
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 记录启动开始时间
        LaunchTimeTracker.recordStartTime()
//        XCrash.init(this)// 初始化 XCrash 进行崩溃监控
        hookInstrumentation()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // 主线程必要初始化
        initCoreComponents()

        // 延迟非关键初始化
        CoroutineScope(Dispatchers.Default).launch {
            initNonCriticalComponents()
        }

        // 延迟到首帧绘制后
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity is MainActivity) {
                    CoroutineScope(Dispatchers.IO).launch {
                        initAfterFirstFrame()
                    }
                    unregisterActivityLifecycleCallbacks(this)
                }
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {

            }

            override fun onActivityStarted(p0: Activity) {

            }

            override fun onActivityResumed(p0: Activity) {

            }

            override fun onActivityPaused(p0: Activity) {

            }

            override fun onActivityStopped(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(
                p0: Activity,
                p1: Bundle
            ) {

            }

            override fun onActivityDestroyed(p0: Activity) {

            }
        })

    }


    private fun hookInstrumentation() {
        try {
            // 获取 ActivityThread 实例
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread")
            val activityThread = currentActivityThreadMethod.invoke(null)

            // 获取原始 Instrumentation
            val instrumentationField = activityThreadClass.getDeclaredField("mInstrumentation")
            instrumentationField.isAccessible = true
            val originalInstrumentation =
                instrumentationField.get(activityThread) as Instrumentation

            // 替换为代理 Instrumentation
            instrumentationField.set(activityThread, ProxyInstrumentation(originalInstrumentation))
        } catch (e: Exception) {
            LogUtils.d("Failed to hook Instrumentation", e)
        }
    }

    private fun initCoreComponents() {
        // 核心组件初始化（必须立即执行的）

        // 初始化 LogUtils
        LogUtils.getConfig().setGlobalTag(AppUtils.getAppPackageName())

        // 初始化 MMKV 键值对存储框架
        initMMKV()

        // 开启动态主题
        DynamicColors.applyToActivitiesIfAvailable(instance)

        // 初始化腾讯Bugly崩溃日志
//        CrashReport.initCrashReport(applicationContext, "056610e788", true)
    }

    private fun initNonCriticalComponents() {
        // 非关键组件初始化（如：分析统计、广告 SDK）

        // 初始化应用崩溃日志
        processCrashLogs()
    }

    private fun initAfterFirstFrame() {
        // 首帧绘制后的初始化（如：后台服务）

        // 初始化DialogX框架
        DialogX.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel() // 应用终止时取消协程作用域
    }
    //endregion

    //region 处理应用崩溃日志
    private fun processCrashLogs() {
        applicationScope.launch(Dispatchers.IO) {
            delay(5000) // 延迟 5 秒执行
            for (file in TombstoneManager.getAllTombstones()) {
                try {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
                        put(MediaStore.MediaColumns.MIME_TYPE, "txt")
                    }
                    val uri = contentResolver.insert(
                        MediaStore.Files.getContentUri("external"),
                        contentValues
                    ) ?: continue
                    contentResolver.openOutputStream(uri)?.use { out ->
                        file.inputStream().use { ins ->
                            ins.copyTo(out) // 复制崩溃日志文件内容
                            LogUtils.d("Copy file to $out")
                        }
                    }
                    file.delete() // 删除原始日志文件
                } catch (ignore: Throwable) {
                    // 忽略异常，防止崩溃
                }
            }
        }
    }
    //endregion

    /**
     * 初始化 腾讯开源的高性能keyValue存储，用来替代系统的SharedPreferences
     */
    private fun initMMKV() {
        val rootDir = MMKV.initialize(this)
        LogUtils.d("initMMKV %s", rootDir)
    }

    companion object {
        /**
         * 这里是为了以后能方便的调用 PlayDemoApplication 里面的一些方法，如 initOCR() 等
         */
        lateinit var instance: AppContext
    }

    object LaunchTimeTracker {
        private var startTime: Long = 0L
        private var endTime: Long = 0L
        private var isStarted = false
        private var isEnded = false

        /**
         * 在 Application.attachBaseContext() 中调用
         */
        fun recordStartTime() {
            if (isStarted) return
            startTime = System.currentTimeMillis()
            isStarted = true
        }

        /**
         * 在用户界面完全可操作时调用
         */
        fun recordEndTime() {
            if (isEnded || !isStarted) return
            endTime = System.currentTimeMillis()
            isEnded = true
        }

        /**
         * 获取启动耗时（毫秒）
         */
        fun getLaunchDuration(): Long {
            return if (isStarted && isEnded) {
                endTime - startTime
            } else {
                -1L
            }
        }

        /**
         * 打印启动耗时日志
         */
        fun printLaunchTime() {
            val duration = getLaunchDuration()
            if (duration >= 0) {
                Log.d("LaunchTime", "App launch took: $duration ms")
            } else {
                Log.e("LaunchTime", "Launch time recording incomplete")
            }
        }
    }

}