package com.example.food.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.food.R
import com.example.food.bean.User
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener
import org.litepal.LitePal

/**
 * User Details
 */
class UserDetailActivity : AppCompatActivity() {
    private var mActionBar: ActionBar? = null //title bar
    private var mActivity: Activity? = null
    private var account: TextView? = null
    private var nickName: EditText? = null
    private var age: EditText? = null
    private var email: EditText? = null
    private var mUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        mActivity = this
        account = findViewById(R.id.account)
        nickName = findViewById(R.id.nickName)
        age = findViewById(R.id.age)
        email = findViewById(R.id.email)
        mActionBar = findViewById(R.id.myActionBar)
        //Side scrolling menu
        mActionBar?.setData(
            mActivity,
            "Employee Information",
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
        mUser = intent.getSerializableExtra("user") as User?
        if (mUser != null) {
            account?.setText(mUser!!.account)
            nickName?.setText(mUser!!.nickName)
            age?.setText(mUser!!.age.toString())
            email?.setText(mUser!!.email)
        }
    }

    //save
    fun save(view: View?) {
        val user = LitePal.where("account = ?", mUser!!.account).findFirst(
            User::class.java
        )
        val nickNameStr = nickName!!.text.toString()
        val ageStr = age!!.text.toString()
        val emailStr = email!!.text.toString()
        if ("" == nickNameStr) {
            Toast.makeText(mActivity, "Nickname cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if ("" == ageStr) {
            Toast.makeText(mActivity, "Age cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if ("" == emailStr) {
            Toast.makeText(mActivity, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (user != null) {
            user.nickName = nickNameStr
            user.age = ageStr.toInt()
            user.email = emailStr
            user.save()
            Toast.makeText(mActivity, "Successfully saved", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(mActivity, "Save failed", Toast.LENGTH_SHORT).show()
        }
    }
}
