package com.example.food.ui.fragment

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food.R
import com.example.food.MyApplication
import com.example.food.adapter.FruitAdapter
import com.example.food.bean.Fruit
import com.example.food.ui.activity.AddFruitActivity
import com.example.food.ui.activity.FruitDetailActivity
import com.example.food.ui.activity.LoginActivity
import com.example.food.util.SPUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import org.litepal.LitePal

/**
 * 水果
 */
class FruitFragment : Fragment() {
    private var myActivity: Activity? = null //上下文
    private var tabTitle: TabLayout? = null
    private var rvfruitList: RecyclerView? = null
    private var mfruitAdapter: FruitAdapter? = null
    private var llEmpty: LinearLayout? = null
    private var mIsAdmin: Boolean? = null
    private var etQuery: EditText? = null //搜索内容
    private var ivSearch: ImageView? = null //搜索图标
    private var btnAdd: FloatingActionButton? = null
    private val state = arrayOf("0", "1", "2", "3", "4", "5")
    private val title = arrayOf("人气", "主食", "靓汤", "小炒", "炸鸡", "轻食")
    private var typeId = "0"
    private var mfruit: List<Fruit>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_fruit, container, false)
//        tabTitle = view.findViewById<View>(R.id.tab_title) as TabLayout
//        rvfruitList = view.findViewById<View>(R.id.rv_fruit_list) as RecyclerView
//        llEmpty = view.findViewById(R.id.ll_empty)
//        etQuery = view.findViewById(R.id.et_query)
//        ivSearch = view.findViewById(R.id.iv_search)
//        btnAdd = view.findViewById<View>(R.id.btn_add) as FloatingActionButton
//        //获取控件
//        initView()
//        //软键盘搜索
//        ivSearch?.setOnClickListener(View.OnClickListener {
//            loadData() //加载数据
//        })
//        //点击软键盘中的搜索
//        etQuery?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                loadData() //加载数据
//                return@OnEditorActionListener true
//            }
//            false
//        })
//        btnAdd!!.setOnClickListener {
//            val intent = Intent(myActivity, AddFruitActivity::class.java)
//            startActivityForResult(intent, 100)
//        }
//        return view
//    }
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    val view = inflater.inflate(R.layout.fragment_fruit, container, false)
    tabTitle = view.findViewById<View>(R.id.tab_title) as TabLayout
    rvfruitList = view.findViewById<View>(R.id.rv_fruit_list) as RecyclerView
    llEmpty = view.findViewById(R.id.ll_empty)
    etQuery = view.findViewById(R.id.et_query)
    ivSearch = view.findViewById(R.id.iv_search)
    btnAdd = view.findViewById<View>(R.id.btn_add) as FloatingActionButton
    //获取控件
    initView()
    //软键盘搜索
    ivSearch?.setOnClickListener {
        loadData() //加载数据
    }
    //点击软键盘中的搜索
    etQuery?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            loadData() //加载数据
            return@OnEditorActionListener true
        }
        false
    })
    btnAdd?.setOnClickListener {
        val intent = Intent(myActivity, AddFruitActivity::class.java)
        startActivityForResult(intent, 100)
    }
    return view
}

    /**
     * 初始化页面
     */
    private fun initView() {
        mIsAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
        btnAdd!!.visibility = if (mIsAdmin!!) View.VISIBLE else View.GONE
        tabTitle!!.tabMode = TabLayout.MODE_SCROLLABLE

        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);

        //为TabLayout添加tab名称
        for (i in title.indices) {
            tabTitle!!.addTab(tabTitle!!.newTab().setText(title[i]))
        }
        val layoutManager = LinearLayoutManager(myActivity)
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3、设置recyclerView的布局管理器
        rvfruitList!!.layoutManager = layoutManager
        //==2、实例化适配器
        //=2.1、初始化适配器
        mfruitAdapter = FruitAdapter(llEmpty, rvfruitList)
        //=2.3、设置recyclerView的适配器
        rvfruitList!!.adapter = mfruitAdapter
        loadData()

//        mfruitAdapter!!.setItemListener { fruit ->
//            val isAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
//            val account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
//            if ("" == account) { //未登录,跳转到登录页面
//                MyApplication?.Instance?.mainActivity?.finish()
//                startActivity(Intent(myActivity, LoginActivity::class.java))
//            } else {
//                //已经登录
//                val intent = if (isAdmin) {
//                    Intent(myActivity, AddFruitActivity::class.java)
//                } else {
//                    Intent(myActivity, FruitDetailActivity::class.java)
//                }
//                intent.putExtra("fruit", fruit)
//                startActivityForResult(intent, 100)
//            }
//        }


        mfruitAdapter!!.setItemListener(object : FruitAdapter.ItemListener {
//            override fun itemClick(fruit: Fruit) {
//                val isAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
//                val account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
//                if (account.isEmpty()) { // 未登录, 跳转到登录页面
//                    MyApplication.Instance?.mainActivity?.finish()
//                    startActivity(Intent(myActivity, LoginActivity::class.java))
//                } else { // 已经登录
//                    val intent = if (isAdmin) {
//                        Intent(myActivity, AddFruitActivity::class.java)
//                    } else {
//                        Intent(myActivity, FruitDetailActivity::class.java)
//                    }
//                    intent.putExtra("fruit", fruit)
//                    startActivityForResult(intent, 100)
//                }
//            }

//            override fun ItemClick(fruit: Fruit) {
//                val isAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
//                val account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
//                if (account.isEmpty()) { // 未登录, 跳转到登录页面
////                    MyApplication.Instance.getMainActivity().finish()
//                    MyApplication.instance.mainActivity?.finish()
//                    startActivity(Intent(myActivity, LoginActivity::class.java))
//                } else { // 已经登录
//                    val intent = if (isAdmin) {
//                        Intent(myActivity, AddFruitActivity::class.java)
//                    } else {
//                        Intent(myActivity, FruitDetailActivity::class.java)
//                    }
//                    intent.putExtra("fruit", fruit)
//                    startActivityForResult(intent, 100)
//                }
//            }

            override fun ItemClick(fruit: Fruit?) { val isAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
                val account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
                if (account.isEmpty()) { // 未登录, 跳转到登录页面
//                    MyApplication.Instance.getMainActivity().finish()
                    MyApplication.Instance?.mainActivity?.finish()
                    startActivity(Intent(myActivity, LoginActivity::class.java))
                } else { // 已经登录
                    val intent = if (isAdmin) {
                        Intent(myActivity, AddFruitActivity::class.java)
                    } else {
                        Intent(myActivity, FruitDetailActivity::class.java)
                    }
                    intent.putExtra("fruit", fruit)
                    startActivityForResult(intent, 100)
                }
            }
        })


        tabTitle!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                typeId = state[tab.position]
                loadData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun loadData() {
        val content = etQuery!!.text.toString() //获取搜索内容
        mfruit = if ("" == content) {
            LitePal.where("typeId = ?", typeId).find(Fruit::class.java)
        } else {
            LitePal.where("typeId = ? and title like ?", typeId, "%$content%").find(
                Fruit::class.java
            ) //通过标题模糊查询留言
        }
        if (mfruit != null && mfruit!!.size > 0) {
            rvfruitList!!.visibility = View.VISIBLE
            llEmpty!!.visibility = View.GONE
            mfruitAdapter!!.addItem(mfruit)
        } else {
            rvfruitList!!.visibility = View.GONE
            llEmpty!!.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            loadData() //加载数据
        }
    }
}
