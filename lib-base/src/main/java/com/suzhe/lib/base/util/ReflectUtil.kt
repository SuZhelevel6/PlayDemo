package com.suzhe.lib.base.util

import android.view.LayoutInflater
import java.lang.reflect.ParameterizedType

/**
 * 反射工具类
 * 用于 ViewBinding 的自动创建
 */
object ReflectUtil {

    /**
     * 通过反射创建 ViewBinding 实例
     *
     * @param layoutInflater 布局填充器
     * @param clazz 持有 ViewBinding 泛型参数的类
     * @return ViewBinding 实例
     */
    @Suppress("UNCHECKED_CAST")
    fun <VB> newViewBinding(layoutInflater: LayoutInflater, clazz: Class<*>): VB {
        return try {
            // 获取泛型参数对象
            val type = try {
                clazz.genericSuperclass as ParameterizedType
            } catch (e: ClassCastException) {
                clazz.superclass.genericSuperclass as ParameterizedType
            }

            // type.actualTypeArguments[0]: ViewBinding
            val clazzVB = type.actualTypeArguments[0] as Class<VB>

            // 获取 inflate 方法
            val inflateMethod = clazzVB.getMethod("inflate", LayoutInflater::class.java)
            inflateMethod.invoke(null, layoutInflater) as VB
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to create ViewBinding instance", e)
        }
    }
}
