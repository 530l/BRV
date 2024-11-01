package com.drake.brv.sample.model

import com.drake.brv.item.ItemPosition

data class OneMoreModel(var type: Int, override var itemPosition: Int = 0) : ItemPosition

data class OneMoreModel1(var type: Int, val  txt:String)