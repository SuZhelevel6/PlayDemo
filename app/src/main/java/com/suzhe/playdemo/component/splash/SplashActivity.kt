package com.suzhe.playdemo.component.splash

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.TimeUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.activity.BaseLogicActivity
import com.suzhe.playdemo.base.activity.BaseViewModelActivity
import com.suzhe.playdemo.databinding.ActivitySplashBinding
import com.suzhe.playdemo.utils.SuperDarkUtil
import java.util.*

class SplashActivity : BaseViewModelActivity<ActivitySplashBinding>() {

    override fun initViews() {
        super.initViews()
        //设置沉浸式状态栏
        QMUIStatusBarHelper.translucent(this)

        if (SuperDarkUtil.isDark(this)) {
            //状态栏文字白色
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        } else {
            //状态栏文字黑色
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }
    }

    override fun initDatum() {
        super.initDatum()
//        if (DefaultPreferenceUtil.getInstance(this).isAcceptTermsServiceAgreement) {
            //已经同意了用户协议
            requestPermission()
//        } else {
            showTermsServiceAgreementDialog()
//        }

        //设置版本年份
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.copyright.text = getString(R.string.copyright, currentYear)
    }

    private fun showTermsServiceAgreementDialog() {

    }

    private fun requestPermission() {

    }

}