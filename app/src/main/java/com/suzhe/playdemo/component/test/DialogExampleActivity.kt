package com.suzhe.playdemo.component.test

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.MessageMenu
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.dialogs.WaitDialog
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener
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
            val singleSelectMenuText = arrayOf("拒绝", "询问", "始终允许", "仅在使用中允许")
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
            val multiSelectMenuText = arrayOf("上海", "北京", "广州", "深圳")
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