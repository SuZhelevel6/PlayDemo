package com.suzhe.playdemo.component.library

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.component.recyclerView.RecyclerViewDemoActivity
import com.suzhe.playdemo.databinding.FragmentLibraryBinding
import com.suzhe.lib.common.constants.Constants.FRAGMENT_ID

class LibraryFragment : BaseViewModelFragment<FragmentLibraryBinding>() {
    override fun initDatum() {
        super.initDatum()

    }

    override fun initListeners() {
        super.initListeners()

        binding.apply {

            // 标题栏
            tvLibraryHeader.text = "UI 相关"

            // DialogX 按钮
            btnDialogX.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(FRAGMENT_ID, "DialogX")
                ActivityUtils.startActivity(bundle, LibraryContentActivity::class.java)
            }

            btnRecyclerView.setOnClickListener {
                ActivityUtils.startActivity(hostActivity, RecyclerViewDemoActivity::class.java)
            }

            btnBRVAH.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(FRAGMENT_ID, "BRVAH")
                ActivityUtils.startActivity(bundle, LibraryContentActivity::class.java)
            }

        }
    }

    override fun pageId(): String? {
        return "Library"
    }

    companion object {
        fun newInstance(): LibraryFragment {
            val args = Bundle()

            val fragment = LibraryFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
