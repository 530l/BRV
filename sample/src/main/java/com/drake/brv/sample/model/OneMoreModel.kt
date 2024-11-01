package com.drake.brv.sample.model

import com.drake.brv.item.ItemPosition

data class OneMoreModel(var type: Int, override var itemPosition: Int = 0) : ItemPosition

data class OneMoreModel1(var id: Int, var type: Int, var txt: String) {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true // 如果是同一实例
//        if (other !is OneMoreModel1) return false // 类型检查
////        return id == other.id && type == other.type && txt == other.txt // 比较属性
//        return id == other.id // 只比较 id
//
//    }
//
//    override fun hashCode(): Int {
//        var result = id
//        result = 31 * result + type
//        result = 31 * result + txt.hashCode()
//        return result
//    }


}
