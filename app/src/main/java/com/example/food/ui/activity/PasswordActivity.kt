package com.example.food.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.food.R
import com.example.food.bean.User
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener
import org.litepal.crud.DataSupport

/**
 * 重置密码
 */
class PasswordActivity : AppCompatActivity() {
    private var activity: Activity? = null
    private var mTitleBar: ActionBar? = null //标题栏
    private var etAccount: EditText? = null
    private var etEmail: EditText? = null
    private var etNewPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_password)
        etAccount = findViewById(R.id.et_account)
        etEmail = findViewById(R.id.et_email)
        etNewPassword = findViewById(R.id.et_new_password)
        mTitleBar = findViewById<View>(R.id.myActionBar) as ActionBar
        mTitleBar!!.setData(
            activity,
            "重置密码",
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
    }

    //保存信息
    fun save(v: View) {
        //关闭虚拟键盘
        val inputMethodManager =
            v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        val account = etAccount!!.text.toString()
        val email = etEmail!!.text.toString()
        val newPassword = etNewPassword!!.text.toString()
        if ("" == account) { //账号不能为空
            Toast.makeText(activity, "账号不能为空", Toast.LENGTH_LONG).show()
            return
        }
        if ("" == email) { //邮箱为空
            Toast.makeText(activity, "邮箱为空", Toast.LENGTH_LONG).show()
            return
        }
        if ("" == newPassword) { //密码为空
            Toast.makeText(activity, "新密码为空", Toast.LENGTH_LONG).show()
            return
        }
        val user = DataSupport.where("account = ? and email = ?", account, email).findFirst(
            User::class.java
        )
        if (user != null) {
            user.password = newPassword
            user.save()
            Toast.makeText(activity, "密码修改成功", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(activity, "账号或者邮箱错误", Toast.LENGTH_SHORT).show()
        }
    }
}
