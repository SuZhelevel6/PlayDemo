package com.suzhe.playdemo.component.library

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.databinding.FragmentLibraryBinding
import com.suzhe.playdemo.utils.Constants.FRAGMENT_ID

class LibraryFragment : BaseViewModelFragment<FragmentLibraryBinding>() {
    override fun initDatum() {
        super.initDatum()

    }

    override fun initListeners() {
        super.initListeners()
        binding.btnDialogX.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(FRAGMENT_ID, "DialogExampleFragment")
            ActivityUtils.startActivity(bundle, LibraryContentActivity::class.java)
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
