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
import com.example.food.bean.Food
import com.example.food.bean.Orders
import com.example.food.bean.User
import com.example.food.ui.activity.FoodDetailActivity
import com.example.food.util.SPUtils
import org.litepal.LitePal

class OrderAdapter(private val llEmpty: LinearLayout?, private val rvOrderList: RecyclerView?) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private val list: MutableList<Orders?>? = ArrayList()
    private var mActivity: Context? = null
    private val headerRO = RequestOptions().circleCrop() //Round corner transformation
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
            viewHolder.nickName.text = String.format("Nickname：%s", user.nickName)
            viewHolder.number.text = order.number
            viewHolder.date.text = order.date
            viewHolder.itemView.setOnClickListener {
                val intent = Intent(mActivity, FoodDetailActivity::class.java)
                val food = LitePal.where("title = ?", order.title).findFirst(
                    Food::class.java
                )
                intent.putExtra("fruit", food)
                mActivity!!.startActivity(intent)
            }
            val isAdmin = SPUtils.get(mActivity, SPUtils.IS_ADMIN, false) as Boolean
            if (isAdmin) {
                viewHolder.itemView.setOnLongClickListener {
                    val dialog = AlertDialog.Builder(
                        mActivity!!
                    )
                    dialog.setMessage("Want to delete this order?")
                    dialog.setPositiveButton("Yes") { dialog, which ->
                        list.remove(order)
                        order.delete()
                        notifyDataSetChanged()
                        Toast.makeText(mActivity, "Deleted", Toast.LENGTH_LONG).show()
                        if (list != null && list.size > 0) {
                            rvOrderList!!.visibility = View.VISIBLE
                            llEmpty!!.visibility = View.GONE
                        } else {
                            rvOrderList!!.visibility = View.GONE
                            llEmpty!!.visibility = View.VISIBLE
                        }
                    }
                    dialog.setNeutralButton("Cancel") { dialog, which -> dialog.dismiss() }
                    dialog.show()
                    false
                }
            }
        }
    }

    fun addItem(listAdd: List<Orders?>?) {
        //If loading the first page, clear the data list first
        list!!.clear()
        if (listAdd != null) {
            //add data
            list.addAll(listAdd)
        }
        //Notify RecyclerView to make changes - overall
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
