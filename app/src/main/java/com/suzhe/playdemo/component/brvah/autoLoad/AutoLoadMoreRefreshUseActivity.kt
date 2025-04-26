package com.suzhe.playdemo.component.brvah.autoLoad

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter.OnTrailingListener
import com.kongzue.dialogx.dialogs.PopTip
import com.suzhe.playdemo.base.activity.BaseTitleActivity
import com.suzhe.playdemo.component.brvah.animation.AnimationAdapter
import com.suzhe.playdemo.data.DataServer
import com.suzhe.playdemo.databinding.ActivityAutoLoadMoreRefreshUseBinding

/**
 * 要实现自动加载需要
 * 1. 在布局文件中使用SwipeRefreshLayout包裹RecyclerView
 * 2. 使用QuickAdapterHelper的setTrailingLoadStateAdapter方法加载数据
 * 3. 创建PageInfo类来管理一次应该加载多少数据，什么时候停止加载
 * 4. 区分mAdapter.submitList方法和mAdapter.addAll方法
 */
class AutoLoadMoreRefreshUseActivity : BaseTitleActivity<ActivityAutoLoadMoreRefreshUseBinding>() {

    internal class PageInfo {
        var page = 0
        fun nextPage() {
            page++
        }

        fun reset() {
            page = 0
        }

        val isFirstPage: Boolean
            get() = page == 0
    }

    private val pageInfo = PageInfo()

    private val mAdapter: AnimationAdapter = AnimationAdapter()
    private var helper: QuickAdapterHelper = QuickAdapterHelper.Builder(mAdapter)
        .setTrailingLoadStateAdapter(object : OnTrailingListener {
            override fun onLoad() {
                request()
            }

            override fun onFailRetry() {
                request()
            }

            override fun isAllowLoading(): Boolean {
                return !binding.refreshLayout.isRefreshing
            }
        }).build()

    override fun onStart() {
        super.onStart()
        // 进入 Activity 时，先进入刷新状态
        binding.refreshLayout.isRefreshing = true
        refresh()
    }

    override fun initViews() {
        super.initViews()

        initRefreshLayout()

        binding.rv.adapter = helper.adapter
        mAdapter.submitList(DataServer.getStringItems100())
    }

    private fun initRefreshLayout() {
        binding.refreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189))
        binding.refreshLayout.setOnRefreshListener { refresh() }
    }

    /**
     * 刷新
     */
    private fun refresh() {
        // 下拉刷新，需要重置页数
        pageInfo.reset()
        // 重置“加载更多”时状态
        helper.trailingLoadState = LoadState.None
        request()
    }

    /**
     * 请求数据
     */
    private fun request() {

        Request(pageInfo.page, object : RequestCallBack {
            override fun success(data: List<String>) {
                binding.refreshLayout.isRefreshing = false
                if (pageInfo.isFirstPage) {
                    // 如果是加载的第一页数据，用 submitList()
                    // If it is the first page of data loaded, use submitList().
                    mAdapter.submitList(data)
                } else {
                    //不是第一页，则用add
                    mAdapter.addAll(data)
                }

                // 如果在数据不满足一屏时，暂停加载更多，请调用下面方法
//                 helper.trailingLoadStateAdapter?.checkDisableLoadMoreIfNotFullPage()

                if (pageInfo.page >= PAGE_SIZE) {
                    /*
                    Set the status to not loaded, and there is no paging data.
                    设置状态为未加载，并且没有分页数据了
                     */
                    helper.trailingLoadState = LoadState.NotLoading(true)
                    PopTip.show("no more data")
                } else {
                    /*
                    Set the state to not loaded, and there is also paginated data
                    设置状态为未加载，并且还有分页数据
                     */
                    helper.trailingLoadState = LoadState.NotLoading(false)
                }

                // page加一
                pageInfo.nextPage()
            }

            override fun fail(e: Exception) {
                PopTip.show("Network error")
                binding.refreshLayout.isRefreshing = false
                helper.trailingLoadState = LoadState.Error(e)
            }
        }).start()
    }

    /**
     * 模拟加载数据的类，不用特别关注
     */
    internal class Request(private val mPage: Int, private val mCallBack: RequestCallBack) :
        Thread() {
        private val mHandler: Handler = Handler(Looper.getMainLooper())

        override fun run() {
            try {
                sleep(800) // 模拟网络延迟
            } catch (ignored: InterruptedException) {
            }
            if (mPage == 3 && mFirstError) {
                // 加载到第3页时，模拟加载失败
                mFirstError = false
                mHandler.post { mCallBack.fail(RuntimeException("load fail")) }
            } else {
                // 模拟加载成功
                val dataSize = PAGE_SIZE
                mHandler.post { mCallBack.success(DataServer.getStringItems(dataSize)) }
            }
        }

        companion object {
            private var mFirstPageNoMore = false
            private var mFirstError = true
        }
    }

    internal interface RequestCallBack {
        /**
         * 模拟加载成功
         *
         * @param data 数据
         */
        fun success(data: List<String>)

        /**
         * 模拟加载失败
         *
         * @param e 错误信息
         */
        fun fail(e: Exception)
    }

    companion object {
        private const val PAGE_SIZE = 14
    }

}