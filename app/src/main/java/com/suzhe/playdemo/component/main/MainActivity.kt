package com.suzhe.playdemo.component.main

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentContainerView
import androidx.viewpager2.widget.ViewPager2
import com.angcyo.tablayout.DslTabLayout
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialogx.dialogs.MessageMenu
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnMenuButtonClickListener
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.util.QMUIViewOffsetHelper
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2
import com.qmuiteam.qmui.widget.popup.QMUIPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.activity.BaseViewModelActivity
import com.suzhe.playdemo.component.splash.TermServiceDialogFragment
import com.suzhe.playdemo.databinding.ActivityMainBinding
import com.suzhe.playdemo.databinding.ActivitySplashBinding
import com.suzhe.playdemo.databinding.ItemTabBinding
import com.suzhe.playdemo.utils.PreferenceUtil
import com.suzhe.playdemo.utils.SuperDarkUtil
import java.util.Calendar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : BaseViewModelActivity<ActivityMainBinding>() {

    // 新增：Splash相关变量
    private lateinit var splashBinding: ActivitySplashBinding
    private val splashExecutor: ExecutorService = Executors.newFixedThreadPool(2)
    private val mainHandler = Handler(Looper.getMainLooper())

    private var context = this@MainActivity
    private lateinit var mGlobalAction: QMUIPopup
    private lateinit var mTabs: DslTabLayout
    private lateinit var mPager: ViewPager2

    private val tabTitles =
        arrayOf(
            "Discover",
            "Library",
            "Category",
            "Me"
        )

    private val tabIcons = intArrayOf(
        R.drawable.selector_tab_discovery,
        R.drawable.selector_tab_library,
        R.drawable.selector_tab_category,
        R.drawable.selector_tab_me
    )

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
            executeSplashTasks()
        } else {
            // 至少有一个权限被拒绝
            ToastUtils.make().show("Permissions not granted by the user.")
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashView()
        binding.root.addView(SkinFlotButton(context))
    }

    private fun initSplashView() {
        // 先隐藏主界面
        binding.mainContainer.visibility = View.GONE

        // 初始化Splash布局
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        val splashContainer = FrameLayout(this)
        splashContainer.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        splashContainer.addView(splashBinding.root)
        setContentView(splashContainer)

        // 设置状态栏
        QMUIStatusBarHelper.translucent(this)
        if (SuperDarkUtil.isDark(this)) {
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }

        // 设置版本年份
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        splashBinding.copyright.text = getString(R.string.copyright, currentYear)

        // 检查用户协议
        if (PreferenceUtil.isAcceptTermsServiceAgreement()) {
            requestPermission()
        } else {
            showTermsServiceAgreementDialog()
        }
    }

    // 新增：执行Splash期间的并发任务
    private fun executeSplashTasks() {
        Log.d("Splash", "Executing splash tasks...")
        // 任务1：模拟延迟
        splashExecutor.submit {

            // 任务2：异步预加载View或其他初始化操作
            preloadViews()

            // 回到主线程切换界面
            mainHandler.post {
                switchToMainContent()
            }
        }
    }

    // 新增：预加载视图等操作
    private fun preloadViews() {
        Log.d("Splash", "Preloading views...")
        // 在这里执行View预加载或其他耗时操作
        try {

            // 可以添加更多预加载任务
        } catch (e: Exception) {
            Log.e("Splash", "Error preloading views", e)
        }
    }

    // 新增：切换到主内容界面
    private fun switchToMainContent() {
        Log.d("Splash", "Switching to main content...")
        // 显示主界面
        setContentView(binding.root)
        binding.mainContainer.visibility = View.VISIBLE
    }

    // 新增：权限请求
    private fun requestPermission() {
        requestMultiplePermissionsLauncher.launch(requiredPermissions)
    }

    // 新增：显示服务条款对话框
    private fun showTermsServiceAgreementDialog() {
        TermServiceDialogFragment.show(supportFragmentManager) {
            PreferenceUtil.setAcceptTermsServiceAgreement()
            requestPermission()
        }
    }

    // 新增：销毁时关闭线程池
    override fun onDestroy() {
        super.onDestroy()
        splashExecutor.shutdown()
    }


    override fun initViews() {
        super.initViews()
        initTabs()
        initPagers()
    }

    private fun initPagers() {
        mPager = binding.pager
        mPager.apply {
            offscreenPageLimit = tabTitles.size
            adapter = MainAdapter(this@MainActivity, tabTitles.size)
            ViewPager2Delegate.install(mPager, mTabs, false)
        }
    }

    private fun initTabs() {
        mTabs = binding.tab
        for (i in tabTitles.indices) {
            ItemTabBinding.inflate(layoutInflater).apply {
                content.setText(tabTitles[i])
                icon.setImageResource(tabIcons[i])
                mTabs.addView(root)
            }
        }
    }


    private fun showGlobalActionPopup(v: View) {
        // 1. 定义菜单项数据
        val listItems = arrayOf(
            "Change Skin"
        )
        val data = listItems.toList()

        // 2. 构建ArrayAdapter，适配菜单项列表到弹窗中
        val adapter = ArrayAdapter(context, R.layout.simple_list_item, data)

        // 3. 设置菜单项点击事件监听器
        val onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            // 1. 根据点击的菜单项索引判断执行操作
            if (i == 0) {
                // 定义皮肤选项
                val items = arrayOf("蓝色（默认）", "黑色", "白色")

                var selectMenuIndex = 0
                MessageMenu.show(items)
                    .setMessage("请选择你喜欢的主题")
                    .setTitle("皮肤选择器")
                    .setOnMenuItemClickListener(object : OnMenuItemSelectListener<MessageMenu>() {
                        override fun onOneItemSelect(
                            dialog: MessageMenu,
                            text: CharSequence,
                            index: Int,
                            select: Boolean
                        ) {
                            selectMenuIndex = index
                        }
                    })
                    .setCancelButton("确认", OnMenuButtonClickListener<MessageMenu> { dialog, _ ->
                        PopTip.show("已选择菜单")
                        false
                    })
                    .setSelection(selectMenuIndex)
            }
            // 3. 无论点击哪个菜单项，关闭全局操作弹窗
            mGlobalAction.dismiss()
        }

        // 4. 构建全局操作弹窗，设置弹窗样式、方向、阴影等属性，并显示在传入的视图上
        mGlobalAction = QMUIPopups.listPopup(
            context,
            QMUIDisplayHelper.dp2px(context, 250),
            QMUIDisplayHelper.dp2px(context, 300),
            adapter,
            onItemClickListener
        )
            .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
            .preferredDirection(QMUIPopup.DIRECTION_TOP)
            .shadow(true)
            .edgeProtection(QMUIDisplayHelper.dp2px(context, 10))
            .offsetYIfTop(QMUIDisplayHelper.dp2px(context, 5))
