package com.suzhe.playdemo.data

object DataServer {

    // 模拟获取包含 100 条数据的 ArrayList
    fun getStringItems(): ArrayList<String> {
        val itemList = ArrayList<String>()
        for (i in 1..100) {
            itemList.add("Item" + i)
        }
        return itemList
    }

}