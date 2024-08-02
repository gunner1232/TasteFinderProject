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
 * register page
 */
class RegisterActivity : Activity() {
    private var activity: Activity? = null
    private var mTitleBar: ActionBar? = null //title bar
    private var etAccount: EditText? = null //account
    private var etNickName: EditText? = null //nickname
    private var etAge: EditText? = null //age
    private var etEmail: EditText? = null //email
    private var etPassword: EditText? = null //password
    private var etPasswordSure: EditText? = null //confirm password
    private var tvLogin: TextView? = null //login
    private var btnRegister: Button? = null //register button
    private val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_register) //load page
        etAccount = findViewById<View>(R.id.et_account) as EditText //get account
        etNickName = findViewById<View>(R.id.et_nickName) as EditText //get nickname
        etAge = findViewById<View>(R.id.et_age) as EditText //get age
        etEmail = findViewById<View>(R.id.et_email) as EditText //get email
        etPassword = findViewById<View>(R.id.et_password) as EditText //get password
        etPasswordSure = findViewById<View>(R.id.et_password_sure) as EditText //get confirm password
        tvLogin = findViewById<View>(R.id.tv_login) as TextView //login
        btnRegister = findViewById<View>(R.id.btn_register) as Button //get register button
        mTitleBar = findViewById<View>(R.id.myActionBar) as ActionBar
        mTitleBar!!.setData(
            activity,
            "Register",
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
        tvLogin!!.setOnClickListener { //jump to the login page
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        //Set registration click button
        btnRegister!!.setOnClickListener(View.OnClickListener { v ->
            //Close virtual keyboard
            val inputMethodManager =
                v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            val account = etAccount!!.text.toString()
            val nickName = etNickName!!.text.toString()
            val age = etAge!!.text.toString()
            val email = etEmail!!.text.toString()
            val password = etPassword!!.text.toString()
            val passwordSure = etPasswordSure!!.text.toString()
            if ("" == account) { //Username cannot be empty
                Toast.makeText(activity, "Username cannot be empty", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == nickName) { //Nickname cannot be empty
                Toast.makeText(activity, "Nickname cannot be empty", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == age) { //Age cannot be empty
                Toast.makeText(activity, "Age cannot be empty", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == email) { //Email cannot be empty
                Toast.makeText(activity, "Email cannot be empty", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == password) { //Password is empty
                Toast.makeText(activity, "Password is empty", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (password != passwordSure) { //Password inconsistency
                Toast.makeText(activity, "Password inconsistency", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            var user = LitePal.where("account = ?", account).findFirst(
                User::class.java
            )
            if (user == null) {
                user = User(account, password, nickName, age.toInt(), email)
                user.save() //save user's info
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(activity, "Register was successful", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(activity, "This account already exists", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
