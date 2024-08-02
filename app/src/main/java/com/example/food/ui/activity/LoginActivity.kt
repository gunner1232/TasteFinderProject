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
 * login page
 */
class LoginActivity : Activity() {
    private var activity: Activity? = null
    private var mTitleBar: ActionBar? = null //title bar
    private var etAccount: EditText? = null //account
    private var etPassword: EditText? = null //password
    private var tvRegister: TextView? = null //register
    private var btnLogin: Button? = null //login button
    private var rgType: RadioGroup? = null //user type
    private var rbUser: RadioButton? = null //user type
    private var rbAdmin: RadioButton? = null //user type
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        setContentView(R.layout.activity_login) //load pages
        etAccount = findViewById<View>(R.id.et_account) as EditText //get phone number
        etPassword = findViewById<View>(R.id.et_password) as EditText //get password
        tvRegister = findViewById<View>(R.id.tv_register) as TextView //get register
        btnLogin = findViewById<View>(R.id.btn_login) as Button //get login
        rgType = findViewById(R.id.rg_type)
        rbUser = findViewById(R.id.rb_user)
        rbAdmin = findViewById(R.id.rb_admin)
        mTitleBar = findViewById<View>(R.id.myActionBar) as ActionBar
        mTitleBar!!.setData(
            activity,
            "Login",
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

        //Mobile number registration
        tvRegister!!.setOnClickListener { //Jump to the registration page
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
        //choose type
        rgType?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            SPUtils.put(
                activity,
                SPUtils.IS_ADMIN,
                checkedId == R.id.rb_admin
            )
        })
        //Set click button
        btnLogin!!.setOnClickListener(View.OnClickListener { v -> //Close virtual keyboard
            val inputMethodManager =
                v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            //Get request parameters
            val account = etAccount!!.text.toString()
            val password = etPassword!!.text.toString()
            val isAdmit = SPUtils.get(activity, SPUtils.IS_ADMIN, false) as Boolean
            if ("" == account) { //Username cannot be empty
                Toast.makeText(activity, "Username cannot be empty", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if ("" == password) { //Password is empty
                Toast.makeText(activity, "Password is empty", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            val user = LitePal.where("account = ?", account).findFirst(
                User::class.java
            )
            if (user != null) {
                if (password != user.password) {
                    Toast.makeText(activity, "Password error", Toast.LENGTH_SHORT).show()
                } else {
                    if (isAdmit && "admin" != user.account) {
                        Toast.makeText(activity, "This account is not an admin account", Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                    if (!isAdmit && "admin" == user.account) {
                        Toast.makeText(activity, "This account is not a regular user account", Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                    SPUtils.put(this@LoginActivity, "account", account)
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(activity, "Account does not exist", Toast.LENGTH_SHORT).show()
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
