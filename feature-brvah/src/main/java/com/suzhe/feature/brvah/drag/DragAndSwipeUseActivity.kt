package com.suzhe.feature.brvah.drag

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.dragswipe.QuickDragAndSwipe
import com.chad.library.adapter4.dragswipe.listener.OnItemDragListener
import com.chad.library.adapter4.dragswipe.listener.OnItemSwipeListener
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.kongzue.dialogx.dialogs.PopTip
import com.suzhe.lib.base.activity.BaseTitleActivity
import com.suzhe.lib.common.data.DataServer
import com.suzhe.feature.brvah.databinding.ActivityDragAndSwipeUseBinding
import com.suzhe.lib.common.utils.vibrate

class DragAndSwipeUseActivity : BaseTitleActivity<ActivityDragAndSwipeUseBinding>() {

    private lateinit var recyclerView: RecyclerView
    private val mAdapter: DragAndSwipeAdapter = DragAndSwipeAdapter()
    private val helper by lazy(LazyThreadSafetyMode.NONE) {
        QuickAdapterHelper.Builder(mAdapter)
            .build()
    }

    private val quickDragAndSwipe = QuickDragAndSwipe()
        .setDragMoveFlags(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        )
        .setSwipeMoveFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

    override fun initViews() {
        super.initViews()
        recyclerView = binding.rv
        recyclerView.adapter = helper.adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        initMenu()
    }

    override fun initDatum() {
        super.initDatum()
        mAdapter.submitList(DataServer.getStringItems(40))
    }

    override fun initListeners() {
        super.initListeners()
        // 拖拽监听
        val listener: OnItemDragListener = object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                // 震动反馈
                vibrate()
                LogUtils.d("drag start")
                val holder = viewHolder as QuickViewHolder? ?: return
                // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.WHITE
                val endColor = Color.rgb(245, 245, 245)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation: ValueAnimator ->
                        holder.itemView.setBackgroundColor(
                            animation.animatedValue as Int
                        )
                    }
                    v.duration = 300
                    // 这里的动画效果会和我的背景设置冲突
//                    v.start()
                }
            }

            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder,
                from: Int,
                target: RecyclerView.ViewHolder,
                to: Int
            ) {
                LogUtils.d(
                    "move from: " + source.bindingAdapterPosition + " to: " + target.bindingAdapterPosition
                )
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                LogUtils.d("drag end")
                val holder = viewHolder as QuickViewHolder
                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.rgb(245, 245, 245)
                val endColor = Color.WHITE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation: ValueAnimator ->
                        holder.itemView.setBackgroundColor(
                            animation.animatedValue as Int
                        )
                    }
                    v.duration = 300
//                    v.start()
                }
            }
        }

        val swipeListener: OnItemSwipeListener = object : OnItemSwipeListener {
            override fun onItemSwipeStart(
                viewHolder: RecyclerView.ViewHolder?,
                bindingAdapterPosition: Int
            ) {
                LogUtils.d("onItemSwipeStart")
            }

            override fun onItemSwipeEnd(
                viewHolder: RecyclerView.ViewHolder,
                bindingAdapterPosition: Int
            ) {
                LogUtils.d("onItemSwipeEnd")
            }

            override fun onItemSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int,
                bindingAdapterPosition: Int
            ) {
                LogUtils.d("onItemSwiped")
            }

            override fun onItemSwipeMoving(
                canvas: Canvas,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                isCurrentlyActive: Boolean
            ) {
                LogUtils.d("onItemSwipeMoving")
            }
        }

        // 滑动事件
        quickDragAndSwipe.attachToRecyclerView(recyclerView)
            .setDataCallback(mAdapter)
            .setItemDragListener(listener)
            .setItemSwipeListener(swipeListener)

        // 点击事件
        mAdapter.setOnItemClickListener { adapter, view, position ->
            PopTip.show("点击了：$position")
        }
    }

    /**
     * Init menu
     * 初始化下拉菜单
     */
    private fun initMenu() {
        binding.spinner.setItems(
            "LinearLayoutManager",// 线性布局
            "GridLayoutManager",// 网格布局
            "StaggeredGridLayoutManager"//瀑布流布局
        )
        binding.spinner.setOnItemSelectedListener { _, position, _, _ ->
            when (position) {
                0 -> recyclerView.layoutManager = LinearLayoutManager(this)
                1 -> recyclerView.layoutManager = GridLayoutManager(this, 2)
                2 -> recyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

                else -> {}
            }
        }
    }
}