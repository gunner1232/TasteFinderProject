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
import com.example.food.adapter.OrderAdapter
import com.example.food.bean.Orders
import com.example.food.util.SPUtils
import org.litepal.LitePal
import java.util.Collections

/**
 * 订单
 */
class OrderFragment : Fragment() {
    private var myActivity: Activity? = null
    private var llEmpty: LinearLayout? = null
    private var rvOrderList: RecyclerView? = null
    var mOrderAdapter: OrderAdapter? = null
    private var mIsAdmin: Boolean? = null
    private var etQuery: EditText? = null //搜索内容
    private var ivSearch: ImageView? = null //搜索图标
    private var mOrder: List<Orders?>? = ArrayList()
    private var account: String? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        rvOrderList = view.findViewById(R.id.rv_order_list)
        llEmpty = view.findViewById(R.id.ll_empty)
        etQuery = view.findViewById(R.id.et_query)
        ivSearch = view.findViewById(R.id.iv_search)
        //获取控件
        initView()
        //软键盘搜索
        ivSearch?.setOnClickListener(View.OnClickListener {
            loadData() //加载数据
        })
        //点击软键盘中的搜索
        etQuery?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                loadData() //加载数据
                return@OnEditorActionListener true
            }
            false
        })
        return view
    }

    private fun initView() {
        mIsAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
        account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
        val layoutManager = LinearLayoutManager(myActivity)
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3、设置recyclerView的布局管理器
        rvOrderList!!.layoutManager = layoutManager
        //==2、实例化适配器
        //=2.1、初始化适配器
        mOrderAdapter = OrderAdapter(llEmpty, rvOrderList)
        //=2.3、设置recyclerView的适配器
        rvOrderList!!.adapter = mOrderAdapter
        loadData() //加载数据
    }

    /**
     * 加载数据
     */
    private fun loadData() {
        val content = etQuery!!.text.toString() //获取搜索内容
        mOrder = if ("" == content && !mIsAdmin!!) {
            LitePal.where("account = ? ", account).find(Orders::class.java) //查询全部
        } else {
            LitePal.where("number like ? and account != ?", "%$content%", "admin").find(
                Orders::class.java
            ) //通过标题模糊查询留言
        }
        Collections.reverse(mOrder)
        if (mOrder != null && mOrder!!.size > 0) {
            rvOrderList!!.visibility = View.VISIBLE
            llEmpty!!.visibility = View.GONE
            mOrderAdapter!!.addItem(mOrder)
        } else {
            rvOrderList!!.visibility = View.GONE
            llEmpty!!.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            loadData()
        }
    }
}
