package com.suzhe.playdemo.data

object DataServer {

    // 模拟获取包含 100 条数据的 ArrayList
    fun getStringItems100(): ArrayList<String> {
        val itemList = ArrayList<String>()
        for (i in 1..100) {
            itemList.add("Item" + i)
        }
        return itemList
    }

    fun getStringItems5(): ArrayList<String> {
        val itemList = ArrayList<String>()
        for (i in 1..5) {
            itemList.add("Item" + i)
        }
        return itemList
    }

    fun getStringItems(num: Int): ArrayList<String> {
        val itemList = ArrayList<String>()
        for (i in 1..num) {
            itemList.add("Item" + i)
        }
        return itemList
    }

}