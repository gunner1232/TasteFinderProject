package com.example.food.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.food.R
import com.example.food.bean.Fruit
import com.example.food.bean.Orders
import com.example.food.bean.User
import com.example.food.ui.activity.FruitDetailActivity
import com.example.food.util.SPUtils
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport

class OrderAdapter(private val llEmpty: LinearLayout?, private val rvOrderList: RecyclerView?) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private val list: MutableList<Orders?>? = ArrayList()
    private var mActivity: Context? = null
    private val headerRO = RequestOptions().circleCrop() //圆角变换
    private var mItemListener: ItemListener? = null
    fun setItemListener(itemListener: ItemListener?) {
        this.mItemListener = itemListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        mActivity = viewGroup.context
        val view =
            LayoutInflater.from(mActivity).inflate(R.layout.item_rv_order_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val order = list!![i]
        val user = LitePal.where("account = ? ", order?.account).findFirst(
            User::class.java
        )
        if (order != null && user != null) {
            viewHolder.nickName.text = String.format("用户：%s", user.nickName)
            viewHolder.number.text = order.number
            viewHolder.date.text = order.date
            viewHolder.itemView.setOnClickListener {
                val intent = Intent(mActivity, FruitDetailActivity::class.java)
                val fruit = LitePal.where("title = ?", order.title).findFirst(
                    Fruit::class.java
                )
                intent.putExtra("fruit", fruit)
                mActivity!!.startActivity(intent)
            }
            val isAdmin = SPUtils.get(mActivity, SPUtils.IS_ADMIN, false) as Boolean
            if (isAdmin) {
                viewHolder.itemView.setOnLongClickListener {
                    val dialog = AlertDialog.Builder(
                        mActivity!!
                    )
                    dialog.setMessage("确认要删除该订单吗")
                    dialog.setPositiveButton("确定") { dialog, which ->
                        list.remove(order)
                        order.delete()
                        notifyDataSetChanged()
                        Toast.makeText(mActivity, "删除成功", Toast.LENGTH_LONG).show()
                        if (list != null && list.size > 0) {
                            rvOrderList!!.visibility = View.VISIBLE
                            llEmpty!!.visibility = View.GONE
                        } else {
                            rvOrderList!!.visibility = View.GONE
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

    fun addItem(listAdd: List<Orders?>?) {
        //如果是加载第一页，需要先清空数据列表
        list!!.clear()
        if (listAdd != null) {
            //添加数据
            list.addAll(listAdd)
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nickName: TextView = itemView.findViewById(R.id.nickName)
        val date: TextView = itemView.findViewById(R.id.date)
        val number: TextView = itemView.findViewById(R.id.number)
    }

    interface ItemListener {
        fun ItemClick(order: Orders?)
    }
}
