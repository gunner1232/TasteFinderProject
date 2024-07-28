package com.example.food.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.food.R
import com.example.food.bean.User
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener
import org.litepal.LitePal
import java.text.SimpleDateFormat

/**
 * 注册页面
 */
class RegisterActivity : Activity() {
    private var activity: Activity? = null
    private var mTitleBar: ActionBar? = null //标题栏
    private var etAccount: EditText? = null //手机号
    private var etNickName: EditText? = null //昵称
    private var etAge: EditText? = null //年龄
    private var etEmail: EditText? = null //邮箱
    private var etPassword: EditText? = null //密码
    private var etPasswordSure: EditText? = null //确认密码
    private var tvLogin: TextView? = null //登录
    private var btnRegister: Button? = null //注册按钮
    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_register) //加载页面
        etAccount = findViewById<View>(R.id.et_account) as EditText //获取手机号
        etNickName = findViewById<View>(R.id.et_nickName) as EditText //获取昵称
        etAge = findViewById<View>(R.id.et_age) as EditText //获取年龄
        etEmail = findViewById<View>(R.id.et_email) as EditText //获取邮箱
        etPassword = findViewById<View>(R.id.et_password) as EditText //获取密码
        etPasswordSure = findViewById<View>(R.id.et_password_sure) as EditText //获取确认密码
        tvLogin = findViewById<View>(R.id.tv_login) as TextView //登录
        btnRegister = findViewById<View>(R.id.btn_register) as Button //获取注册按钮
        mTitleBar = findViewById<View>(R.id.myActionBar) as ActionBar
        mTitleBar!!.setData(
            activity,
            "注册",
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
        tvLogin!!.setOnClickListener { //跳转到登录页面
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        //设置注册点击按钮
        btnRegister!!.setOnClickListener(View.OnClickListener { v ->
            //关闭虚拟键盘
            val inputMethodManager =
                v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            val account = etAccount!!.text.toString()
            val nickName = etNickName!!.text.toString()
            val age = etAge!!.text.toString()
            val email = etEmail!!.text.toString()
            val password = etPassword!!.text.toString()
            val passwordSure = etPasswordSure!!.text.toString()
            if ("" == account) { //账号不能为空
                Toast.makeText(activity, "账号不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == nickName) { //昵称不能为空
                Toast.makeText(activity, "昵称不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == age) { //年龄不能为空
                Toast.makeText(activity, "年龄不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == email) { //邮箱不能为空
                Toast.makeText(activity, "邮箱不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == password) { //密码为空
                Toast.makeText(activity, "密码为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (password != passwordSure) { //密码不一致
                Toast.makeText(activity, "密码不一致", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            var user = LitePal.where("account = ?", account).findFirst(
                User::class.java
            )
            if (user == null) {
                user = User(account, password, nickName, age.toInt(), email)
                user.save() //保存用户信息
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(activity, "注册成功", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(activity, "该账号已存在", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
