package com.suzhe.lib.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * 用于包装 ViewPager2 内部可滚动组件的布局
 * 解决 ViewPager2 与内部同向滚动组件的滑动冲突问题
 *
 * 使用方式：将可滚动组件作为此布局的唯一直接子视图
 *
 * 来源：https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/main/java/androidx/viewpager2/integration/testapp/NestedScrollableHost.kt
 */
class NestedScrollableHost : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f

    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View?
        get() = if (childCount > 0) getChildAt(0) else null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> child?.canScrollHorizontally(direction) ?: false
            1 -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }

    /**
     * 滑动冲突处理核心逻辑（内部拦截法实现）
     */
    private fun handleInterceptTouchEvent(e: MotionEvent) {
        val orientation = parentViewPager?.orientation ?: return

        // 若子视图完全无法滚动，无需处理冲突
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) return

        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = e.x
                initialY = e.y
                parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = e.x - initialX
                val dy = e.y - initialY
                val isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL

                // 通过缩放敏感度区分主次方向
                val scaledDx = dx.absoluteValue * if (isVpHorizontal) 0.5f else 1f
                val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else 0.5f

                if (scaledDx > touchSlop || scaledDy > touchSlop) {
                    if (isVpHorizontal == (scaledDy > scaledDx)) {
                        // 手势方向与 ViewPager2 方向垂直
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else {
                        // 手势方向与 ViewPager2 方向平行，需判断子视图滚动能力
                        val canScroll = canChildScroll(orientation, if (isVpHorizontal) dx else dy)
                        parent.requestDisallowInterceptTouchEvent(canScroll)
                    }
                }
            }
        }
    }
}
