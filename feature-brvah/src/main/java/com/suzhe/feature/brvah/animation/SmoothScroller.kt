import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * 参考：https://juejin.cn/post/7393533296241786918
 * 使用方式：
 *             // 平滑滚动到指定位置并居中对齐
 *             val smoothIndexScroller = SmoothIndexScroller(recyclerView)
 *             smoothIndexScroller.scrollToPosition(20, SnapPreference.SNAP_TO_CENTER)
 *
 *             // 即时滚动到指定位置并居中对齐
 *             val immediateIndexScroller = ImmediateIndexScroller(recyclerView)
 *             immediateIndexScroller.scrollToPosition(20, SnapPreference.SNAP_TO_CENTER)
 */
// 定义对齐方式注解
@Retention(AnnotationRetention.SOURCE)
annotation class SnapPreference {
    companion object {
        const val SNAP_TO_START = 0
        const val SNAP_TO_END = 1
        const val SNAP_TO_ANY = 2
        const val SNAP_TO_CENTER = 3
    }
}

// 定义 IndexScroller 接口
interface IndexScroller {
    fun scrollToPosition(position: Int, @SnapPreference snapPreference: Int)
}

// 自定义 SmoothScroller
class SmoothScroller(
    context: Context,
    @SnapPreference private val verticalSnapPreference: Int,
    @SnapPreference private val horizontalSnapPreference: Int
) : LinearSmoothScroller(context) {

    override fun getVerticalSnapPreference(): Int = verticalSnapPreference

    override fun getHorizontalSnapPreference(): Int = horizontalSnapPreference

    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        return if (snapPreference == SnapPreference.SNAP_TO_CENTER) {
            // itemView的中心点 - recyclerView的中心点
            val boxCenter = boxStart + (boxEnd - boxStart) / 2
            val viewCenter = viewStart + (viewEnd - viewStart) / 2
            boxCenter - viewCenter
        } else {
            super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference)
        }
    }
}

// 实现 IndexScroller 接口的 SmoothIndexScroller
class SmoothIndexScroller(private val recyclerView: RecyclerView) : IndexScroller {

    override fun scrollToPosition(position: Int, @SnapPreference snapPreference: Int) {
        val layoutManager = recyclerView.layoutManager ?: return
        if (position < 0) return

        val scroller = SmoothScroller(
            recyclerView.context,
            snapPreference,
            snapPreference
        ).apply { targetPosition = position }

        layoutManager.startSmoothScroll(scroller)
    }
}

// 实现 IndexScroller 接口的 ImmediateIndexScroller
class ImmediateIndexScroller(private val recyclerView: RecyclerView) : IndexScroller {

    override fun scrollToPosition(position: Int, @SnapPreference snapPreference: Int) {
        if (position == RecyclerView.NO_POSITION) return

        recyclerView.scrollToPosition(position)
        recyclerView.post {
            val targetView = recyclerView.layoutManager?.findViewByPosition(position) ?: return@post
            onTargetFound(targetView, snapPreference)
        }
    }

    private fun onTargetFound(targetView: View, @SnapPreference snapPreference: Int) {
        val rangefinder = Rangefinder(recyclerView.layoutManager)
        val dx = rangefinder.calculateDxToMakeVisible(targetView, snapPreference)
        val dy = rangefinder.calculateDyToMakeVisible(targetView, snapPreference)
        if (dx != 0 || dy != 0) {
            recyclerView.scrollBy(-dx, -dy)
        }
    }
}

// Rangefinder 类
class Rangefinder(private val layoutManager: RecyclerView.LayoutManager?) {

    fun calculateDxToMakeVisible(view: View, @SnapPreference snapPreference: Int): Int {
        return calculateDtToFit(
            view.left,
            view.right,
            getBoxStart(true),
            getBoxEnd(true),
            snapPreference
        )
    }

    fun calculateDyToMakeVisible(view: View, @SnapPreference snapPreference: Int): Int {
        return calculateDtToFit(
            view.top,
            view.bottom,
            getBoxStart(false),
            getBoxEnd(false),
            snapPreference
        )
    }

    private fun getBoxStart(isHorizontal: Boolean): Int {
        val linearLayoutManager = layoutManager as? LinearLayoutManager ?: return 0
        val firstVisibleView = linearLayoutManager.getChildAt(0) ?: return 0
        return if (isHorizontal) {
            linearLayoutManager.getDecoratedLeft(firstVisibleView)
        } else {
            linearLayoutManager.getDecoratedTop(firstVisibleView)
        }
    }

    private fun getBoxEnd(isHorizontal: Boolean): Int {
        val linearLayoutManager = layoutManager as? LinearLayoutManager ?: return 0
        val lastVisibleView =
            linearLayoutManager.getChildAt(linearLayoutManager.childCount - 1) ?: return 0
        return if (isHorizontal) {
            linearLayoutManager.getDecoratedRight(lastVisibleView)
        } else {
            linearLayoutManager.getDecoratedBottom(lastVisibleView)
        }
    }

    private fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        @SnapPreference snapPreference: Int
    ): Int {
        return when (snapPreference) {
            SnapPreference.SNAP_TO_START -> boxStart - viewStart
            SnapPreference.SNAP_TO_END -> boxEnd - viewEnd
            SnapPreference.SNAP_TO_ANY -> {
                val dtStart = boxStart - viewStart
                if (dtStart > 0) return dtStart
                val dtEnd = boxEnd - viewEnd
                if (dtEnd < 0) return dtEnd
                0
            }

            SnapPreference.SNAP_TO_CENTER -> {
                val boxCenter = boxStart + (boxEnd - boxStart) / 2
                val viewCenter = viewStart + (viewEnd - viewStart) / 2
                boxCenter - viewCenter
            }

            else -> 0
        }
    }
}