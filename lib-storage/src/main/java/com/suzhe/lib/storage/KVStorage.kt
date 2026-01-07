package com.suzhe.lib.storage

import com.tencent.mmkv.MMKV

/**
 * KV 存储基类
 * 提供类型安全的存取方法
 */
open class KVStorage(
    private val mmkv: MMKV = StorageManager.defaultMMKV()
) {
    // region String
    protected fun getString(key: String, defaultValue: String? = null): String? {
        return mmkv.decodeString(key, defaultValue)
    }

    protected fun putString(key: String, value: String?) {
        if (value == null) {
            mmkv.removeValueForKey(key)
        } else {
            mmkv.encode(key, value)
        }
    }
    // endregion

    // region Int
    protected fun getInt(key: String, defaultValue: Int = 0): Int {
        return mmkv.decodeInt(key, defaultValue)
    }

    protected fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }
    // endregion

    // region Long
    protected fun getLong(key: String, defaultValue: Long = 0L): Long {
        return mmkv.decodeLong(key, defaultValue)
    }

    protected fun putLong(key: String, value: Long) {
        mmkv.encode(key, value)
    }
    // endregion

    // region Float
    protected fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return mmkv.decodeFloat(key, defaultValue)
    }

    protected fun putFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }
    // endregion

    // region Double
    protected fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        return mmkv.decodeDouble(key, defaultValue)
    }

    protected fun putDouble(key: String, value: Double) {
        mmkv.encode(key, value)
    }
    // endregion

    // region Boolean
    protected fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return mmkv.decodeBool(key, defaultValue)
    }

    protected fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }
    // endregion

    // region ByteArray
    protected fun getBytes(key: String, defaultValue: ByteArray? = null): ByteArray? {
        return mmkv.decodeBytes(key, defaultValue)
    }

    protected fun putBytes(key: String, value: ByteArray?) {
        if (value == null) {
            mmkv.removeValueForKey(key)
        } else {
            mmkv.encode(key, value)
        }
    }
    // endregion

    // region Utils
    protected fun contains(key: String): Boolean {
        return mmkv.containsKey(key)
    }

    protected fun remove(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun clear() {
        mmkv.clearAll()
    }
    // endregion
}
