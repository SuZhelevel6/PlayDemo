package com.suzhe.playdemo.base.activity

import androidx.viewbinding.ViewBinding
import com.suzhe.lib.base.activity.BaseTitleActivity as LibBaseTitleActivity

/**
 * 带有 ToolBar 的通用标题界面
 * 继承自 lib-base 的 BaseTitleActivity
 */
open class BaseTitleActivity<VB : ViewBinding> : LibBaseTitleActivity<VB>()