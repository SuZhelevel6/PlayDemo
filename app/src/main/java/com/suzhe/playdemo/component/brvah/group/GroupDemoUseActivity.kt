package com.suzhe.playdemo.component.brvah.group

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suzhe.playdemo.base.activity.BaseTitleActivity
import com.suzhe.playdemo.data.DataServer
import com.suzhe.playdemo.databinding.ActivityGroupDemoUseBinding

class GroupDemoUseActivity : BaseTitleActivity<ActivityGroupDemoUseBinding>() {
    private lateinit var recyclerView: RecyclerView

    // 创建一个 ConcatAdapter，用来包裹 GroupAdapter
    private val concatAdapter = ConcatAdapter()

    override fun initViews() {
        super.initViews()
        recyclerView = binding.rv
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        // 有几个分组就创建几个 GroupAdapter
        for (i in 0..5) {
            val groupAdapter = GroupAdapter()
            // 把每个分组的 adapter 丢进 ConcatAdapter 里面即可
            groupAdapter.submitList(DataServer.getStringItems(5))
            concatAdapter.addAdapter(groupAdapter)
        }
    }
}