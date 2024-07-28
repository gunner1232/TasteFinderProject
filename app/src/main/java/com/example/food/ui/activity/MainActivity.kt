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
 * 主页面
 */
class MainActivity : Activity() {
    private var mActionBar: ActionBar? = null //标题栏
    private var myActivity: Activity? = null
    private var llContent: LinearLayout? = null
    private var rbFruit: RadioButton? = null
    private var rbFruitData: RadioButton? = null
    private var rbUserManage: RadioButton? = null
    private var rbUser: RadioButton? = null
    private val fragments = arrayOf<Fragment?>(null, null, null, null) //存放Fragment
    private var mIsAdmin: Boolean? = null //是否管理员
    private var mAccount: String? = null //账号

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
        //侧滑菜单
        mActionBar!!.setData(
            myActivity,
            "点餐",
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
            mActionBar!!.setTitle("点餐")
            switchFragment(0)
        }
        rbFruitData!!.setOnClickListener {
            if ("" == mAccount) { ////未登录,跳转到登录页面
                MyApplication.Instance?.mainActivity?.finish()
                startActivity(Intent(myActivity, LoginActivity::class.java))
            } else { //已经登录
                mActionBar!!.setTitle("订单")
                switchFragment(1)
            }
        }
        rbUserManage!!.setOnClickListener {
            mActionBar!!.setTitle("用户管理")
            switchFragment(2)
        }
        rbUser!!.setOnClickListener {
            if ("" == mAccount) { ////未登录,跳转到登录页面
                MyApplication.Instance?.mainActivity?.finish()
                startActivity(Intent(myActivity, LoginActivity::class.java))
            } else { //已经登录
                mActionBar!!.setTitle("我的")
                switchFragment(if (mIsAdmin!!) 3 else 2)
            }
        }
    }

    private fun initView() {
        mIsAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
        mAccount = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
        //设置导航栏图标样式
        val iconNews = resources.getDrawable(R.drawable.selector_main_rb_home) //设置主页图标样式
        iconNews.setBounds(0, 0, 68, 68) //设置图标边距 大小
        rbFruit!!.setCompoundDrawables(null, iconNews, null, null) //设置图标位置
        rbFruit!!.compoundDrawablePadding = 5 //设置文字与图片的间距
        val iconNewsData = resources.getDrawable(R.drawable.selector_main_rb_buy) //设置主页图标样式
        iconNewsData.setBounds(0, 0, 68, 68) //设置图标边距 大小
        rbFruitData!!.setCompoundDrawables(null, iconNewsData, null, null) //设置图标位置
        rbFruitData!!.compoundDrawablePadding = 5 //设置文字与图片的间距
        val iconSetting = resources.getDrawable(R.drawable.selector_main_rb_manage) //设置主页图标样式
        iconSetting.setBounds(0, 0, 60, 60) //设置图标边距 大小
        rbUserManage!!.setCompoundDrawables(null, iconSetting, null, null) //设置图标位置
        rbUserManage!!.compoundDrawablePadding = 5 //设置文字与图片的间距
        val iconUser = resources.getDrawable(R.drawable.selector_main_rb_user) //设置主页图标样式
        iconUser.setBounds(0, 0, 55, 55) //设置图标边距 大小
        rbUser!!.setCompoundDrawables(null, iconUser, null, null) //设置图标位置
        rbUser!!.compoundDrawablePadding = 5 //设置文字与图片的间距
        rbUserManage!!.visibility = if (mIsAdmin!!) View.VISIBLE else View.GONE
        switchFragment(0)
        rbFruit!!.isChecked = true
    }

    /**
     * 方法 - 切换Fragment
     *
     * @param fragmentIndex 要显示Fragment的索引
     */
    private fun switchFragment(fragmentIndex: Int) {
        //在Activity中显示Fragment
        //1、获取Fragment管理器 FragmentManager
        val fragmentManager = this.fragmentManager
        //2、开启fragment事务
        val transaction = fragmentManager.beginTransaction()

        //懒加载 - 如果需要显示的Fragment为null，就new。并添加到Fragment事务中
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

            //==添加Fragment对象到Fragment事务中
            //参数：显示Fragment的容器的ID，Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex])
        }

        //隐藏其他的Fragment
        for (i in fragments.indices) {
            if (fragmentIndex != i && fragments[i] != null) {
                //隐藏指定的Fragment
                transaction.hide(fragments[i])
            }
        }
        //4、显示Fragment
        transaction.show(fragments[fragmentIndex])

        //5、提交事务
        transaction.commit()
    }

    /**
     * 双击退出
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
            Toast.makeText(myActivity, "再点击一次退出应用程序", Toast.LENGTH_LONG).show()
        } else {
            finish()
        }
    }
}
