package com.suzhe.playdemo.base.activity


import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.suzhe.playdemo.R
import com.suzhe.playdemo.databinding.ActivityWebBinding
import com.suzhe.playdemo.utils.Constants

/**
 * 通用WebView界面
 */
class WebActivity : BaseTitleActivity<ActivityWebBinding>() {
    private var title: String? = null

    override fun initViews() {
        super.initViews()
        //获取webview设置
        val webSettings: WebSettings = binding.web.getSettings()

        //运行访问文件
        webSettings.allowFileAccess = true

        //支持多窗口
        webSettings.setSupportMultipleWindows(true)

        //开启js支持
        webSettings.javaScriptEnabled = true

        //显示图片
        webSettings.blockNetworkImage = false

        //运行显示手机中的网页
        webSettings.allowUniversalAccessFromFileURLs = true

        //运行文件访问
        webSettings.allowFileAccessFromFileURLs = true

        //运行dom存储
        webSettings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //运行混合类型
            //也就说支持网页中有http/https
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        binding.web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url != null && url.startsWith("http://") && url.startsWith("https://")) {
                    //只加载网址协议
                    view.loadUrl(url)
                    return true
                }
                return false
            }
        }
        binding.web.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                if (TextUtils.isEmpty(this@WebActivity.title)) {
                    //如果没有不传递标题，就用网址标题
                    setTitle(title)
                }
            }

            /**
             * 进度改变了
             *
             * @param view
             * @param newProgress
             */
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progress.setWebProgress(newProgress)
            }

            /**
             * html里面点击全屏调用
             *
             * @param view
             * @param callback
             */
            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                super.onShowCustomView(view, callback)
                switchFullScreen()
                binding.fullContainer.visibility = View.VISIBLE
                binding.fullContainer.addView(view)
            }

            /**
             * html退出全屏调用
             */
            override fun onHideCustomView() {
                super.onHideCustomView()
                switchFullScreen()
                binding.fullContainer.setVisibility(View.GONE)
                binding.fullContainer.removeAllViews()
            }
        }

        //进度条高亮颜色
        binding.progress.setColor(getColor(R.color.light_blue_900))
    }

    protected override fun initDatum() {
        super.initDatum()

        //获取传递的数据
        title = extraStringOrNull(Constants.TITLE_KEY)
        val url = getIntent().getStringExtra(Constants.URL)
        val data = getIntent().getStringExtra(Constants.DATA)
        if (!TextUtils.isEmpty(title)) {
            //设置标题
            setTitle(title)
        }
        if (!TextUtils.isEmpty(url)) {
            //加载网址
            binding.web.loadUrl(url!!)
        } else if (!TextUtils.isEmpty(data)) {
            //加载字符串html
            binding.web.loadDataWithBaseURL(null, data!!, "text/html", "utf-8", null)
        }
    }

    private fun switchFullScreen() {
        if (resources.configuration.orientation === Configuration.ORIENTATION_PORTRAIT) {
            //横屏
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            //竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
            }
        }
    }

    protected override fun onDestroy() {
        binding.web.destroy()
        super.onDestroy()
    }

    companion object {
        /**
         * 定义静态的启动方法
         * 好处是用户只要看到声明
         * 就知道该界面需要哪些参数
         *
         * @param activity
         * @param title
         * @param url
         */
        fun start(activity: Activity, url: String, title: String? = null) {
            //创建Intent
            val intent = Intent(activity, WebActivity::class.java)

            title?.let {
                //添加标题
                intent.putExtra(Constants.TITLE_KEY, title)
            }

            //添加url
            intent.putExtra(Constants.URL, url)

            //启动界面
            activity.startActivity(intent)
        }

        /**
         * 显示字符串html
         *
         * @param activity
         * @param title
         * @param data
         */
        fun startWithData(activity: Activity, title: String, data: String) {
            //创建Intent
            val intent = Intent(activity, WebActivity::class.java)
            intent.putExtra(Constants.TITLE_KEY, title)
            intent.putExtra(Constants.DATA, data)
            activity.startActivity(intent)
        }
    }
}