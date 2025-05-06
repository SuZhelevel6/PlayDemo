package com.suzhe.playdemo.component.brvah.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.Interpolator

import com.chad.library.adapter4.animation.ItemAnimator

import java.lang.Math.PI
import kotlin.math.pow

// 创建一个自定义的动画实现类，继承自 ItemAnimator 接口
class CustomAnimation2 : ItemAnimator {

    // 覆写 animator 方法，创建并返回动画对象
    override fun animator(view: View): Animator {
        // 创建平移动画，让视图从屏幕左侧滑入
        val translationXAnimator = ObjectAnimator.ofFloat(
            view,                 // 目标视图
            "translationX",       // 动画属性（横向平移）
            -view.rootView.width.toFloat(), // 起始值（屏幕左边宽度处）
            0f                    // 结束值（原点位置）
        )

        // 设置动画持续时间（800 毫秒）
        translationXAnimator.duration = 800

        // 设置自定义插值器，控制动画运动曲线
        translationXAnimator.interpolator = MyInterpolator2()

        // 返回创建的动画对象
        return translationXAnimator
    }

    // 内部自定义插值器类
    private inner class MyInterpolator2 : Interpolator {

        // 实现插值器接口的 getInterpolation 方法
        override fun getInterpolation(input: Float): Float {
            // 定义震荡控制参数（0.7-1 是常见动画参数范围）
            val factor = 0.7f

            // 计算带有指数衰减的正弦波插值公式
            // 返回值 = 指数衰减因子 × 正弦波 + 抵消初相位的修正值
            return (2.0.pow(-10.0 * input.toDouble()) *
                    kotlin.math.sin((input - factor / 4) * (2 * PI) / factor) + 1).toFloat()
        }
    }
}