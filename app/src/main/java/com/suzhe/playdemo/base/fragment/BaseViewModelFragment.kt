package com.suzhe.playdemo.base.fragment

import androidx.viewbinding.ViewBinding
import com.suzhe.lib.base.fragment.BaseViewModelFragment as LibBaseViewModelFragment

/**
 * 带 ViewBinding 的 Fragment 基类
 * 继承自 lib-base 的 BaseViewModelFragment
 */
abstract class BaseViewModelFragment<VB : ViewBinding> : LibBaseViewModelFragment<VB>()