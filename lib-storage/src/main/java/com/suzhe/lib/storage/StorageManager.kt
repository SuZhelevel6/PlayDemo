package com.suzhe.lib.storage

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * 存储管理器
 * 封装 MMKV 的初始化和基础操作
 */
object StorageManager {

    private var isInitialized = false

    /**
     * 初始化存储模块
     * 应在 Application.onCreate() 中调用
     *
     * @param context Application Context
     * @return MMKV 根目录路径
     */
    fun init(context: Context): String {
        if (isInitialized) {
            return MMKV.getRootDir() ?: ""
        }
        val rootDir = MMKV.initialize(context)
        isInitialized = true
        return rootDir
    }

    /**
     * 获取默认的 MMKV 实例
     */
    fun defaultMMKV(): MMKV {
        checkInitialized()
        return MMKV.defaultMMKV()!!
    }

    /**
     * 获取指定 ID 的 MMKV 实例
     *
     * @param mmapID 实例 ID
     */
    fun mmkvWithID(mmapID: String): MMKV {
        checkInitialized()
        return MMKV.mmkvWithID(mmapID)!!
    }

    /**
     * 获取多进程模式的 MMKV 实例
     *
     * @param mmapID 实例 ID
     */
    fun mmkvWithIDMultiProcess(mmapID: String): MMKV {
        checkInitialized()
        return MMKV.mmkvWithID(mmapID, MMKV.MULTI_PROCESS_MODE)!!
    }

    private fun checkInitialized() {
        if (!isInitialized) {
            throw IllegalStateException("StorageManager not initialized. Call init() first.")
        }
    }
}
