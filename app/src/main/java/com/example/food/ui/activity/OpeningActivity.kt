package com.example.food.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.food.R
import com.example.food.bean.Fruit
import com.example.food.bean.User
import com.example.food.util.SPUtils
import com.example.food.util.StatusBarUtil
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date

/**
 * 开屏页面
 */
class OpeningActivity : AppCompatActivity() {
    private var myActivity: Activity? = null
    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myActivity = this
        //设置页面布局
        setContentView(R.layout.activity_opening)
        try {
            initView()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class, JSONException::class)
    private fun initView() {
        StatusBarUtil.setStatusBar(myActivity, true) //设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity, true) //状态栏文字颜色
        Handler().postDelayed(Runnable {
            if ((intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish()
                return@Runnable
            }
            val isFirst = SPUtils.get(myActivity, SPUtils.IF_FIRST, true) as Boolean
            val account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
            if (isFirst) { //第一次进来  初始化本地数据
                SPUtils.put(myActivity, SPUtils.IF_FIRST, false) //第一次
                //初始化数据
                //获取json数据
                var rewardJson: String? = ""
                var rewardJsonLine: String?
                //assets文件夹下db.json文件的路径->打开db.json文件
                var bufferedReader: BufferedReader? = null
                try {
                    bufferedReader =
                        BufferedReader(InputStreamReader(myActivity!!.assets.open("db.json")))
                    while (true) {
                        if (bufferedReader.readLine().also { rewardJsonLine = it } == null) break
                        rewardJson += rewardJsonLine
                    }
                    val jsonObject = JSONObject(rewardJson)
                    val fruitList = jsonObject.getJSONArray("fruit") //获得列表
                    //把物品列表保存到本地
                    var i = 0
                    val length = fruitList.length()
                    while (i < length) {
                        val o = fruitList.getJSONObject(i)
                        val fruit = Fruit(
                            o.getInt("typeId"),
                            o.getString("title"),
                            o.getString("img"),
                            o.getString("content"),
                            o.getString("issuer"),
                            sf.format(Date())
                        )
                        fruit.save() //保存到本地
                        i++
                    }
                    val user = User("admin", "root", "管理员", 22, "123456789@qq.com")
                    user.save()
                    val user2 = User("312", "312", "牢大", 25, "12321216789@qq.com")
                    user2.save()
                    val user3 = User("313", "313", "祖国人", 27, "142126789@qq.com")
                    user3.save()
                    val user4 = User("314", "314", "baby", 18, "1532556789@qq.com")
                    user4.save()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            //两秒后跳转到主页面
            val intent2 = Intent()
            if ("" == account) {
                intent2.setClass(this@OpeningActivity, MainActivity::class.java)
            } else {
                intent2.setClass(this@OpeningActivity, MainActivity::class.java)
            }
            startActivity(intent2)
            finish()
        }, 2000)
    }


    override fun onBackPressed() {
    }
}
