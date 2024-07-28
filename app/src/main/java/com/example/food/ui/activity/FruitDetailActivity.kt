package com.example.food.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.food.R
import com.example.food.bean.Browse
import com.example.food.bean.Fruit
import com.example.food.bean.Orders
import com.example.food.util.SPUtils
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener
//import org.litepal.crud.DataSupport
import java.text.SimpleDateFormat
import java.util.Date
import org.litepal.LitePal

/**
 * 菜品明细信息
 */
class FruitDetailActivity : AppCompatActivity() {
    private var mActivity: Activity? = null
    private var ivImg: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvDate: TextView? = null
    private var tvContent: TextView? = null
    private var tvIssuer: TextView? = null
    private var btnCollect: Button? = null
    private var btnCancel: Button? = null
    private var mActionBar: ActionBar? = null //标题栏
    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        setContentView(R.layout.activity_fruit_detail)
        ivImg = findViewById(R.id.img)
        tvTitle = findViewById(R.id.title)
        tvDate = findViewById(R.id.date)
        tvContent = findViewById(R.id.content)
        tvIssuer = findViewById(R.id.issuer)
        btnCollect = findViewById(R.id.btn_collect)
        btnCancel = findViewById(R.id.btn_cancel)
        mActionBar = findViewById(R.id.myActionBar)
        //侧滑菜单
        mActionBar?.let{actionBar-> actionBar.setData(
            mActivity,
            "明细信息",
            R.drawable.ic_back,
            0,
            0,
            resources.getColor(R.color.colorPrimary),
            object : ActionBarClickListener {
                override fun onLeftClick() {
                    finish()
                }

                override fun onRightClick() {
                }
            })
        val fruit = intent.getSerializableExtra("fruit") as Fruit?
        tvTitle.setText(fruit!!.title)
        tvDate.setText(fruit.date)
        tvContent.setText(fruit.content)
        tvIssuer.setText(String.format("￥ %s", fruit.issuer))
        Glide.with(mActivity)
            .asBitmap()
            .skipMemoryCache(true)
            .load(fruit.img)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ivImg)
        val account = SPUtils.get(mActivity, SPUtils.ACCOUNT, "") as String
        val browse = LitePal.where("account = ? and title = ?", account, fruit.title).findFirst(
            Browse::class.java
        ) //浏览记录
        if (browse == null) { //不存在该条浏览记录  新增记录
            val browse1 = Browse(account, fruit.title)
            browse1.save()
        }
        val isAdmin = SPUtils.get(mActivity, SPUtils.IS_ADMIN, false) as Boolean
        if (!isAdmin) {
            val order =
                LitePal.where("account = ? and title = ?", account, fruit.title).findFirst(
                    Orders::class.java
                )
            btnCollect.setVisibility(if (order != null) View.GONE else View.VISIBLE)
            btnCancel.setVisibility(if (order != null) View.VISIBLE else View.GONE)
        }
        //收藏
        btnCollect.setOnClickListener(View.OnClickListener {
            val order = Orders(
                account, fruit.title, "S" + System.currentTimeMillis(), account, sf.format(
                    Date()
                )
            )
            order.save()
            Toast.makeText(mActivity, "点餐成功", Toast.LENGTH_SHORT).show()
            btnCollect.setVisibility(View.GONE)
            btnCancel.setVisibility(View.VISIBLE)
        })
        //取消收藏
        btnCancel.setOnClickListener(View.OnClickListener {
            val order =
                DataSupport.where("account = ? and title = ?", account, fruit.title).findFirst(
                    Orders::class.java
                )
            order.delete()
            Toast.makeText(mActivity, "取消成功", Toast.LENGTH_SHORT).show()
            btnCollect.setVisibility(View.VISIBLE)
            btnCancel.setVisibility(View.GONE)
        })
    }
}
