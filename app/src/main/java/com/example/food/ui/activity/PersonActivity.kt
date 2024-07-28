package com.example.food.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.food.R
import com.example.food.bean.User
import com.example.food.util.SPUtils
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener
import org.litepal.LitePal

/**
 * 个人信息
 */
class PersonActivity : AppCompatActivity() {
    private var mActivity: Activity? = null
    private var mTitleBar: ActionBar? = null //标题栏
    private var tvAccount: TextView? = null
    private var etNickName: TextView? = null
    private var etAge: TextView? = null
    private var etEmail: TextView? = null
    private var btnSave: Button? = null //保存
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)
        mActivity = this
        tvAccount = findViewById(R.id.tv_account)
        etNickName = findViewById(R.id.tv_nickName)
        etAge = findViewById(R.id.tv_age)
        etEmail = findViewById(R.id.tv_email)
        btnSave = findViewById(R.id.btn_save)
        mTitleBar = findViewById<View>(R.id.myActionBar) as ActionBar
        mTitleBar!!.setData(
            mActivity,
            "个人信息",
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
        initView()
    }

    private fun initView() {
        val account = SPUtils.get(mActivity, "account", "") as String
        val user = LitePal.where("account = ?", account).findFirst(
            User::class.java
        )
        if (user != null) {
            tvAccount!!.text = user.account
            etNickName!!.text = user.nickName
            etAge!!.text = user.age.toString()
            etEmail!!.text = user.email
        }
        //保存
        btnSave!!.setOnClickListener(View.OnClickListener {
            val account = tvAccount!!.text.toString()
            val nickName = etNickName!!.text.toString()
            val age = etAge!!.text.toString()
            val email = etEmail!!.text.toString()
            val user1 = LitePal.where("account = ?", account).findFirst(
                User::class.java
            )
            if ("" == nickName) {
                Toast.makeText(mActivity, "昵称不能为空", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if ("" == age) {
                Toast.makeText(mActivity, "年龄不能为空", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if ("" == email) {
                Toast.makeText(mActivity, "邮箱不能为空", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            user1.nickName = nickName
            user1.age = age.toInt()
            user1.email = email
            user1.save()
            Toast.makeText(mActivity, "保存成功", Toast.LENGTH_SHORT).show()
            finish() //关闭页面
        })
        /* btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.Instance.getMainActivity().finish();
                SPUtils.remove(mActivity,"account");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });*/
    }
}
