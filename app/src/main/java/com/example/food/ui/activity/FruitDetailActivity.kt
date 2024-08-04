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
 * Detailed information of dishes
 */
class FruitDetailActivity : AppCompatActivity() {
    private lateinit var ivImg: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvContent: TextView
    private lateinit var tvIssuer: TextView
    private lateinit var btnCollect: Button
    private lateinit var btnCancel: Button
    private lateinit var mActionBar: ActionBar
    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        ivImg = findViewById(R.id.img)
        tvTitle = findViewById(R.id.title)
        tvDate = findViewById(R.id.date)
        tvContent = findViewById(R.id.content)
        tvIssuer = findViewById(R.id.issuer)
        btnCollect = findViewById(R.id.btn_collect)
        btnCancel = findViewById(R.id.btn_cancel)
        mActionBar = findViewById(R.id.myActionBar)

        mActionBar.setData(
            this,
            "Detailed information",
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

        val fruit = intent.getSerializableExtra("fruit") as? Fruit
        if (fruit != null) {
            tvTitle.text = fruit.title
            tvDate.text = fruit.date
            tvContent.text = fruit.content
            tvIssuer.text = String.format("$ %s", fruit.issuer)

            Glide.with(this)
                .asBitmap()
                .skipMemoryCache(true)
                .load(fruit.img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImg)

            val account = SPUtils.get(this, SPUtils.ACCOUNT, "") as String
            val browse = LitePal.where("account = ? and title = ?", account, fruit.title).findFirst(Browse::class.java)
            if (browse == null) {
                val browse1 = Browse(account, fruit.title)
                browse1.save()
            }

            val isAdmin = SPUtils.get(this, SPUtils.IS_ADMIN, false) as Boolean
            if (!isAdmin) {
                val order = LitePal.where("account = ? and title = ?", account, fruit.title).findFirst(Orders::class.java)
                btnCollect.visibility = if (order != null) View.GONE else View.VISIBLE
                btnCancel.visibility = if (order != null) View.VISIBLE else View.GONE
            }

            btnCollect.setOnClickListener {
                val order = Orders(account, fruit.title, "S" + System.currentTimeMillis(), account, sf.format(Date()))
                order.save()
                Toast.makeText(this, "Ordered", Toast.LENGTH_SHORT).show()
                btnCollect.visibility = View.GONE
                btnCancel.visibility = View.VISIBLE
            }

            btnCancel.setOnClickListener {
                val order = LitePal.where("account = ? and title = ?", account, fruit.title).findFirst(Orders::class.java)
                order?.delete()
                Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show()
                btnCollect.visibility = View.VISIBLE
                btnCancel.visibility = View.GONE
            }
        } else {
            Toast.makeText(this, "Unable to load details", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}