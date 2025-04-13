package com.suzhe.playdemo.component.test

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.suzhe.playdemo.base.fragment.BaseViewModelFragment
import com.suzhe.playdemo.component.discovery.DiscoveryFragment
import com.suzhe.playdemo.databinding.FragmentDiscoveryBinding
import com.suzhe.playdemo.databinding.FragmentTestBinding

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TestFragment : BaseViewModelFragment<FragmentTestBinding>() {

    companion object {
        fun newInstance(): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }
}