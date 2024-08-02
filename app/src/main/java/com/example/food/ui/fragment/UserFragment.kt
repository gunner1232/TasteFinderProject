package com.example.food.ui.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.food.R
import com.example.food.MyApplication
import com.example.food.ui.activity.BrowseActivity
import com.example.food.ui.activity.LoginActivity
import com.example.food.ui.activity.PasswordActivity
import com.example.food.ui.activity.PersonActivity
import com.example.food.util.SPUtils

/**
 * personal menu
 */
class UserFragment : Fragment() {
    private var mActivity: Activity? = null
    private var llPerson: LinearLayout? = null
    private var llSecurity: LinearLayout? = null
    private var llBrowse: LinearLayout? = null
    private var btnLogout: Button? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        llPerson = view.findViewById(R.id.person)
        llSecurity = view.findViewById(R.id.security)
        llBrowse = view.findViewById(R.id.browse)
        btnLogout = view.findViewById(R.id.logout)
        initView()
        return view
    }

    private fun initView() {
        val isAdmin = SPUtils.get(mActivity, SPUtils.IS_ADMIN, false) as Boolean
        llBrowse!!.visibility = if (isAdmin) View.GONE else View.VISIBLE
        //personal info
        llPerson!!.setOnClickListener { //Jump page
            val intent = Intent(mActivity, PersonActivity::class.java)
            startActivity(intent)
        }
        //Account safe
        llSecurity!!.setOnClickListener { //Jump page
            val intent = Intent(mActivity, PasswordActivity::class.java)
            startActivity(intent)
        }
        //Browsing History
        llBrowse!!.setOnClickListener { //Jump page
            val intent = Intent(mActivity, BrowseActivity::class.java)
            startActivity(intent)
        }
        //logout
        btnLogout!!.setOnClickListener {
            MyApplication.Instance?.mainActivity?.finish()
            SPUtils.remove(mActivity, SPUtils.IS_ADMIN)
            SPUtils.remove(mActivity, SPUtils.ACCOUNT)
            startActivity(Intent(mActivity, LoginActivity::class.java))
        }
    }
}
