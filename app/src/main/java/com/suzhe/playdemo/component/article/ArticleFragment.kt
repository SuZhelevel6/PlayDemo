package com.suzhe.playdemo.component.article

import android.webkit.WebViewClient
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.databinding.FragmentArticleBinding


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ArticleFragment(private var content: String) :
    BaseViewModelFragment<FragmentArticleBinding>() {
    override fun initViews() {
        super.initViews()
        binding.webview.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://www.baidu.com/")
        }
    }
}