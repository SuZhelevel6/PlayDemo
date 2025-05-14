package com.suzhe.playdemo.view

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
 * Layout to wrap a scrollable component inside a ViewPager2. Provided as a solution to the problem
 * where pages of ViewPager2 have nested scrollable elements that scroll in the same direction as
 * ViewPager2. The scrollable element needs to be the immediate and only child of this host layout.
 *
 * This solution has limitations when using multiple levels of nested scrollable elements
 * (e.g. a horizontal RecyclerView in a vertical RecyclerView in a horizontal ViewPager2).
 *
 * 来自：https://github.com/android/views-widgets-samples/blob/master/ViewPager2/app/src/main/java/androidx/viewpager2/integration/testapp/NestedScrollableHost.kt#L71
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

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

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
     * 滑动冲突处理核心逻辑（网页6内部拦截法的典型实现）
     */
    private fun handleInterceptTouchEvent(e: MotionEvent) {
        val orientation = parentViewPager?.orientation ?: return  // 获取ViewPager2滑动方向

        // 若子视图完全无法滚动，无需处理冲突（如子视图内容不足）
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) return

        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = e.x
                initialY = e.y
                parent.requestDisallowInterceptTouchEvent(true)  // 初始禁止父容器拦截
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = e.x - initialX  // X轴滑动距离
                val dy = e.y - initialY  // Y轴滑动距离
                val isVpHorizontal = orientation == ViewPager2.ORIENTATION_HORIZONTAL

                // 优化方向判断（通过缩放敏感度区分主次方向）
                val scaledDx =
                    dx.absoluteValue * if (isVpHorizontal) 0.5f else 1f  // 横向ViewPager降低横向敏感度
                val scaledDy =
                    dy.absoluteValue * if (isVpHorizontal) 1f else 0.5f  // 纵向ViewPager降低纵向敏感度

                // 当任一方向超过滑动阈值时判断处理（阈值判断）
                if (scaledDx > touchSlop || scaledDy > touchSlop) {
                    if (isVpHorizontal == (scaledDy > scaledDx)) {
                        // 手势方向与ViewPager2方向垂直（如横向ViewPager中的上下滑动）
                        parent.requestDisallowInterceptTouchEvent(false)  // 允许父容器拦截（传递非主方向事件）
                    } else {
                        // 手势方向与ViewPager2方向平行（需判断子视图滚动能力）
                        val canScroll = canChildScroll(orientation, if (isVpHorizontal) dx else dy)
                        parent.requestDisallowInterceptTouchEvent(canScroll)  // 动态调整拦截策略
                    }
                }
            }
        }
    }
}