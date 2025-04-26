package com.suzhe.playdemo.component.brvah

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import com.suzhe.playdemo.R
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.component.dialogX.DialogExampleFragment
import com.suzhe.playdemo.databinding.FragmentBrvahExampleBinding
import com.suzhe.playdemo.databinding.FragmentDialogExampleBinding

class BRVAHExampleFragment : BaseViewModelFragment<FragmentBrvahExampleBinding>() {

    private val itemData : ArrayList<BRVAHEntity>
        get() = arrayListOf(
            BRVAHEntity(sectionTitle = "BaseQuickAdapter 基础功能"),
//            BRVAHEntity("Animation", AnimationUseActivity::class.java, R.mipmap.gv_animation),
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
        // 一定要设置 layoutManager
        binding.recyclerView.layoutManager = LinearLayoutManager(hostActivity)
        // 将 helper 中的适配器设置给 RecyclerView
        binding.recyclerView.adapter = helper.adapter
    }

    override fun initDatum() {
        super.initDatum()
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