package com.suzhe.lib.base.fragment

import android.content.Intent
import android.view.View
import androidx.annotation.IdRes

abstract class BaseCommonFragment : BaseFragment() {

    fun <T : View?> findViewById(@IdRes id: Int): T {
        return requireView().findViewById(id)
    }

    protected fun extraInt(key: String?): Int {
        return requireArguments().getInt(key, -1)
    }

    protected fun startActivityExtraId(clazz: Class<*>, id: String, key: String = "id") {
        val intent = Intent(activity, clazz).apply {
            putExtra(key, id)
        }
        startActivity(intent)
    }

    protected fun startActivity(clazz: Class<*>) {
        val intent = Intent(requireActivity(), clazz)
        startActivity(intent)
    }
}
