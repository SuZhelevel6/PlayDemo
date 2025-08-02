package com.suzhe.playdemo.proxy

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import com.blankj.utilcode.util.LogUtils

class ProxyInstrumentation(
    private val original: Instrumentation
) : Instrumentation() {

    override fun newActivity(cl: ClassLoader?, className: String?, intent: Intent?): Activity {
        // 拦截目标 Activity 的创建
        if (className?.contains("MainActivity") == true) {
            intent?.flags = (intent?.flags?.or(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                ?.or(Intent.FLAG_ACTIVITY_SINGLE_TOP) ?: intent?.flags)!!
            LogUtils.d("MainActivity Hooked")
        }
        return original.newActivity(cl, className, intent)
    }
}