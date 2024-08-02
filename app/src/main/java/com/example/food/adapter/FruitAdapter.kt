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
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport

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
                    dialog.setMessage("Want to delete this food?")
                    dialog.setPositiveButton("Yes") { dialog, which ->
                        list.remove(fruit)
//                        fruit.delete()
                        fruit?.let {
                            it.delete()
                        }
                        notifyDataSetChanged()
                        Toast.makeText(mActivity, "Deleted", Toast.LENGTH_LONG).show()
                        if (list != null && list.size > 0) {
                            rvfruitList!!.visibility = View.VISIBLE
                            llEmpty!!.visibility = View.GONE
                        } else {
                            rvfruitList!!.visibility = View.GONE
                            llEmpty!!.visibility = View.VISIBLE
                        }
                    }
                    dialog.setNeutralButton("Cancle") { dialog, which -> dialog.dismiss() }
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
        //If loading the first page, clear the data list first
        list!!.clear()
        if (listAdd != null) {
            //add data
            list.addAll(listAdd)
        }
        //Notify RecyclerView to make changes - overall
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
}
