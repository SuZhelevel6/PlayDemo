package com.suzhe.playdemo.component.brvah

import android.content.Intent
import android.os.Bundle
import com.chad.library.adapter4.QuickAdapterHelper
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.component.brvah.animation.AnimationUseActivity
import com.suzhe.playdemo.databinding.FragmentBrvahExampleBinding

class BRVAHExampleFragment : BaseViewModelFragment<FragmentBrvahExampleBinding>() {

    private val itemData : ArrayList<BRVAHEntity>
        get() = arrayListOf(
            BRVAHEntity(sectionTitle = "BaseQuickAdapter 基础功能"),
            BRVAHEntity("RV动画效果", AnimationUseActivity::class.java, R.drawable.icon_animation),
//            BRVAHEntity(
//                "Header/Footer",
//                HeaderAndFooterUseActivity::class.java,
//                R.mipmap.gv_header_and_footer
//            ),
//            BRVAHEntity("EmptyView", EmptyViewUseActivity::class.java, R.mipmap.gv_empty),
//            BRVAHEntity("ItemClick", ItemClickActivity::class.java, R.mipmap.gv_item_click),
//            BRVAHEntity("DataBinding", DataBindingUseActivity::class.java, R.mipmap.gv_databinding),
//            BRVAHEntity("DiffUtil", DifferActivity::class.java, R.mipmap.gv_databinding),

            BRVAHEntity(sectionTitle = "功能模块"),
//            BRVAHEntity("LoadMore(Auto)", AutoLoadMoreRefreshUseActivity::class.java, R.mipmap.gv_pulltorefresh),
//            BRVAHEntity("LoadMore", NoAutoAutoLoadMoreRefreshUseActivity::class.java, R.mipmap.gv_pulltorefresh),
//            BRVAHEntity("DragAndSwipe", DragAndSwipeUseActivity::class.java, R.mipmap.gv_drag_and_swipe),
//            BRVAHEntity("UpFetch", UpFetchUseActivity::class.java, R.drawable.gv_up_fetch),


            BRVAHEntity(sectionTitle = "场景演示"),
//            BRVAHEntity("Group（ConcatAdapter）", GroupDemoActivity::class.java, R.mipmap.gv_animation),
        )

    private val mAdapter by lazy(LazyThreadSafetyMode.NONE) {
        // 这里将数据传入适配器
        BRVAHExampleAdapter(itemData)
    }

    private val helper by lazy(LazyThreadSafetyMode.NONE) {
        QuickAdapterHelper.Builder(mAdapter)
            .build()
            .addBeforeAdapter(HomeTopHeaderAdapter())// 这里加载 Banner 图片
    }

    override fun initViews() {
        super.initViews()
        // 不用设置 layoutManager，因为在 xml 中我已经设置了layoutManager
//        binding.recyclerView.layoutManager = LinearLayoutManager(hostActivity)
        // 将 helper 中的适配器设置给 RecyclerView
        binding.recyclerView.adapter = helper.adapter
    }

    override fun initListeners() {
        super.initListeners()
        // item 点击事件
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val item = adapter.items[position]
            if (!item.isSection) {
                // 这个 Item 是 HomeEntity 类型，里面有 activity 属性
                startActivity(Intent(hostActivity, item.activity))
            }
        }
    }

    companion object {
        fun newInstance(): BRVAHExampleFragment {
            val args = Bundle()

            val fragment = BRVAHExampleFragment()
            fragment.arguments = args
            return fragment
        }
    }
}