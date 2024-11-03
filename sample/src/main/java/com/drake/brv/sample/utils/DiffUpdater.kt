//package com.drake.brv.sample
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.drake.brv.sample.DiffUpdater.dispatch
//import kotlin.reflect.KClass
//import kotlin.reflect.KProperty1
//import kotlin.reflect.full.memberProperties
//
//@Suppress("UNCHECKED_CAST")
//object DiffUpdater {
//
//    data class Payload<T>(
//        val newState: T,
//        val changed: List<KProperty1<T, *>>,
//    )
//
//    class Binder<T, VH : RecyclerView.ViewHolder> {
//        val handlers = mutableMapOf<KProperty1<T, *>, (VH, Any?) -> Unit>()
//
//        infix fun <R> KProperty1<T, R>.onChange(action: (R) -> Unit) {
//            handlers[this] = action as (VH, Any?) -> Unit
//        }
//    }
//
//    fun <T, VH : RecyclerView.ViewHolder> Payload<T>.dispatch(vh: VH, block: Binder<T,VH>.() -> Unit) {
//        val binder = Binder<T,VH>()
//        block(binder)
//        return doUpdate(vh,this, binder.handlers)
//    }
//
//    inline fun <reified T> createPayload(lh: T, rh: T): Payload<T> {
//        val clz = T::class as KClass<Any>
//        val changed: List<KProperty1<Any, *>> = clz.memberProperties.filter {
//            it.get(lh as Any) != it.get(rh as Any)
//        }
//        return Payload(rh, changed as List<KProperty1<T, *>>)
//    }
//
//    private fun <T,VH : RecyclerView.ViewHolder> doUpdate(
//        vh: VH,
//        payload: Payload<T>,
//        handlers: Map<KProperty1<T, *>, (VH,Any?) -> Unit>,
//    ) {
//        val (state, changedProps) = payload
//        for (prop in changedProps) {
//            val handler = handlers[prop]
//            if (handler == null) {
//                print("not handle with ${prop.name} change.")
//                continue
//            }
//            val newValue = prop.get(state)
//            handler(vh,newValue)
//        }
//    }
//}
//
//data class Item(val name: String, val age: Int)
//
//
//class MyViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
//    val nameTextView = itemView.findViewById<android.widget.TextView>(R.id.item)
//    val ageTextView = itemView.findViewById<android.widget.TextView>(R.id.item2)
//}
//
//class DiffUpdaterActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: MyAdapter
//
//    @SuppressLint("NotifyDataSetChanged")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_one_more_type)
//
//        recyclerView = findViewById(R.id.rv)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = MyAdapter()
//        recyclerView.adapter = adapter
//
//        // 初始数据
//        adapter.items = listOf(
//            Item("Alice", 20),
//            Item("Bob", 25),
//            Item("Charlie", 30)
//        ).toMutableList()
//        adapter.notifyDataSetChanged()
//
//        // 模拟数据更新
//        // 延迟 2 秒后更新第二个 Item 的年龄
//        Handler(Looper.getMainLooper()).postDelayed({
//            adapter.items = adapter.items.toMutableList().apply {
//                this[1] = this[1].copy(age = 26)
//            }
//            adapter.notifyItemChanged(1)
//        }, 2000)
//    }
//}
//
//class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
//
//    var items = mutableListOf<Item>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_one_more1, parent, false)
//        return MyViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val item = items[position]
//        val payload = DiffUpdater.createPayload(items[position], item)
//
//        payload.dispatch(holder) {
//            Item::name onChange { newName ->
//                holder.nameTextView.text = newName
//            }
//            Item::age onChange { newAge ->
//                holder.ageTextView.text = newAge.toString()
//            }
//        }
//    }
//
//    override fun getItemCount(): Int = items.size
//}