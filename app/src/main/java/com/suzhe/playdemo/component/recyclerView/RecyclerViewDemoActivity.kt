package com.suzhe.playdemo.component.recyclerView

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.activity.BaseViewModelActivity
import com.suzhe.playdemo.databinding.ActivityRecyclerViewDemoBinding

/**
 * RecyclerView 的实现主要依赖于以下三个核心组件：
 *
 * Adapter：Adapter 是数据源与 RecyclerView 之间的桥梁，负责将数据绑定到视图上。
 * ViewHolder：ViewHolder 是对 RecyclerView 中每个条目视图的缓存，它避免了每次滑动时都要重新查找视图的性能问题。
 * LayoutManager：LayoutManager 决定了 RecyclerView 中条目的排列方式，它支持多种布局类型，如 LinearLayoutManager（线性布局）、GridLayoutManager（网格布局）、StaggeredGridLayoutManager（瀑布流布局）等。
 *
 * 使用方式三步走：
 * 1. 布局配置：根部局和子布局
 * 2. 适配器配置：Adapter 和 ViewHolder
 * 3. 布局管理器配置：LayoutManager
 */
class RecyclerViewDemoActivity : BaseViewModelActivity<ActivityRecyclerViewDemoBinding>() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BaseQuickDemoAdapter
    private lateinit var callback:ItemTouchHelperCallback
    private lateinit var itemTouchHelper:ItemTouchHelper

    private val itemList = mutableListOf<Any>(
        "Text Item 1",                      // 文本条目
        "Text Item 2",                      // 文本条目
        Pair(android.R.drawable.btn_star_big_on, "Image and Text Item 1"), // 图片和文本条目
        "Text Item 3",                      // 文本条目
        Pair(R.drawable.ic_launcher_background, "Image and Text Item 2"),  // 图片和文本条目
        "Text Item 4",                      // 文本条目
        "Text Item 5",                      // 文本条目
        "Text Item 6",                      // 文本条目
        "Text Item 7",                      // 文本条目
        "Text Item 8",                      // 文本条目
        "Text Item 9",                      // 文本条目
        "Text Item 10",                     // 文本条目
        "Text Item 11",                     // 文本条目
        "Text Item 12",                     // 文本条目
        "Text Item 13",                     // 文本条目
        "Text Item 14",                     // 文本条目
        "Text Item 15",                     // 文本条目
    )

    override fun initViews() {
        super.initViews()

        initRecyclerView()

        //初始化 callback
        callback = ItemTouchHelperCallback(adapter)
        // 初始化 ItemTouchHelper 绑定callback
        itemTouchHelper = ItemTouchHelper(callback)
        // 初始化 ItemTouchHelper 绑定的RecycleView
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun initListeners() {
        super.initListeners()
        binding.btnSwitchLayout.setOnClickListener{
            switchLayoutManager()
        }
        binding.btnAddItem.setOnClickListener {
            // 添加新条目
            showInputDialog()
        }
    }

    private fun initRecyclerView() {
        // 获取 RecyclerView 控件
        recyclerView = binding.recyclerView

        // RecyclerView.Adapter 的适配器示例
//        adapter = RecyclerViewDemoAdapter(itemList)
        // BaseQuickAdapter 的适配器示例
        adapter = BaseQuickDemoAdapter().apply {
            // 设置数据集合
            submitList(itemList)
            // item 点击事件, setOnDebouncedItemClick 可以改成去除点击抖动
//            setOnItemClickListener { adapter, view, position ->
//                ("onItemClick $position").shortToast()
//            }
//            // item 长按事件
//            setOnItemLongClickListener { adapter, view, position ->
//                PopTip.show("长按")
//                true
//            }
//            // item 子控件点击事件
//            addOnItemChildClickListener(R.id.imageViewLeft) { adapter, view, position ->
//                ("onItemChildClick:  add $position").shortToast()
//            }
//            // item 子控件长按事件
//            // 需要传递控件 id
//            addOnItemChildLongClickListener(R.id.imageViewLeft) { adapter, view, position ->
//                ("onItemChildLongClick $position").shortToast()
//                true
//            }
            animationEnable = true
            setItemAnimation(BaseQuickAdapter.AnimationType.SlideInBottom)

        }

        // 设置适配器
        recyclerView.adapter = adapter

        // 设置 RecyclerView 初始布局管理器
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun showInputDialog() {
        // 创建一个 AlertDialog 对话框
        InputDialog("添加新内容", "输入文本...", "确定", "取消")
            .setInputText("123456") // 设置默认输入内容
            .setOkButton(object : OnInputDialogButtonClickListener<InputDialog> {
                override fun onClick(
                    dialog: InputDialog,
                    v: View,
                    inputStr: String
                ): Boolean {
                    if (inputStr.isNotEmpty()) {
                        // 尾部新增数据
                        adapter.add(inputStr)
                        PopTip.show("已添加")
                        return false // 保持对话框开启
                    }
                    PopTip.show("没有输入任何内容")
                    return true // 关闭对话框
                }
            })
            .show()
    }

    private fun switchLayoutManager() {

        val currentLayoutManager = recyclerView.layoutManager
        if (currentLayoutManager is LinearLayoutManager && currentLayoutManager !is GridLayoutManager) {
            // 从线性布局切换到网格布局
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        } else if (currentLayoutManager is GridLayoutManager) {
            // 从网格布局切换到瀑布流布局
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        } else if (currentLayoutManager is StaggeredGridLayoutManager) {
            // 从瀑布流布局切换回线性布局
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            // 默认切换到线性布局
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        // 更新适配器
        recyclerView.adapter = adapter
    }

    // 定义条目的类型常量
    companion object {
        const val TYPE_TEXT = 0            // 文本条目类型
        const val TYPE_IMAGE_TEXT = 1      // 混合条目类型
    }
}
