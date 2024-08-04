package com.example.food.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.food.R
import com.example.food.bean.Food
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
 * Open screen page
 */
class OpeningActivity : AppCompatActivity() {
    private var myActivity: Activity? = null
    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myActivity = this
        //set the page layout
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
        StatusBarUtil.setStatusBar(myActivity, true) //Set whether the current interface is in full screen mode (status bar)
        StatusBarUtil.setStatusBarLightMode(myActivity, true) //Status bar text color
        Handler().postDelayed(Runnable {
            if ((intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish()
                return@Runnable
            }
            val isFirst = SPUtils.get(myActivity, SPUtils.IF_FIRST, true) as Boolean
            val account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
            if (isFirst) { //First time entering to initialize local data
                SPUtils.put(myActivity, SPUtils.IF_FIRST, false)
                //Initialized Data
                //Retrieve JSON data
                var rewardJson: String? = ""
                var rewardJsonLine: String?
                //Path to the db.exe file in the assets folder ->Open the dbjson file
                var bufferedReader: BufferedReader? = null
                try {
                    bufferedReader =
                        BufferedReader(InputStreamReader(myActivity!!.assets.open("db.json")))
                    while (true) {
                        if (bufferedReader.readLine().also { rewardJsonLine = it } == null) break
                        rewardJson += rewardJsonLine
                    }
                    val jsonObject = JSONObject(rewardJson)
                    val fruitList = jsonObject.getJSONArray("fruit") //get list
                    //Save the item list locally
                    var i = 0
                    val length = fruitList.length()
                    while (i < length) {
                        val o = fruitList.getJSONObject(i)
                        val food = Food(
                            o.getInt("typeId"),
                            o.getString("title"),
                            o.getString("img"),
                            o.getString("content"),
                            o.getString("issuer"),
                            sf.format(Date())
                        )
                        food.save() //save to the local
                        i++
                    }
                    val user = User("admin", "root", "admin1", 22, "123456789@gmail.com")
                    user.save()
                    val user2 = User("312", "312", "user1", 25, "12321216789@gmail.com")
                    user2.save()
                    val user3 = User("313", "313", "user2", 27, "142126789@gmail.com")
                    user3.save()
                    val user4 = User("314", "314", "user3", 18, "1532556789@gmail.com")
                    user4.save()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            //Jump to the main page in two seconds
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
