package com.suzhe.lib.base.fragment

import com.suzhe.lib.base.activity.BaseLogicActivity

abstract class BaseLogicFragment : BaseCommonFragment() {

    protected val hostActivity: BaseLogicActivity
        get() = requireActivity() as BaseLogicActivity

    open fun onError() {}
}
