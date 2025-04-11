package com.suzhe.playdemo.utils

import com.tencent.mmkv.MMKV

/**
 * 偏好设置工具类
 */
object PreferenceUtil {
    val p: MMKV by lazy {
        MMKV.defaultMMKV()!!
    }

    private const val ACCEPT_TERM = "Accept_Term"

    fun isAcceptTermsServiceAgreement(): Boolean {
        return getBoolean(ACCEPT_TERM, false)
    }

    fun setAcceptTermsServiceAgreement() {
        return putBoolean(ACCEPT_TERM, true)
    }

    //region 辅助方法
    private fun getString(key: String): String? {
        return p.decodeString(key, null)
    }

    private fun putString(key: String, value: String) {
        p.edit().putString(key, value).apply()
    }

    private fun delete(data: String) {
        p.edit().remove(data).commit()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return p.getBoolean(key, defaultValue)
    }

    private fun putBoolean(key: String, value: Boolean) {
        p.edit().putBoolean(key, value).apply()
    }
    //endregion
}