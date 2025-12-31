package com.suzhe.playdemo.component.test

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.suzhe.playdemo.BuildInfo
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.databinding.FragmentTestBinding

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TestFragment : BaseViewModelFragment<FragmentTestBinding>() {

    override fun initViews() {
        super.initViews()
        binding.liquidGlassView.bind(binding.contentContainer)

        // 显示构建信息
        showBuildInfo()
    }

    private fun showBuildInfo() {
        val buildInfoText = """
            |═══ 构建信息 ═══
            |分支: ${BuildInfo.GIT_BRANCH}
            |提交: ${BuildInfo.GIT_HASH}
            |干净: ${if (BuildInfo.GIT_CLEAN) "✓" else "✗ (有未提交修改)"}
            |时间: ${BuildInfo.BUILD_TIME}
            |用户: ${BuildInfo.BUILD_USER}
            |═══════════════
            |${BuildInfo.BUILD_DESC}
        """.trimMargin()

        // 在界面上显示
        binding.tvBuildInfo.text = buildInfoText

        // 同时输出到日志
        LogUtils.d(buildInfoText)
    }

    companion object {
        fun newInstance(): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }
}