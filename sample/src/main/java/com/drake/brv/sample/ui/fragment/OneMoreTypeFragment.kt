package com.drake.brv.sample.ui.fragment

import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import com.drake.brv.listener.ItemDifferCallback
import com.drake.brv.sample.R
import com.drake.brv.sample.databinding.FragmentOneMoreTypeBinding
import com.drake.brv.sample.databinding.ItemOneMore1Binding
import com.drake.brv.sample.databinding.ItemOneMore2Binding
import com.drake.brv.sample.databinding.ItemOneMore3Binding
import com.drake.brv.sample.model.OneMoreModel1
import com.drake.brv.sample.utils.TestItemDifferCallback
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.linear
import com.drake.brv.utils.setDifferModels
import com.drake.brv.utils.setup
import com.drake.engine.base.EngineFragment
import com.drake.tooltip.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OneMoreTypeFragment :
    EngineFragment<FragmentOneMoreTypeBinding>(R.layout.fragment_one_more_type) {
    //在 Kotlin 中，如果属性的类型是一个类，并且该类有无参构造函数，那么在属性声明中可以省略括号 ().
    // var testItemDifferCallback: TestItemDifferCallback = TestItemDifferCallback()
    // 等价于下面
    var testItemDifferCallback: TestItemDifferCallback = TestItemDifferCallback
    var list: MutableList<OneMoreModel1> = mutableListOf()
    override fun initView() {
        testItemDifferCallback.areItemsTheSame(1, 1)
        repeat(6) {
            list.add(OneMoreModel1(it, 3, "-q--$it"))
        }
        binding.rv.grid(3).setup {
            addType<OneMoreModel1> {
                when (type) {
                    1 -> R.layout.item_one_more1
                    2 -> R.layout.item_one_more2
                    else -> R.layout.item_one_more3
                }
            }
//            addHeader(OneMoreModel1(-1,1, "head"))
//            addFooter(OneMoreModel1(-2,2, "foot"))
            itemDifferCallback = object : ItemDifferCallback {
                //判断两个项目是否是同一个项目，通常通过唯一标识符（如 ID）来比较。
                override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                    if (oldItem is OneMoreModel1 && newItem is OneMoreModel1) {
                        return oldItem.id == newItem.id // 比较 id 字段
                    }
                    return false
                }

                //判断两个项目的内容是否相同，通常需要比较所有相关字段。
                override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                    if (oldItem is OneMoreModel1 && newItem is OneMoreModel1) {
                        return oldItem == newItem // 默认比较所有字段
                    }
                    return false
                }

                override fun getChangePayload(oldItem: Any, newItem: Any): Any? {
                    if (oldItem is OneMoreModel1 && newItem is OneMoreModel1) {
                        // 只返回改变的字段
//                        if (oldItem.txt != newItem.txt) {
//                            return newItem.txt
//                        }
                        if (oldItem != newItem) {
                            return newItem
                        }
                    }
                    return null
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

            onPayload {
                Log.i("onPayload", it.toString() + "type $itemViewType")
                when (itemViewType) {
                    else -> {
                        getBinding<ItemOneMore3Binding>().apply {
                            if (it.isNotEmpty()) {
                                item.text = (it[0] as OneMoreModel1).txt
                            }
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
            newList.add(OneMoreModel1(-2, 2, "foot"))
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
//                val b = filter { it.id == 0 }[0]
//                val i = indexOf(b)
//                this[i] = b.copy(txt = "改名后的小帅哥$ids")
                //todo 如果不使用。onPayload，局部更新某个走onBind，还是会闪的。因为局部走onBing更新
                // 走onPayload就不会，复用当前的bind.直接刷新
                forEachIndexed { index, oneMoreModel1 ->
                    if (index > 0 && index < size - 1) {
                        this[index] = oneMoreModel1.copy(
                            id = oneMoreModel1.id,
                            type = oneMoreModel1.type, txt = "呜呜呜呜$index"
                        )
                    }
                }
            }
            binding.rv.setDifferModels(newModels = newList)
            list = newList
        }

        binding.titleTv.setOnClickListener {
            ids++
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
            binding.rv.setDifferModels(newModels = newList, commitCallback = {

            })
            list = newList
//            ADD()
        }


    }


    /**
     * todo DiffUtil 负责识别数据变化，但不负责处理重复数据。
     *      你需要在添加新 item 之前，手动检查重复数据。
     */
    private fun ADD() {
        ids++
        val newData: MutableList<OneMoreModel1> = ArrayList(list) // 从现有列表创建副本

        val newItems = listOf(
            OneMoreModel1(5, 3, "新增$ids"),
            OneMoreModel1(6, 3, "新增$ids"),
            OneMoreModel1(7, 3, "新增$ids")
        )

        // 仅添加不存在的项目
        for (newItem in newItems) {
            if (!newData.contains(newItem)) { // 使用 contains 方法检查是否存在
                newData.add(newItem)
            }
        }

        // 使用 DiffUtil 计算差异
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallBack(newData, list))
        // 更新列表
        list.clear()
        list.addAll(newData)
        // 将新数据提交给适配器
        diffResult.dispatchUpdatesTo(binding.rv.bindingAdapter)
    }

    class DiffUtilCallBack(
        private val newlist: List<OneMoreModel1>,
        private val oldlist: List<OneMoreModel1>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldlist.size
        }

        override fun getNewListSize(): Int {
            return newlist.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            //判断是否是同一个item，可以在这里处理
            // 判断是否是相同item的逻辑，比如id之类的
            return newlist[newItemPosition].id == oldlist[oldItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            //判断数据是否发生改变，这个  方法会在上面的方法返回true时调用，
            // 因为虽然item是同一个，但有可能item的数据发生了改变
            return newlist[newItemPosition] == oldlist[oldItemPosition]
        }
    }


    var ids = 99

    override fun initData() {
    }

}
