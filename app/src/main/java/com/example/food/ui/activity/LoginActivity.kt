package com.example.food.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.food.R
import com.example.food.bean.User
import com.example.food.util.SPUtils
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener
import org.litepal.LitePal

/**
 * 登录页面
 */
class LoginActivity : Activity() {
    private var activity: Activity? = null
    private var mTitleBar: ActionBar? = null //标题栏
    private var etAccount: EditText? = null //手机号
    private var etPassword: EditText? = null //密码
    private var tvRegister: TextView? = null //注册
    private var btnLogin: Button? = null //登录按钮
    private var rgType: RadioGroup? = null //用户类型
    private var rbUser: RadioButton? = null //用户类型
    private var rbAdmin: RadioButton? = null //用户类型
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_login) //加载页面
        etAccount = findViewById<View>(R.id.et_account) as EditText //获取手机号
        etPassword = findViewById<View>(R.id.et_password) as EditText //获取密码
        tvRegister = findViewById<View>(R.id.tv_register) as TextView //获取注册
        btnLogin = findViewById<View>(R.id.btn_login) as Button //获取登录
        rgType = findViewById(R.id.rg_type)
        rbUser = findViewById(R.id.rb_user)
        rbAdmin = findViewById(R.id.rb_admin)
        mTitleBar = findViewById<View>(R.id.myActionBar) as ActionBar
        mTitleBar!!.setData(
            activity,
            "登录",
            0,
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

        //手机号注册
        tvRegister!!.setOnClickListener { //跳转到注册页面
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
        //选择类型
        rgType?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            SPUtils.put(
                activity,
                SPUtils.IS_ADMIN,
                checkedId == R.id.rb_admin
            )
        })
        //设置点击按钮
        btnLogin!!.setOnClickListener(View.OnClickListener { v -> //关闭虚拟键盘
            val inputMethodManager =
                v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            //获取请求参数
            val account = etAccount!!.text.toString()
            val password = etPassword!!.text.toString()
            val isAdmit = SPUtils.get(activity, SPUtils.IS_ADMIN, false) as Boolean
            if ("" == account) { //账号不能为空
                Toast.makeText(activity, "账号不能为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == password) { //密码为空
                Toast.makeText(activity, "密码为空", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            val user = LitePal.where("account = ?", account).findFirst(
                User::class.java
            )
            if (user != null) {
                if (password != user.password) {
                    Toast.makeText(activity, "密码错误", Toast.LENGTH_SHORT).show()
                } else {
                    if (isAdmit && "admin" != user.account) {
                        Toast.makeText(activity, "该账号不是管理员账号", Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                    if (!isAdmit && "admin" == user.account) {
                        Toast.makeText(activity, "该账号不是普通用户账号", Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                    SPUtils.put(this@LoginActivity, "account", account)
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(activity, "账号不存在", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
