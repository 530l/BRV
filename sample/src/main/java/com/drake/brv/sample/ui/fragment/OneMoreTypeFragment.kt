package com.drake.brv.sample.ui.fragment

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

    val list: MutableList<OneMoreModel1> = mutableListOf()
    override fun initView() {
        repeat(3) {
            list.add(OneMoreModel1(3, "-q--$it"))
        }
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

        binding.rv.run {
            binding.rv.bindingAdapter.addHeader(OneMoreModel1(1, "head"))
            binding.rv.bindingAdapter.addFooter(OneMoreModel1(2, "foot"))
        }


        binding.titleTv.setOnClickListener {
            val newList = mutableListOf<OneMoreModel1>()
            newList.addAll(list)
            newList.add(OneMoreModel1(3, "-q--99999"))
            binding.rv.setDifferModels(newModels = newList)
            list.clear()
            list.addAll(newList)
        }

    }


    override fun initData() {
    }

}