//            .skinManager(QMUISkinManager.defaultInstance(context))
            .show(v)
    }

    inner class SkinFlotButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : FrameLayout(context, attrs, defStyleAttr) {

        //region 成员变量定义
        /// Fragment容器，用于展示Fragment页面
        private val fragmentContainer: FragmentContainerView

        /// 全局按钮，圆形图片按钮，用于触发全局操作弹窗
        private val globalBtn: QMUIRadiusImageView2

        /// 按钮位置偏移辅助类，用于处理按钮的拖动效果
        private val globalBtnOffsetHelper: QMUIViewOffsetHelper

        /// 按钮尺寸，单位：像素
        private val btnSize: Int

        /// 触摸最小位移距离，用于判断是否拖动
        private val touchSlop: Int

        /// 记录触摸按下时的X坐标
        private var touchDownX = 0f

        /// 记录触摸按下时的Y坐标
        private var touchDownY = 0f

        /// 记录上一次触摸时的X坐标
        private var lastTouchX = 0f

        /// 记录上一次触摸时的Y坐标
        private var lastTouchY = 0f

        /// 标识是否处于拖动状态
        private var isDragging = false

        /// 标识触摸是否在全局按钮区域内开始
        private var isTouchDownInGlobalBtn = false
        //endregion

        init {
            //region 构造方法
            // 1. 根据屏幕密度计算按钮尺寸（56dp转换为像素）
            btnSize = QMUIDisplayHelper.dp2px(context, 56)

            // 2. 构造Fragment容器，并设置布局参数为充满全屏
            fragmentContainer = FragmentContainerView(context)
            fragmentContainer.id = generateViewId()
            addView(
                fragmentContainer,
                LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            // 3. 构造全局按钮，并设置图标、缩放模式、圆角及阴影效果
            globalBtn = QMUIRadiusImageView2(context).apply {
                setImageResource(R.drawable.skin)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setRadiusAndShadow(
                    btnSize / 2,
                    QMUIDisplayHelper.dp2px(context, 16),
                    0.4f
                )
                borderWidth = 1
//                borderColor = QMUIResHelper.getAttrColor(context, R.attr.qmui_skin_support_color_separator)
//                setBackgroundColor(QMUIResHelper.getAttrColor(context, R.attr.app_skin_common_background))
                // 4. 设置全局按钮点击事件，点击时显示全局操作弹窗
                setOnClickListener { showGlobalActionPopup(it) }
            }

            // 5. 设置全局按钮的布局参数：按钮置于右下角，并设置底部与右侧外边距
            val globalBtnLp = LayoutParams(btnSize, btnSize).apply {
                gravity = Gravity.BOTTOM or Gravity.RIGHT
                bottomMargin = QMUIDisplayHelper.dp2px(context, 120)
                rightMargin = QMUIDisplayHelper.dp2px(context, 20)
            }

            // 6. 设置按钮皮肤属性，支持动态换肤
//            val builder = QMUISkinValueBuilder.acquire()
//            builder.background(R.color.primary_100)
//            builder.border(R.color.vip_border)
//            builder.tintColor(R.color.link)
//            QMUISkinHelper.setSkinValue(globalBtn, builder)
//            builder.release()

            // 7. 将全局按钮添加到根视图中
            addView(globalBtn, globalBtnLp)
            // 8. 初始化按钮位置偏移辅助类，用于处理拖动过程中的位置变化
            globalBtnOffsetHelper = QMUIViewOffsetHelper(globalBtn)
            // 9. 获取系统设置的触摸最小滑动距离，作为判断拖动的阈值
            touchSlop = ViewConfiguration.get(context).scaledTouchSlop
            //endregion
        }

        //region 重写布局方法
        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            // 1. 调用父类onLayout进行基本布局
            super.onLayout(changed, left, top, right, bottom)
            // 2. 通知偏移辅助类更新全局按钮布局位置（调试建议：此处可断点查看偏移量变化）
            globalBtnOffsetHelper.onViewLayout()
        }
        //endregion

        //region 触摸事件拦截与处理
        override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y
            when (event.action) {
                // 1. 按下时记录位置，用来判断是否是拖动
                MotionEvent.ACTION_DOWN -> {
                    isTouchDownInGlobalBtn = isDownInGlobalBtn(x, y)
                    touchDownX = x
                    lastTouchX = x
                    touchDownY = y
                    lastTouchY = y
                }
                // 2. 拖动时判断是否在全局按钮范围内
                MotionEvent.ACTION_MOVE -> {
                    if (!isDragging && isTouchDownInGlobalBtn) {
                        val dx = (x - touchDownX).toInt()
                        val dy = (y - touchDownY).toInt()
                        // 3. 当移动距离超过触摸最小滑动距离时，设置为拖动状态
                        if (Math.sqrt(dx * dx + dy * dy.toDouble()) > touchSlop) {
                            isDragging = true
                        }
                    }
                }
            }
            // 4. 如果处于拖动状态，拦截触摸事件
            return isDragging
        }

        /// 辅助方法：判断触摸点是否在全局按钮范围内
        private fun isDownInGlobalBtn(x: Float, y: Float): Boolean {
            return globalBtn.left < x && globalBtn.right > x && globalBtn.top < y && globalBtn.bottom > y
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x
            val y = event.y
            when (event.action) {
                // 1. 处理移动事件，更新按钮位置
                MotionEvent.ACTION_MOVE -> {
                    if (isDragging) { // 确保仅在拖动状态下处理
                        val dx = (x - lastTouchX).toInt()
                        val dy = (y - lastTouchY).toInt()
                        val gx = globalBtn.left
                        val gy = globalBtn.top
                        val gw = globalBtn.width
                        val w = width
                        val gh = globalBtn.height
                        val h = height
                        val newDx =
                            if (gx + dx < 0) -gx else if (gx + dx + gw > w) w - gw - gx else dx
                        val newDy =
                            if (gy + dy < 0) -gy else if (gy + dy + gh > h) h - gh - gy else dy
                        globalBtnOffsetHelper.setLeftAndRightOffset(globalBtnOffsetHelper.leftAndRightOffset + newDx)
                        globalBtnOffsetHelper.setTopAndBottomOffset(globalBtnOffsetHelper.topAndBottomOffset + newDy)
                        lastTouchX = x
                        lastTouchY = y
                    }
                }
                // 2. 处理抬起和取消事件，取消拖动状态
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isDragging = false
                    isTouchDownInGlobalBtn = false
                }
            }
            return isDragging || super.onTouchEvent(event)
        }
        //endregion
        //endregion
    }

}