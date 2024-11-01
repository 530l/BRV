package com.drake.brv.sample.ui.fragment

import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.sample.R
import com.drake.brv.sample.databinding.FragmentOneMoreTypeBinding
import com.drake.brv.sample.databinding.ItemOneMore1Binding
import com.drake.brv.sample.databinding.ItemOneMore2Binding
import com.drake.brv.sample.databinding.ItemOneMore3Binding
import com.drake.brv.sample.model.OneMoreModel
import com.drake.brv.sample.model.OneMoreModel1
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setDifferModels
import com.drake.brv.utils.setup
import com.drake.engine.base.EngineFragment


class OneMoreTypeFragment :
    EngineFragment<FragmentOneMoreTypeBinding>(R.layout.fragment_one_more_type) {

    var list: MutableList<OneMoreModel1> = mutableListOf()
    override fun initView() {
        repeat(2) {
            list.add(OneMoreModel1(it, 3, "-q--$it"))
        }
        list.add(OneMoreModel1(-1, 1, "head"))
        list.add(OneMoreModel1(-2, 2, "foot"))
        binding.rv.linear().setup {
            addType<OneMoreModel1> {
                when (type) {
                    1 -> R.layout.item_one_more1
                    2 -> R.layout.item_one_more2
                    else -> R.layout.item_one_more3
                }
            }
            onBind {
                when (itemViewType) {
                    R.layout.item_one_more1 -> {
                        getBinding<ItemOneMore1Binding>().apply {
                            item.text = getModel<OneMoreModel1>().txt
                        }
                    }

                    R.layout.item_one_more2 -> {
                        getBinding<ItemOneMore2Binding>().apply {
                            item.text = getModel<OneMoreModel1>().txt
                        }
                    }

                    else -> {
                        getBinding<ItemOneMore3Binding>().apply {
                            item.text = getModel<OneMoreModel1>().txt
                        }
                    }
                }
            }


        }.models = list


        //todo 如果用这种方法添加头部，则存在问题？索引
//        binding.rv.run {
//            binding.rv.bindingAdapter.addHeader(OneMoreModel1(-1,1, "head"))
//            binding.rv.bindingAdapter.addFooter(OneMoreModel1(-2,2, "foot"))
//        }

        binding.titleTv2.setOnClickListener {
            val newList = list.toMutableList().apply {
                //更新
//                this[0] = this[0].copy(txt = "改名后的小帅哥$ids")
                this[0] = filter { it.id == 0 }[0].copy(txt = "改名后的小帅哥$ids")
            }
            binding.rv.setDifferModels(newModels = newList)
            list = newList

        }

        binding.titleTv.setOnClickListener {
            val newList = list.toMutableList().apply {
                //删除
                //removeAt(2)
                //新增
                add(list.size - 1, OneMoreModel1(ids++, 1, "新增$ids"))
            }

            binding.rv.setDifferModels(newModels = newList)
            list = newList
        }


        //列表清空
        //list.clear()
        //binding.rv.setDifferModels(list)

    }

    var ids = 99

    override fun initData() {
    }

}
