package com.suzhe.playdemo.component.guid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.suzhe.playdemo.databinding.FragmentGuideBinding


class GuideFragment : Fragment() {

    private var _binding: FragmentGuideBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d("GuideFragment onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuideBinding.inflate(inflater, container, false)

        // 获取传递的图片资源 ID
        val data = requireArguments().getInt("img")
        binding.icon.setImageResource(data)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(data: Int): GuideFragment {
            val args = Bundle()
            args.putInt("img", data)

            val fragment = GuideFragment()
            fragment.arguments = args
            return fragment
        }
    }
}