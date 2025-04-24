package com.suzhe.playdemo.component.splash

import android.Manifest
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.activity.BaseViewModelActivity
import com.suzhe.playdemo.component.recyclerView.RecyclerViewDemoActivity
import com.suzhe.playdemo.databinding.ActivitySplashBinding
import com.suzhe.playdemo.utils.PreferenceUtil
import com.suzhe.playdemo.utils.SuperDarkUtil
import java.util.Calendar

class SplashActivity : BaseViewModelActivity<ActivitySplashBinding>() {

    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO
    )

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // 处理权限请求的结果
        var allGranted = true
        permissions.entries.forEach { entry ->
            Log.d(
                "Permission",
                "Permission ${entry.key} is ${if (entry.value) "granted" else "denied"}"
            )
            if (!entry.value) {
                allGranted = false
            }
        }

        if (allGranted) {
            // 所有权限都被授予
            binding.root.postDelayed({
                prepareNext()
            }, 1000)
        } else {
            // 至少有一个权限被拒绝
            ToastUtils.make().show("Permissions not granted by the user.")
            finish()
        }
    }

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
        if (PreferenceUtil.isAcceptTermsServiceAgreement()) {
            //已经同意了用户协议，请求多个权限
            requestPermission()
        } else {
            showTermsServiceAgreementDialog()
        }

        //设置版本年份
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.copyright.text = getString(R.string.copyright, currentYear)
    }

    private fun requestPermission() {
        requestMultiplePermissionsLauncher.launch(requiredPermissions)
    }

    private fun showTermsServiceAgreementDialog() {
        TermServiceDialogFragment.show(supportFragmentManager) {
            PreferenceUtil.setAcceptTermsServiceAgreement()
            requestPermission()
        }
    }


    private fun prepareNext() {
        ActivityUtils.startActivity(RecyclerViewDemoActivity::class.java)
        finish()
    }

}