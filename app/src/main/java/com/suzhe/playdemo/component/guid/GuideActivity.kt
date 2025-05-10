package com.suzhe.playdemo.component.guid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.suzhe.playdemo.R

class GuideActivity : AppCompatActivity() {
    private lateinit var adapter: GuideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_guide)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // 初始化 pageImages
        val pageImages = listOf(
            R.drawable.bg_lvzhi,
            R.drawable.bg_danche,
            R.drawable.bg_huacao,
            R.drawable.bg_caocong,
            R.drawable.bg_songshu
        )

        // 创建适配器并传入数据源
        adapter = GuideAdapter(
            supportFragmentManager, // 使用 supportFragmentManager
            lifecycle,
            pageImages
        )

        // 设置适配器到控件
        viewPager.adapter = adapter
    }
}