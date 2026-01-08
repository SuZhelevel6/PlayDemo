package com.suzhe.playdemo.base.activity

import androidx.viewbinding.ViewBinding
import com.suzhe.lib.base.activity.BaseViewModelActivity as LibBaseViewModelActivity

/**
 * 通用 ViewModel Activity
 * 包括 ViewBinding，自动处理 setContentView
 * 继承自 lib-base 的 BaseViewModelActivity
 */
open class BaseViewModelActivity<VB : ViewBinding> : LibBaseViewModelActivity<VB>()