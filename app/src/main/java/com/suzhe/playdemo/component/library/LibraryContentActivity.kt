package com.suzhe.playdemo.component.library

import androidx.fragment.app.Fragment
import com.suzhe.playdemo.base.activity.BaseTitleActivity
import com.suzhe.feature.brvah.BRVAHExampleFragment
import com.suzhe.feature.dialog.DialogExampleFragment
import com.suzhe.playdemo.databinding.ActivityLibraryContentBinding
import com.suzhe.lib.common.constants.Constants.FRAGMENT_ID

class LibraryContentActivity : BaseTitleActivity<ActivityLibraryContentBinding>() {

    private lateinit var fragment: Fragment

    override fun initViews() {
        super.initViews()

        // 根据传入的参数判断应该唤起哪个Fragment
        val fragmentId = intent?.extras?.getString(FRAGMENT_ID)

        // 设置ToolBar 的标题为传入的参数
        setToolBarTitle(fragmentId.toString())

        fragment = when (fragmentId) {
            "DialogX" -> DialogExampleFragment.newInstance()
            "BRVAH" -> BRVAHExampleFragment.newInstance()
            else -> DialogExampleFragment.newInstance()
        }

        // 启动这个Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    fun pageId(): String? {
        return "LibraryContent"
    }

}