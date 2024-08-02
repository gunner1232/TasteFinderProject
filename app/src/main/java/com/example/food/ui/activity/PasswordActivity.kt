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
import org.litepal.LitePal

/**
 * reset password
 */
class PasswordActivity : AppCompatActivity() {
    private var activity: Activity? = null
    private var mTitleBar: ActionBar? = null //title bar
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
            "Reset Password",
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

    //save info
    fun save(v: View) {
        //Close virtual keyboard
        val inputMethodManager =
            v.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        val account = etAccount!!.text.toString()
        val email = etEmail!!.text.toString()
        val newPassword = etNewPassword!!.text.toString()
        if ("" == account) { //Username cannot be empty
            Toast.makeText(activity, "Username cannot be empty", Toast.LENGTH_LONG).show()
            return
        }
        if ("" == email) { //Email is empty
            Toast.makeText(activity, "Email is empty", Toast.LENGTH_LONG).show()
            return
        }
        if ("" == newPassword) { //The new password is empty
            Toast.makeText(activity, "The new password is empty", Toast.LENGTH_LONG).show()
            return
        }
        val user = LitePal.where("account = ? and email = ?", account, email).findFirst(
            User::class.java
        )
        if (user != null) {
            user.password = newPassword
            user.save()
            Toast.makeText(activity, "Password changed successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(activity, "Username or email error", Toast.LENGTH_SHORT).show()
        }
    }
}
