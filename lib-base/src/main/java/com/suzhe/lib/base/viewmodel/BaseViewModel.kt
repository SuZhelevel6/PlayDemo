package com.suzhe.lib.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * 所有 ViewModel 父类
 * 提供通用的状态管理和异常处理
 */
open class BaseViewModel : ViewModel() {

    protected val viewModel: BaseViewModel
        get() = this

    /**
     * 协程异常处理器
     */
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _exception.value = exception
    }

    /**
     * 本地提示（资源 ID）
     */
    protected val _tip = MutableLiveData<Int>()
    val tip: LiveData<Int> = _tip

    /**
     * 异常
     */
    protected val _exception = MutableLiveData<Throwable>()
    val exception: LiveData<Throwable> = _exception

    /**
     * 加载状态（显示文本，空字符串表示隐藏）
     */
    protected val _loading = MutableLiveData<String>()
    val loading: LiveData<String> = _loading

    /**
     * 关闭界面信号
     */
    protected val _finishPage = MutableLiveData<Long>()
    val finishPage: LiveData<Long> = _finishPage

    /**
     * 请求关闭界面
     */
    fun finish() {
        _finishPage.value = System.currentTimeMillis()
    }

    /**
     * 显示加载中
     */
    protected fun showLoading(message: String = "") {
        _loading.value = message
    }

    /**
     * 隐藏加载中
     */
    protected fun hideLoading() {
        _loading.value = ""
    }
}
