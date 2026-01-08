package com.suzhe.feature.brvah.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.chad.library.adapter4.animation.ItemAnimator

class CustomAnimation1 : ItemAnimator {

    override fun animator(view: View): Animator {
        // 创建一个透明度动画，从完全透明到完全不透明
        val alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)

        // 创建一个纵向缩放动画，从 1.3 倍缩放到正常尺寸
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1f)

        // 创建一个横向缩放动画，从 1.3 倍缩放到正常尺寸
        val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1f)

        // set interpolator for scale animations
        scaleYAnimator.interpolator = DecelerateInterpolator()
        scaleXAnimator.interpolator = DecelerateInterpolator()

        // 创建一个动画集，用于组合多个动画
        val animatorSet = AnimatorSet()

        // 设置动画持续时间（350 毫秒）
        animatorSet.duration = 350

        // 在动画集中同时播放透明度动画、纵向缩放动画和横向缩放动画
        animatorSet.play(alphaAnimator).with(scaleXAnimator).with(scaleYAnimator)

        // 返回创建的动画集
        return animatorSet
    }
}