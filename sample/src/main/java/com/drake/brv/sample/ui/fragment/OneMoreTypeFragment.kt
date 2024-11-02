package com.drake.brv.sample.ui.fragment

import android.util.Log
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OneMoreTypeFragment :
    EngineFragment<FragmentOneMoreTypeBinding>(R.layout.fragment_one_more_type) {

    var list: MutableList<OneMoreModel1> = mutableListOf()
    override fun initView() {
        repeat(2) {
            list.add(OneMoreModel1(it, 3, "-q--$it"))
        }
        binding.rv.linear().setup {
            addType<OneMoreModel1> {
                when (type) {
                    1 -> R.layout.item_one_more1
                    2 -> R.layout.item_one_more2
                    else -> R.layout.item_one_more3
                }
            }
//            itemDifferCallback = object : ItemDifferCallback {
//                //判断两个项目是否是同一个项目，通常通过唯一标识符（如 ID）来比较。
//                override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
//                    Log.i(
//                        "ItemDifferCallback",
//                        "areItemsTheSame  ${(oldItem as OneMoreModel1)}  ${(newItem as OneMoreModel1)}"
//                    )
//                    return oldItem.id == (newItem.id)
//                }
//                //判断两个项目的内容是否相同，通常需要比较所有相关字段。
//                override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
//                    Log.i(
//                        "ItemDifferCallback",
//                        "areContentsTheSame  ${(oldItem as OneMoreModel1)}  ${(newItem as OneMoreModel1)}"
//                    )
//                    return oldItem == newItem
//                }
//
//                override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
//                    return super.getChangePayload(oldItem, newItem)
//                }
//            }
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

        //todo 目数据对比默认使用`equals`函数对比,
        //     你可以为数据手动实现equals函数来修改对比逻辑.
        //     推荐定义数据为 data class, 因其会根据构造参数自动生成equals

        //todo 如果用这种方法添加头部，则存在问题？索引
//        binding.rv.run {
//            binding.rv.bindingAdapter.addHeader(OneMoreModel1(-1,1, "head"))
//            binding.rv.bindingAdapter.addFooter(OneMoreModel1(-2,2, "foot"))
//        }

        binding.rv.run {
            val newList = list.toMutableList().apply {}
            newList.add(0, OneMoreModel1(-1, 1, "head"))
            newList. add(OneMoreModel1(-2, 2, "foot"))
            binding.rv.setDifferModels(newModels = newList)
            list = newList
        }

        binding.titleTv3.setOnClickListener {
            val newList = list.toMutableList().apply {
                //removeAt(1) //todo 但是需要注意头尾。
                //删除
                filter { it.id > 0 }.forEach {
                    remove(it)
                }
            }
            binding.rv.setDifferModels(newModels = newList)
            list = newList
        }
        //todo setDifferModels 每次传入的都是新对象，新旧对比
        binding.titleTv4.setOnClickListener {
            //列表清空
            val newList = mutableListOf<OneMoreModel1>()
            binding.rv.setDifferModels(newList)
            list = newList
            ids = 0
            //重新新增。
            lifecycleScope.launch {
                delay(2000)
                val newList2 = mutableListOf<OneMoreModel1>()
                repeat(5) {
                    newList2.add(OneMoreModel1(it, 3, "-q--$it"))
                }
                newList2.add(0, OneMoreModel1(-1, 1, "head"))
                newList2.add(OneMoreModel1(-2, 2, "foot"))
                binding.rv.setDifferModels(newModels = newList2)
                list = newList2
            }
        }

        binding.titleTv2.setOnClickListener {
            val newList = list.toMutableList().apply {
                //todo 这种方式直接通过索引。但是需要注意头尾。
                //this[0] = this[0].copy(txt = "改名后的小帅哥$ids")
                //todo 这种方式是通过遍历id删除。但是需要注意头尾。
                val b = filter { it.id == 0 }[0]
                val i = indexOf(b)
                this[i] = b.copy(txt = "改名后的小帅哥$ids")
            }
            binding.rv.setDifferModels(newModels = newList)
            list = newList

        }

        binding.titleTv.setOnClickListener {
            val newList = list.toMutableList().apply {
                //新增
                // 检查列表是否至少有两个元素
                if (size >= 2) {
                    // 将新项添加到倒数第二个位置
                    add(list.size - 1, OneMoreModel1(ids, 3, "新增$ids"))
                } else {
                    // 如果列表少于两个元素，直接添加到列表末尾
                    add(list.size - 1, OneMoreModel1(ids, 3, "新增$ids"))
                }

            }

            binding.rv.setDifferModels(newModels = newList)
            list = newList
        }


    }

    var ids = 99

    override fun initData() {
    }

}
