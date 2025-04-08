package com.suzhe.playdemo.component.test

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.dialogs.BottomMenu
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.MessageMenu
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.interfaces.BottomDialogSlideEventLifecycleCallback
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener
import com.kongzue.dialogx.interfaces.OnMenuButtonClickListener
import com.kongzue.dialogx.interfaces.OnMenuItemSelectListener
import com.kongzue.dialogx.style.IOSStyle
import com.kongzue.dialogx.style.KongzueStyle
import com.kongzue.dialogx.style.MIUIStyle
import com.kongzue.dialogx.style.MaterialStyle
import com.kongzue.dialogx.util.TextInfo
import com.suzhe.playdemo.R

class DialogExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_example)

        val singleSelectMenuText = arrayOf("拒绝", "询问", "始终允许", "仅在使用中允许")
        var selectMenuIndex: Int = 0

        val multiSelectMenuText = arrayOf("上海", "北京", "广州", "深圳")
        var selectMenuIndexArray: IntArray
        var multiSelectMenuResultCache: String

        // 初始化样式选择
        val styleGroup = findViewById<MaterialButtonToggleGroup>(R.id.styleGroup)
        styleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnMaterial -> setMaterialStyle()
                    R.id.btnIos -> setIosStyle()
                    R.id.btnKongzue -> setKongzueStyle()
                    R.id.btnMiui -> setMiuiStyle()
                }
            }
        }

        // 显示消息对话框按钮
        findViewById<MaterialButton>(R.id.btnShowMessageDialog).setOnClickListener {
            MessageDialog.show("对话框标题", "这里是对话框的正文内容，可以展示不同风格的视觉效果。", "确定","取消", "其他")
                .setDialogLifecycleCallback(object : DialogLifecycleCallback<MessageDialog>() {
                    override fun onShow(dialog: MessageDialog) {
                        dialog.setTitleIcon(R.mipmap.star)
                    }
                })
                // 按钮显示为纵向
                .setButtonOrientation(LinearLayout.VERTICAL)
        }

        // 显示选择对话框
        findViewById<MaterialButton>(R.id.btnSelectDialog).setOnClickListener {
            MessageDialog("删除确认", "移除App会将它从主屏幕移除并保留其所有数据。", "删除App", "取消", "移至App资源库")
                .apply {
                    // 设置按钮垂直排列
                    buttonOrientation = LinearLayout.VERTICAL
                    // 非MIUI样式时设置红色删除按钮
                    if (DialogX.globalStyle !is MIUIStyle) {
                        setOkTextInfo(TextInfo().apply {
                            fontColor = "#EB5545".toColorInt()
                        })
                    }
                }
                .setOkButton { dialog, _ ->
                    PopTip.show("已删除应用")
                    false
                }
                .setCancelButton { dialog, _ ->
                    PopTip.show("操作已取消")
                    false
                }
                .setOtherButton { dialog, _ ->
                    PopTip.show("已移至资源库")
                    false
                }
                .show()
        }

        // 显示输入对话框按钮（新增部分）
        findViewById<MaterialButton>(R.id.btnInputDialog).setOnClickListener {
            InputDialog("验证身份", "请输入您的登录密码", "确定", "取消")
                .setInputText("123456") // 设置默认输入内容
                .setOkButton(object : OnInputDialogButtonClickListener<InputDialog> {
                    override fun onClick(
                        dialog: InputDialog,
                        v: View,
                        inputStr: String
                    ): Boolean {
                        if (inputStr.length != 6 || !inputStr.matches(Regex("\\d+"))) {
                            PopTip.show("密码必须为6位数字")
                            return true // 保持对话框开启
                        }
                        PopTip.show("验证通过")
                        return false // 关闭对话框
                    }
                })
                .show()
        }

        // 新增单选菜单按钮
        findViewById<MaterialButton>(R.id.btnSelectMessageMenu).setOnClickListener {
            var selectMenuIndex = 0
            MessageMenu.show(singleSelectMenuText)
                .setMessage("这里是权限确认的文本说明")
                .setTitle("权限设置")
                .setOnMenuItemClickListener(object : OnMenuItemSelectListener<MessageMenu>() {
                    override fun onOneItemSelect(dialog: MessageMenu, text: CharSequence, index: Int, select: Boolean) {
                        selectMenuIndex = index
                    }
                })
                .setCancelButton("确定") { dialog, _ ->
                    PopTip.show("已选择：${singleSelectMenuText[selectMenuIndex]}")
                    false
                }
                .setSelection(selectMenuIndex)
        }

        // 新增多选菜单按钮
        findViewById<MaterialButton>(R.id.btnMutiSelectMessageMenu).setOnClickListener {
            var selectMenuIndexArray: IntArray = intArrayOf(1, 2) // 初始化一个包含指定元素的数组
            var multiSelectMenuResultCache = "1"
            MessageMenu.show(multiSelectMenuText)
                .setMessage("请选择城市")
                .setOnMenuItemClickListener(object : OnMenuItemSelectListener<MessageMenu>() {
                    override fun onMultiItemSelect(dialog: MessageMenu, text: Array<out CharSequence>, index: IntArray) {
                        multiSelectMenuResultCache = text.joinToString(" ")
                        selectMenuIndexArray = index
                    }
                })
                .setOkButton("确定") { dialog, _ ->
                    PopTip.show("已选择：$multiSelectMenuResultCache")
                    false
                }
                .setSelection(selectMenuIndexArray)
        }

        // 新增等待对话框按钮
        findViewById<MaterialButton>(R.id.btnWaitDialog).setOnClickListener {
            WaitDialog.show("正在加载...")
                .setOnBackPressedListener {
                    PopTip.show("操作进行中...")
                        .setButton("取消等待") { _, _ ->
                            WaitDialog.dismiss()
                            false
                        }
                    false
                }
            // 3秒后自动关闭
            WaitDialog.dismiss(3000)
        }

        // 显示带完成提示的等待对话框
        findViewById<MaterialButton>(R.id.btnWaitAndTipDialog).setOnClickListener {

            WaitDialog.show("处理中...").setOnBackPressedListener {
                PopTip.show("按下返回", "关闭")
                    .setButton { _, _ ->
                        WaitDialog.dismiss()
                        false
                    }
                false
            }
        }

        // 显示成功提示框
        findViewById<MaterialButton>(R.id.btnTipDialogSuccess).setOnClickListener {
            TipDialog.show("Success!", WaitDialog.TYPE.SUCCESS)
        }

        // 显示警告提示框
        findViewById<MaterialButton>(R.id.btnTipDialogWaring).setOnClickListener {
            TipDialog.show("Warning!", WaitDialog.TYPE.WARNING)
        }

        // 显示错误提示框
        findViewById<MaterialButton>(R.id.btnTipDialogError).setOnClickListener {
            TipDialog.show("Error!", WaitDialog.TYPE.ERROR, 3500)
        }

        // 进度提示对话框按钮
        findViewById<MaterialButton>(R.id.btnTipProgress).setOnClickListener {
            var waitId = 0
            var progress = 0f

            WaitDialog.show("加载中...").apply {
                setOnBackPressedListener {
                    MessageDialog.show("提示", "是否取消加载？", "确定", "取消")
                        .setOkButton { _, _ ->
                            waitId = -1
                            WaitDialog.dismiss()
                            false
                        }
                    false
                }
            }

            // 模拟延迟加载
            Handler(Looper.getMainLooper()).postDelayed({
                if (waitId != 0) return@postDelayed

                var cycleRunner: Runnable? = null
                cycleRunner = object : Runnable {
                    override fun run() {
                        if (waitId != 0) return

                        progress += 0.1f
                        if (progress < 1f) {
                            WaitDialog.show("加载进度 ${(progress * 100).toInt()}%", progress)
                            Handler(Looper.getMainLooper()).postDelayed(this, 1000)
                        } else {
                            TipDialog.show("加载完成", WaitDialog.TYPE.SUCCESS)
                            cycleRunner = null
                        }
                    }
                }
                Handler(Looper.getMainLooper()).post(cycleRunner as Runnable)
            }, 3000)
        }

        // 基本提示按钮
        findViewById<MaterialButton>(R.id.btnPoptip).setOnClickListener {
            TipDialog.show("操作成功", WaitDialog.TYPE.SUCCESS)
        }

        // 大段文本提示按钮
        findViewById<MaterialButton>(R.id.btnPoptipBigMessage).setOnClickListener {
            TipDialog.show(
                SpannableString("这是一个超过三行的大段文本提示对话框演示内容，当文本内容超过三行时会自动启用滚动显示功能。").apply {
                    setSpan(ForegroundColorSpan(("#FFA72C").toColorInt()), 9, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                },
                WaitDialog.TYPE.WARNING
            )
        }

        // 成功提示按钮
        findViewById<MaterialButton>(R.id.btnPoptipSuccess).setOnClickListener {
            PopTip.show("操作已完成")
        }

        // 警告提示按钮
        findViewById<MaterialButton>(R.id.btnPoptipWarning).setOnClickListener {
            PopTip.show(R.mipmap.skin, "收到一个画板")
                .setAutoTintIconInLightOrDarkMode(false)
        }

        // 错误提示按钮
        findViewById<MaterialButton>(R.id.btnPoptipError).setOnClickListener {
            PopTip.show(R.mipmap.skin, "邮件已发送", "撤回")
                .setAutoTintIconInLightOrDarkMode(false)
        }
        // 底部对话框按钮
        findViewById<MaterialButton>(R.id.btnBottomDialog).setOnClickListener {
            val s =  "你可以向下滑动来关闭这个对话框 or 点击空白区域或返回键来关闭这个对话框"
            BottomDialog("标题", "这里是对话框内容。\n$s。\n底部对话框也支持自定义布局扩展使用方式。").apply {
                setDialogLifecycleCallback(object : BottomDialogSlideEventLifecycleCallback<BottomDialog>() {
                    override fun onSlideClose(dialog: BottomDialog): Boolean {
                        return super.onSlideClose(dialog)
                    }

                    override fun onSlideTouchEvent(dialog: BottomDialog, v: View, event: MotionEvent): Boolean {
                        return super.onSlideTouchEvent(dialog, v, event)
                    }
                })
            }.show()
        }

        // 底部菜单按钮
        findViewById<MaterialButton>(R.id.btnBottomMenu).setOnClickListener {
            BottomMenu.show("添加", "查看", "编辑", "删除", "分享", "评论", "下载", "收藏", "赞！", "不喜欢")
                .setTitle("操作菜单")
                .setMessage("请选择要执行的操作")
                .setBottomDialogMaxHeight(0.6f)
                .setOnMenuItemClickListener { dialog, text, index ->
                    TipDialog.show(text.toString(), WaitDialog.TYPE.WARNING)
                    false
                }
                .setIconResIds(
                    R.mipmap.img_dialogx_demo_add,
                    R.mipmap.img_dialogx_demo_view,
                    R.mipmap.img_dialogx_demo_edit,
                    R.mipmap.img_dialogx_demo_delete,
                    R.mipmap.img_dialogx_demo_share,
                    R.mipmap.img_dialogx_demo_comment,
                    R.mipmap.img_dialogx_demo_download,
                    R.mipmap.img_dialogx_demo_favorite,
                    R.mipmap.img_dialogx_demo_good,
                    R.mipmap.img_dialogx_demo_dislike
                )
        }

        // 底部单选对话框
        findViewById<MaterialButton>(R.id.btnBottomSelectMenu).setOnClickListener {
            BottomMenu.show(singleSelectMenuText)
                .setShowSelectedBackgroundTips(true)
                .setMessage("这里是权限确认的文本说明，这是一个演示菜单")
                .setTitle("获得权限标题")
                .setOnMenuItemClickListener(object : OnMenuItemSelectListener<BottomMenu>() {
                    override fun onOneItemSelect(dialog: BottomMenu, text: CharSequence, index: Int, select: Boolean) {
                        selectMenuIndex = index
                    }
                })
                .setCancelButton("确定",
                    OnMenuButtonClickListener { _, _ ->
                        PopTip.show("已选择：${singleSelectMenuText[selectMenuIndex]}")
                        false
                    })
                .setSelection(selectMenuIndex)
        }

        // 底部多选对话框
        findViewById<MaterialButton>(R.id.btnBottomMultiSelectMenu).setOnClickListener {
            selectMenuIndexArray = intArrayOf(1,2)
            multiSelectMenuResultCache = " "
            MessageMenu.show(multiSelectMenuText)
                .setMessage("这里是选择城市的模拟范例，这是一个演示菜单")
                .setTitle("请选择城市")
                .setOnMenuItemClickListener(object : OnMenuItemSelectListener<MessageMenu>() {
                    override fun onMultiItemSelect(
                        dialog: MessageMenu,
                        text: Array<out CharSequence>,
                        indexArray: IntArray
                    ) {
                        for (c in text) {
                            multiSelectMenuResultCache = "$multiSelectMenuResultCache $c"
                        }
                        selectMenuIndexArray = indexArray
                    }
                })
                .setOkButton("确定",
                    OnMenuButtonClickListener { _, _ ->
                        PopTip.show("已选择：${multiSelectMenuResultCache}")
                        false
                    })
                .setSelection(selectMenuIndexArray)
        }

        // 底部自定义滑动布局 RecycleView
        findViewById<MaterialButton>(R.id.btnBottomCustomRecycleView).setOnClickListener {

        }


    }

    // 切换样式的方法
    private fun setMaterialStyle() {
        DialogX.globalStyle = MaterialStyle.style()
        DialogX.cancelButtonText = ""
        updateStyleInfo("当前样式：Material Design\n取消按钮文字：空")
    }

    private fun setIosStyle() {
        DialogX.globalStyle = IOSStyle.style()
        DialogX.cancelButtonText = "取消"
        updateStyleInfo("当前样式：iOS风格\n取消按钮文字：取消")
    }

    private fun setKongzueStyle() {
        DialogX.globalStyle = KongzueStyle.style()
        DialogX.cancelButtonText = "取消"
        updateStyleInfo("当前样式：Kongzue风格\n取消按钮文字：取消")
    }

    private fun setMiuiStyle() {
        DialogX.globalStyle = MIUIStyle.style()
        DialogX.cancelButtonText = "取消"
        updateStyleInfo("当前样式：MIUI风格\n取消按钮文字：取消")
    }

    private fun updateStyleInfo(text: String) {
        findViewById<TextView>(R.id.txtStyleInfo).text = text
    }
}