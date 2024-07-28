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
import com.example.food.R
import com.example.food.bean.Browse
import com.example.food.bean.User
import com.example.food.ui.activity.UserDetailActivity
import com.example.food.util.SPUtils
import org.litepal.crud.DataSupport

class UserAdapter(private val llEmpty: LinearLayout?, private val rvUserList: RecyclerView?) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private val list: MutableList<User?>? = ArrayList()
    private var mActivity: Context? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        mActivity = viewGroup.context
        val view =
            LayoutInflater.from(mActivity).inflate(R.layout.item_rv_user_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val user = list!![i]
        if (user != null) {
            val account = SPUtils.get(mActivity, SPUtils.ACCOUNT, "") as String
            viewHolder.itemView.visibility =
                if (account == user.account) View.GONE else View.VISIBLE
            viewHolder.nickName.text = user.nickName
            viewHolder.itemView.setOnClickListener {
                val intent = Intent(mActivity, UserDetailActivity::class.java)
                intent.putExtra("user", user)
                mActivity!!.startActivity(intent)
            }
            viewHolder.itemView.setOnLongClickListener {
                val dialog = AlertDialog.Builder(
                    mActivity!!
                )
                dialog.setMessage("确认要删除该用户吗")
                dialog.setPositiveButton("确定") { dialog, which -> //删除收藏记录和浏览记录
                    val browses = DataSupport.where("account = ?", user.account).find(
                        Browse::class.java
                    )
                    if (browses != null && browses.size > 0) {
                        for (browse in browses) {
                            browse.delete()
                        }
                    }
                    list.remove(user)
                    user.delete()
                    notifyDataSetChanged()
                    Toast.makeText(mActivity, "删除成功", Toast.LENGTH_LONG).show()
                    if (list != null && list.size > 1) {
                        rvUserList!!.visibility = View.VISIBLE
                        llEmpty!!.visibility = View.GONE
                    } else {
                        rvUserList!!.visibility = View.GONE
                        llEmpty!!.visibility = View.VISIBLE
                    }
                }
                dialog.setNeutralButton("取消") { dialog, which -> dialog.dismiss() }
                dialog.show()
                false
            }
        }
    }

    fun addItem(listAdd: List<User?>?) {
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
    }
}
