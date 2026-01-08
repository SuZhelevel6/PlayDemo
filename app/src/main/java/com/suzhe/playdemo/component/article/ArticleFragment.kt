package com.suzhe.playdemo.component.article

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.databinding.FragmentArticleBinding


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ArticleFragment : BaseViewModelFragment<FragmentArticleBinding>() {

    private var content: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString(ARG_CONTENT) ?: ""
    }

    companion object {
        private const val ARG_CONTENT = "arg_content"

        fun newInstance(content: String): ArticleFragment {
            return ArticleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CONTENT, content)
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initDatum() {
        super.initDatum()
        binding.webview.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(false)
            allowFileAccess = true
            javaScriptCanOpenWindowsAutomatically = true
            loadsImagesAutomatically = true
            defaultTextEncodingName = "utf-8"
        }

        binding.webview.webViewClient = object : WebViewClient() {
            // 1. 拦截 URL 加载（例如：外部链接用浏览器打开）
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (url.startsWith("http://") || url.startsWith("https://")) {
                    // 默认在 WebView 内部加载网页
                    false
                } else {
                    // 其他链接（如 tel:、mailto:）尝试用外部应用打开
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        true
                    } catch (e: ActivityNotFoundException) {
                        false
                    }
                }
            }

            // 2. 页面加载完成回调（例如：隐藏进度条）
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // 可以在这里执行加载完成后的逻辑，比如：
                // progressBar.visibility = View.GONE
            }

            // 3. 处理 SSL 错误（可选，仅用于测试环境）
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                // 正式环境应提示用户风险，测试环境可忽略证书错误
                handler?.proceed() // 慎用！仅限调试
            }
        }

        binding.webview.loadUrl("https://github.com/SuZhelevel6/PlayDemo")
    }
}