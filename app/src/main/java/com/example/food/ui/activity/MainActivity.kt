package com.example.food.ui.activity

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import com.example.food.R
import com.example.food.MyApplication
import com.example.food.ui.fragment.FruitFragment
import com.example.food.ui.fragment.OrderFragment
import com.example.food.ui.fragment.UserFragment
import com.example.food.ui.fragment.UserManageFragment
import com.example.food.util.SPUtils
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener

/**
 * main page
 */
class MainActivity : Activity() {
    private var mActionBar: ActionBar? = null //title bar
    private var myActivity: Activity? = null
    private var llContent: LinearLayout? = null
    private var rbFruit: RadioButton? = null
    private var rbFruitData: RadioButton? = null
    private var rbUserManage: RadioButton? = null
    private var rbUser: RadioButton? = null
    private val fragments = arrayOf<Fragment?>(null, null, null, null) //Store Fragment
    private var mIsAdmin: Boolean? = null //admin or not
    private var mAccount: String? = null //account

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myActivity = this
        setContentView(R.layout.activity_main)
        MyApplication.Instance?.mainActivity
        mIsAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
        llContent = findViewById<View>(R.id.ll_main_content) as LinearLayout
        rbFruit = findViewById<View>(R.id.rb_main_fruit) as RadioButton
        rbFruitData = findViewById<View>(R.id.rb_main_fruit_data) as RadioButton
        rbUserManage = findViewById<View>(R.id.rb_main_setting) as RadioButton
        rbUser = findViewById<View>(R.id.rb_main_user) as RadioButton
        mActionBar = findViewById<View>(R.id.myActionBar) as ActionBar
        //Side scrolling menu
        mActionBar!!.setData(
            myActivity,
            "Order",
            0,
            0,
            0,
            resources.getColor(R.color.colorPrimary),
            object : ActionBarClickListener {
                override fun onLeftClick() {
                }

                override fun onRightClick() {
                }
            })
        initView()
        setViewListener()
    }

    private fun setViewListener() {
        rbFruit!!.setOnClickListener {
            mActionBar!!.setTitle("Order")
            switchFragment(0)
        }
        rbFruitData!!.setOnClickListener {
            if ("" == mAccount) { //Not logged in, redirected to login page
                MyApplication.Instance?.mainActivity?.finish()
                startActivity(Intent(myActivity, LoginActivity::class.java))
            } else { //already login
                mActionBar!!.setTitle("Recents")
                switchFragment(1)
            }
        }
        rbUserManage!!.setOnClickListener {
            mActionBar!!.setTitle("User Management")
            switchFragment(2)
        }
        rbUser!!.setOnClickListener {
            if ("" == mAccount) { //Not logged in, redirected to login page
                MyApplication.Instance?.mainActivity?.finish()
                startActivity(Intent(myActivity, LoginActivity::class.java))
            } else { //already login
                mActionBar!!.setTitle("More")
                switchFragment(if (mIsAdmin!!) 3 else 2)
            }
        }
    }

    private fun initView() {
        mIsAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
        mAccount = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
        //Set navigation bar icon style
        val iconNews = resources.getDrawable(R.drawable.selector_main_rb_home) //Set homepage icon style
        iconNews.setBounds(0, 0, 68, 68) //Set icon margin size
        rbFruit!!.setCompoundDrawables(null, iconNews, null, null) //Set icon location
        rbFruit!!.compoundDrawablePadding = 5 //Set the spacing between text and images
        val iconNewsData = resources.getDrawable(R.drawable.selector_main_rb_buy) //Set homepage icon style
        iconNewsData.setBounds(0, 0, 68, 68) //Set icon margin size
        rbFruitData!!.setCompoundDrawables(null, iconNewsData, null, null) //Set icon location
        rbFruitData!!.compoundDrawablePadding = 5 //Set the spacing between text and images
        val iconSetting = resources.getDrawable(R.drawable.selector_main_rb_manage) //Set homepage icon style
        iconSetting.setBounds(0, 0, 60, 60) //Set icon margin size
        rbUserManage!!.setCompoundDrawables(null, iconSetting, null, null) //Set icon location
        rbUserManage!!.compoundDrawablePadding = 5 //Set the spacing between text and images
        val iconUser = resources.getDrawable(R.drawable.selector_main_rb_user) //Set homepage icon style
        iconUser.setBounds(0, 0, 55, 55) //Set icon margin size
        rbUser!!.setCompoundDrawables(null, iconUser, null, null) //Set icon location
        rbUser!!.compoundDrawablePadding = 5 //Set the spacing between text and images
//        rbUserManage!!.visibility = if (mIsAdmin!!) View.VISIBLE else View.GONE
        switchFragment(0)
        rbFruit!!.isChecked = true
    }

    /**
     * Method - Switch Fragment
     *
     * @param fragmentIndex To display the index of the fragment
     */
    private fun switchFragment(fragmentIndex: Int) {
        //Display Fragment in Activity
        //1. Get Fragment Manager Fragment Manager
        val fragmentManager = this.fragmentManager
        //2. Activate fragment transaction
        val transaction = fragmentManager.beginTransaction()

        //Lazy loading - if the fragment to be displayed is null, use new. And add it to the Fragment transaction
        if (fragments[fragmentIndex] == null) {
            if (mIsAdmin!!) {
                when (fragmentIndex) {
                    0 -> fragments[fragmentIndex] = FruitFragment()
                    1 -> fragments[fragmentIndex] = OrderFragment()
                    2 -> fragments[fragmentIndex] = UserManageFragment()
                    3 -> fragments[fragmentIndex] = UserFragment()
                }
            } else {
                when (fragmentIndex) {
                    0 -> fragments[fragmentIndex] = FruitFragment()
                    1 -> fragments[fragmentIndex] = OrderFragment()
                    2 -> fragments[fragmentIndex] = UserFragment()
                }
            }

            //==Add Fragment Object to Fragment Transaction
            //Parameters: Display the ID of the container for the Fragment, Fragment object
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex])
        }

        //Hide other fragments
        for (i in fragments.indices) {
            if (fragmentIndex != i && fragments[i] != null) {
                //Hide the specified fragment
                transaction.hide(fragments[i])
            }
        }
        //4. Display Fragment
        transaction.show(fragments[fragmentIndex])

        //5. Submit transaction
        transaction.commit()
    }

    /**
     * Double click to exit
     *
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
        }

        return false
    }

    private var time: Long = 0

    fun exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(myActivity, "Click again to exit the app", Toast.LENGTH_LONG).show()
        } else {
            finish()
        }
    }
}
