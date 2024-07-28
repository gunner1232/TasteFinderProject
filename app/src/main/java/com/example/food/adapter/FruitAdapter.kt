package com.example.food.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.food.R
import com.example.food.bean.Fruit
import com.example.food.util.SPUtils

class FruitAdapter(private val llEmpty: LinearLayout?, private val rvfruitList: RecyclerView?) :
    RecyclerView.Adapter<FruitAdapter.ViewHolder>() {
    private val list: MutableList<Fruit>? = ArrayList()
    private var mActivity: Context? = null
    private var mItemListener: ItemListener? = null
    fun setItemListener(itemListener: ItemListener?) {
        this.mItemListener = itemListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        mActivity = viewGroup.context
        val view =
            LayoutInflater.from(mActivity).inflate(R.layout.item_rv_fruit_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val fruit = list!![i]
        if (fruit != null) {
            viewHolder.title.text = fruit.title
            viewHolder.author_name.text = String.format("￥ %s", fruit.issuer)
            viewHolder.date.text = fruit.date
            Glide.with(mActivity!!)
                .asBitmap()
                .load(fruit.img)
                .error(R.drawable.ic_error)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(viewHolder.img)
            val isAdmin = SPUtils.get(mActivity, SPUtils.IS_ADMIN, false) as Boolean
            viewHolder.itemView.setOnClickListener {
                if (mItemListener != null) {
                    mItemListener!!.ItemClick(fruit)
                }
            }
            if (isAdmin) {
                viewHolder.itemView.setOnLongClickListener {
                    val dialog = AlertDialog.Builder(
                        mActivity!!
                    )
                    dialog.setMessage("确认要删除该美食吗")
                    dialog.setPositiveButton("确定") { dialog, which ->
                        list.remove(fruit)
//                        fruit.delete()
                        fruit?.let {
                            it.delete()
                        }
                        notifyDataSetChanged()
                        Toast.makeText(mActivity, "删除成功", Toast.LENGTH_LONG).show()
                        if (list != null && list.size > 0) {
                            rvfruitList!!.visibility = View.VISIBLE
                            llEmpty!!.visibility = View.GONE
                        } else {
                            rvfruitList!!.visibility = View.GONE
                            llEmpty!!.visibility = View.VISIBLE
                        }
                    }
                    dialog.setNeutralButton("取消") { dialog, which -> dialog.dismiss() }
                    dialog.show()
                    false
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    fun addItem(listAdd: List<Fruit>?) {
        //如果是加载第一页，需要先清空数据列表
        list!!.clear()
        if (listAdd != null) {
            //添加数据
            list.addAll(listAdd)
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val author_name: TextView = itemView.findViewById(R.id.author_name)
        val date: TextView = itemView.findViewById(R.id.date)
        val img: ImageView = itemView.findViewById(R.id.img)
    }

    interface ItemListener {
        fun ItemClick(fruit: Fruit?)
    }
    private fun deleteFruitMember(fruit: Fruit) {
        // 将成员设为默认值或 null
        fruit.name = null // 如果成员允许为 null
        // 或者 fruit.name = "" // 如果成员不允许为 null，可以设为空字符串

        // 更新数据库中的对象
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                fruit.save()
            }
        }
    }
}
