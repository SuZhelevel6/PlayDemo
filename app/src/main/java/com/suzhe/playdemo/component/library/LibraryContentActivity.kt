package com.suzhe.playdemo.component.library

import androidx.fragment.app.Fragment
import com.suzhe.playdemo.base.activity.BaseViewModelActivity
import com.suzhe.playdemo.component.dialogX.DialogExampleFragment
import com.suzhe.playdemo.databinding.ActivityLibraryContentBinding
import com.suzhe.playdemo.utils.Constants.FRAGMENT_ID

class LibraryContentActivity : BaseViewModelActivity<ActivityLibraryContentBinding>() {

    private lateinit var fragment: Fragment

    override fun initViews() {
        super.initViews()

        // 根据传入的参数判断应该唤起哪个Fragment
        val fragmentId = intent?.extras?.getString(FRAGMENT_ID)
        fragment = when (fragmentId) {
            "DialogExampleFragment" -> DialogExampleFragment.newInstance()
            else -> DialogExampleFragment.newInstance()
        }

        // 启动这个Fragment
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    override fun pageId(): String? {
        return "LibraryContent"
    }

}