package com.example.food.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.food.R
import com.example.food.bean.Fruit
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener
import org.litepal.crud.LitePalSupport
import org.litepal.LitePal
import java.text.SimpleDateFormat
import java.util.Date

/**
 * 添加页面
 */
class AddFruitActivity : AppCompatActivity() {
    private var mActionBar: ActionBar? = null //标题栏
    private var myActivity: Activity? = null
    private var etTitle: EditText? = null //标题
    private var etIssuer: EditText? = null //发布单位
    private var etImg: EditText? = null //图片
    private var spType: Spinner? = null //类型
    private var etContent: EditText? = null //内容
    private var ivImg: ImageView? = null //图片
    var sf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private var mfruit: Fruit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myActivity = this
        setContentView(R.layout.activity_fruit_add)
        etTitle = findViewById(R.id.title)
        etIssuer = findViewById(R.id.issuer)
        spType = findViewById(R.id.type)
        etImg = findViewById(R.id.img)
        etContent = findViewById(R.id.content)
        ivImg = findViewById(R.id.iv_img)
        mActionBar = findViewById(R.id.myActionBar)
        //侧滑菜单
        mActionBar?.let { actionBar ->
            actionBar.setData(
                myActivity,
                "编辑美食信息",
                R.drawable.ic_back,
                0,
                0,
                resources.getColor(R.color.colorPrimary),
                object : ActionBarClickListener {
                    override fun onLeftClick() {
                        finish()
                    }

                    override fun onRightClick() {
                        // Do something
                    }
                })
        }
        initView()
    }

    private fun initView() {
        mfruit = intent.getSerializableExtra("fruit") as Fruit?
        if (mfruit != null) {
            etTitle!!.setText(mfruit!!.title)
            spType!!.setSelection(mfruit!!.typeId)
            etImg!!.setText(mfruit!!.img)
            etIssuer!!.setText(mfruit!!.issuer)
            etContent!!.setText(mfruit!!.content)
            spType!!.setSelection(mfruit!!.typeId, true)
            Glide.with(myActivity!!)
                .asBitmap()
                .load(mfruit!!.img)
                .error(R.drawable.ic_error)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImg!!)
        }
        ivImg!!.visibility = if (mfruit == null) View.GONE else View.VISIBLE
    }

    fun save(view: View?) {
        val title = etTitle!!.text.toString()
        val issuer = etIssuer!!.text.toString()
        val img = etImg!!.text.toString()
        val content = etContent!!.text.toString()
        val typeId = spType!!.selectedItemPosition
        if ("" == title) {
            Toast.makeText(myActivity, "标题不能为空", Toast.LENGTH_LONG).show()
            return
        }
        if ("" == issuer) {
            Toast.makeText(myActivity, "价格不能为空", Toast.LENGTH_LONG).show()
            return
        }
        if ("" == img) {
            Toast.makeText(myActivity, "图片地址不能为空", Toast.LENGTH_LONG).show()
            return
        }
        if ("" == content) {
            Toast.makeText(myActivity, "描述不能为空", Toast.LENGTH_LONG).show()
            return
        }
        var fruit: Fruit? = null
        if (title != if (mfruit != null) mfruit!!.title else "") {
            fruit = LitePal.where("title = ?", title).findFirst(Fruit::class.java)
        }
        if (fruit == null) {
            if (mfruit != null) {
                fruit = LitePal.where("title = ?", mfruit!!.title).findFirst(Fruit::class.java)
                fruit.typeId = typeId
                fruit.title = title
                fruit.issuer = issuer
                fruit.img = img
                fruit.content = content
            } else {
                fruit = Fruit(typeId, title, img, content, issuer, sf.format(Date()))
            }
//            fruit!!.()
            setResult(RESULT_OK)
            finish()
            Toast.makeText(myActivity, "保存成功", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(myActivity, "该标题已存在", Toast.LENGTH_LONG).show()
        }
    }
}
