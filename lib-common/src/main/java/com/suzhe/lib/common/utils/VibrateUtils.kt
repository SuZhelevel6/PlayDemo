package com.suzhe.lib.common.utils

import android.Manifest
import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission

/**
 * 震动
 */
@RequiresPermission(Manifest.permission.VIBRATE)
fun Context.vibrate() {
    if (Build.VERSION.SDK_INT >= 31) {
        val manager: VibratorManager = getSystemService(VibratorManager::class.java)
        manager.defaultVibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
    } else if (Build.VERSION.SDK_INT >= 29) {
        val vib = getSystemService(Vibrator::class.java) as Vibrator
        vib.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
    } else {
        val vib = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        vib.vibrate(70)
    }
}
