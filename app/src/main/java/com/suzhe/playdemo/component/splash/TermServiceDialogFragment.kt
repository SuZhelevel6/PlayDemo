package com.suzhe.playdemo.component.splash

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ScreenUtils
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.fragment.BaseViewModelDialogFragment
import com.suzhe.playdemo.databinding.FragmentDialogTermServiceBinding

class TermServiceDialogFragment : BaseViewModelDialogFragment<FragmentDialogTermServiceBinding>() {
    private lateinit var onAgreementClickListener: View.OnClickListener

    override fun initViews() {
        super.initViews()
        //点击弹窗外边不能关闭
        isCancelable = false

        binding.content.apply {
            // 设置后才可以点击
            movementMethod = LinkMovementMethod.getInstance()
            // 链接的颜色
            setLinkTextColor(getColor(requireContext(), R.color.link))
        }

    }

    override fun initDatum() {
        super.initDatum()
        val content = Html.fromHtml(getString(R.string.term_service_privacy_content))
        binding.content.text = content
    }

    override fun initListeners() {
        super.initListeners()
        //同意按钮点击
        binding.primary.setOnClickListener {
            dismiss()
            onAgreementClickListener.onClick(it)
        }

        //不同意按钮点击
        binding.disagree.setOnClickListener {
            dismiss()
            AppUtils.exitApp()
        }
    }

    override fun onResume() {
        super.onResume()
        //修改宽度，默认比AlertDialog.Builder显示对话框宽度窄，看着不好看
        //参考：https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ((ScreenUtils.getScreenWidth() * 0.9).toInt())
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    companion object {
        fun show(fm: FragmentManager, listener: View.OnClickListener) {
            TermServiceDialogFragment().apply {
                onAgreementClickListener = listener
            }.show(fm, "TermServiceDialogFragment")
        }
    }
}