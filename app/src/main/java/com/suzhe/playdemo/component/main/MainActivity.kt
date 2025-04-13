package com.suzhe.playdemo.component.main

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.FragmentContainerView
import androidx.viewpager2.widget.ViewPager2
import com.angcyo.tablayout.DslTabLayout
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.util.QMUIViewOffsetHelper
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2
import com.qmuiteam.qmui.widget.popup.QMUIPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.activity.BaseViewModelActivity
import com.suzhe.playdemo.databinding.ActivityMainBinding
import com.suzhe.playdemo.databinding.ItemTabBinding

class MainActivity : BaseViewModelActivity<ActivityMainBinding>() {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.root.addView(SkinFlotButton(context))
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
                // 构建 AlertDialog
                val builder = android.app.AlertDialog.Builder(context)
                builder.setTitle("选择皮肤")
                    .setItems(items) { dialog, which ->
                        // 这里可以添加切换皮肤的逻辑
                        dialog.dismiss()
                    }
                val dialog = builder.create()
                dialog.show()
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
                MotionEvent.ACTION_DOWN -> {
                    // 1. 记录按下时坐标，判断是否在全局按钮内
                    isTouchDownInGlobalBtn = isDownInGlobalBtn(x, y)
                    touchDownX = x
                    lastTouchX = x
                    touchDownY = y
                    lastTouchY = y
                }

                MotionEvent.ACTION_MOVE -> {
                    // 2. 如果尚未开始拖动，检测是否滑动超过touchSlop阈值以启动拖动
                    if (!isDragging && isTouchDownInGlobalBtn) {
                        val dx = (x - touchDownX).toInt()
                        val dy = (y - touchDownY).toInt()
                        // 新手提示：使用欧几里得距离判断滑动幅度
                        if (Math.sqrt(dx * dx + dy * dy.toDouble()) > touchSlop) {
                            isDragging = true
                        }
                    }
                    // 3. 若处于拖动状态，计算位移并更新全局按钮位置，同时进行边界检测
                    if (isDragging) {
                        val dx = (x - lastTouchX).toInt()
                        val dy = (y - lastTouchY).toInt()
                        val gx = globalBtn.left
                        val gy = globalBtn.top
                        val gw = globalBtn.width
                        val w = width
                        val gh = globalBtn.height
                        val h = height
                        // 边界检测：左侧与右侧
                        val newDx =
                            if (gx + dx < 0) -gx else if (gx + dx + gw > w) w - gw - gx else dx
                        // 边界检测：顶部与底部
                        val newDy =
                            if (gy + dy < 0) -gy else if (gy + dy + gh > h) h - gh - gy else dy
                        // 更新全局按钮的水平与垂直偏移
                        globalBtnOffsetHelper.setLeftAndRightOffset(
                            globalBtnOffsetHelper.leftAndRightOffset + newDx
                        )
                        globalBtnOffsetHelper.setTopAndBottomOffset(
                            globalBtnOffsetHelper.topAndBottomOffset + newDy
                        )
                    }
                    // 4. 更新最后触摸坐标
                    lastTouchX = x
                    lastTouchY = y
                }

                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    // 5. 触摸结束，重置拖动状态与触摸标记
                    isDragging = false
                    isTouchDownInGlobalBtn = false
                }
            }
            // 返回是否拦截事件（若正在拖动则拦截）
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
                MotionEvent.ACTION_DOWN -> {
                    // 记录按下时坐标及是否在全局按钮内
                    isTouchDownInGlobalBtn = isDownInGlobalBtn(x, y)
                    touchDownX = x
                    lastTouchX = x
                    touchDownY = y
                    lastTouchY = y
                }

                MotionEvent.ACTION_MOVE -> {
                    if (!isDragging && isTouchDownInGlobalBtn) {
                        val dx = (x - touchDownX).toInt()
                        val dy = (y - touchDownY).toInt()
                        if (Math.sqrt(dx * dx + dy * dy.toDouble()) > touchSlop) {
                            isDragging = true
                        }
                    }
                    if (isDragging) {
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
                        globalBtnOffsetHelper.setLeftAndRightOffset(
                            globalBtnOffsetHelper.leftAndRightOffset + newDx
                        )
                        globalBtnOffsetHelper.setTopAndBottomOffset(
                            globalBtnOffsetHelper.topAndBottomOffset + newDy
                        )
                    }
                    lastTouchX = x
                    lastTouchY = y
                }

                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    isDragging = false
                    isTouchDownInGlobalBtn = false
                }
            }
            // 返回true表示事件已被处理；若拖动则拦截事件
            return isDragging || super.onTouchEvent(event)
        }
        //endregion
        //endregion
    }

}