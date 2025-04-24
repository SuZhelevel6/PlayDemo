package com.suzhe.playdemo.component.recyclerView

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

// 自定义 ItemTouchHelper 的回调类，用于处理拖拽排序和滑动删除
// https://blog.csdn.net/eLiRins/article/details/144571015
class ItemTouchHelperCallback(var adapter: BaseQuickDemoAdapter) : ItemTouchHelper.Callback() {

    // 获取 RecyclerView 项目可以执行的动作标志
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // 设置拖拽方向：可以向上或向下拖动
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        // 设置滑动方向：可以向左或向右滑动
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        // 返回组合的标志：拖拽和滑动
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    // 处理拖拽交换项位置的操作 - 有BUG
    override fun onMove(
        recyclerView: RecyclerView,
        source: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 获取源项和目标项的位置
        val fromPosition = source.adapterPosition
        val toPosition = target.adapterPosition
        // 调用适配器中的方法，执行项的交换
        adapter.swap(fromPosition, toPosition)
        return true // 返回 true 表示已处理该事件
    }

    // 处理滑动删除项操作
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 获取当前被滑动项的位置
        val position = viewHolder.adapterPosition
        // 调用适配器中的方法，删除该位置的项
        adapter.removeAt(position)
        // 通知适配器数据已被删除，确保数据同步
        adapter.notifyItemRemoved(position)
    }

    // 选中项时的处理逻辑，修改选中项的透明度来显示拖动状态
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        // 如果状态不是空闲状态（即有拖动或滑动操作）
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // 修改选中项的透明度，表示该项正在被拖动
            viewHolder?.itemView?.alpha = 0.5f
        }
    }

    // 清除选中项时的状态，恢复透明度
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        // 恢复拖动后的透明度
        viewHolder.itemView.alpha = 1f
    }

    // 设置是否启用长按拖动功能
    override fun isLongPressDragEnabled(): Boolean {
        // 返回 true 表示启用长按拖动
        return true
    }

    // 设置是否启用滑动删除功能
    override fun isItemViewSwipeEnabled(): Boolean {
        // 返回 true 表示启用滑动删除
        return true
    }
}