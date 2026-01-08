package com.suzhe.lib.base.activity

import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding

/**
 * 带有 ToolBar 的通用标题界面
 */
open class BaseTitleActivity<VB : ViewBinding> : BaseViewModelActivity<VB>() {
    private var _toolbar: Toolbar? = null
    protected val toolbar: Toolbar get() = _toolbar!!

    override fun initViews() {
        super.initViews()
        // 子类需要自行设置 toolbar
        _toolbar = findToolbar()
        _toolbar?.let {
            setSupportActionBar(it)
            if (isShowBackMenu()) {
                showBackMenu()
            }
        }
    }

    /**
     * 查找 Toolbar，子类可重写以自定义
     */
    protected open fun findToolbar(): Toolbar? {
        return try {
            val toolbarId = resources.getIdentifier("toolbar", "id", packageName)
            if (toolbarId != 0) findViewById(toolbarId) else null
        } catch (e: Exception) {
            null
        }
    }

    protected open fun showBackMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    protected open fun isShowBackMenu(): Boolean = true

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun setToolBarTitle(title: String) {
        this.title = title
    }
}
