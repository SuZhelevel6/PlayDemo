package com.suzhe.playdemo.component.brvah

data class BRVAHEntity (
    val name: String = "",
    val activity: Class<*>? = null,
    val imageResource: Int = 0,
    val sectionTitle: String = ""
    ) {
        val isSection: Boolean
        get() = sectionTitle.isNotBlank()
    }