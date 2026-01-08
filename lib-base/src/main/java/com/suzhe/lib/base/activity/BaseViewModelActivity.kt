package com.suzhe.lib.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.suzhe.lib.base.util.ReflectUtil

/**
 * 通用 ViewModel Activity
 * 包括 ViewBinding，自动处理 setContentView
 */
open class BaseViewModelActivity<VB : ViewBinding> : BaseLogicActivity() {
    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReflectUtil.newViewBinding(layoutInflater, javaClass)
        setContentView(binding.root)
    }
}
