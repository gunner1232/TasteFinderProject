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
import com.example.food.adapter.UserAdapter
import com.example.food.bean.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.litepal.LitePal
import java.util.Collections

/**
 * 用户管理
 */
class UserManageFragment : Fragment() {
    private var myActivity: Activity? = null
    private var llEmpty: LinearLayout? = null
    private var rvUserList: RecyclerView? = null
    var mUserAdapter: UserAdapter? = null
    private val btnAdd: FloatingActionButton? = null
    private var etQuery: EditText? = null //搜索内容
    private var ivSearch: ImageView? = null //搜索图标
    private var mUsers: List<User?>? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_manage, container, false)
        rvUserList = view.findViewById(R.id.rv_user_list)
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
        val layoutManager = LinearLayoutManager(myActivity)
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3、设置recyclerView的布局管理器
        rvUserList!!.layoutManager = layoutManager
        //==2、实例化适配器
        //=2.1、初始化适配器
        mUserAdapter = UserAdapter(llEmpty, rvUserList)
        //=2.3、设置recyclerView的适配器
        rvUserList!!.adapter = mUserAdapter
        loadData()
    }

    /**
     * 加载数据
     */
    private fun loadData() {
        val content = etQuery!!.text.toString() //获取搜索内容
        mUsers = if ("" == content) {
            LitePal.where("account != ?", "admin").find(
                User::class.java
            ) //查询全部
        } else {
            LitePal.where("account like ?", "%$content%").find(
                User::class.java
            ) //通过账号模糊查询用户
        }
        Collections.reverse(mUsers)
        if (mUsers != null && mUsers!!.size > 0) {
            rvUserList!!.visibility = View.VISIBLE
            llEmpty!!.visibility = View.GONE
            mUserAdapter!!.addItem(mUsers)
        } else {
            rvUserList!!.visibility = View.GONE
            llEmpty!!.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            initView()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}
