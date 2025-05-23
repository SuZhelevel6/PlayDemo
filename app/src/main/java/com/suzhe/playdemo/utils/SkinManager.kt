package com.suzhe.playdemo.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.suzhe.playdemo.R

enum class Skin {
    BLUE, BLACK, WHITE
}

object SkinManager {

    const val PREF_NAME = "skin_prefs"
    const val KEY_SKIN = "current_skin"

    private lateinit var currentSkin: Skin
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val savedSkinName = sharedPreferences.getString(KEY_SKIN, Skin.BLUE.name)
        currentSkin = Skin.valueOf(savedSkinName ?: Skin.BLUE.name)
    }

    fun setSkin(activity: Activity, skin: Skin) {
        currentSkin = skin
        sharedPreferences.edit().putString(KEY_SKIN, skin.name).apply()
        activity.recreate()
    }

    fun applySkin(activity: Activity) {
        activity.setTheme(getSkinThemeResId(currentSkin))
    }

    fun getCurrentSkin(): Skin {
        return currentSkin
    }

    private fun getSkinThemeResId(skin: Skin): Int {
        return when (skin) {
            Skin.BLUE -> R.style.AppTheme_Blue
            Skin.BLACK -> R.style.AppTheme_Black
            Skin.WHITE -> R.style.AppTheme_White
        }
    }
}
