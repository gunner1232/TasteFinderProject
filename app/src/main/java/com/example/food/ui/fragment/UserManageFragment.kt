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
 * user management
 */
class UserManageFragment : Fragment() {
    private var myActivity: Activity? = null
    private var llEmpty: LinearLayout? = null
    private var rvUserList: RecyclerView? = null
    var mUserAdapter: UserAdapter? = null
    private val btnAdd: FloatingActionButton? = null
    private var etQuery: EditText? = null //search content
    private var ivSearch: ImageView? = null //search icon
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
        //Get Control
        initView()
        //Soft keyboard search
        ivSearch?.setOnClickListener(View.OnClickListener {
            loadData() //load data
        })
        //click search in the soft keyboard
        etQuery?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                loadData() //load data
                return@OnEditorActionListener true
            }
            false
        })
        return view
    }

    private fun initView() {
        val layoutManager = LinearLayoutManager(myActivity)
        //=1.2. Set to vertical arrangement using the setOrientation method (default is vertical layout)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3. Set up the layout manager for recyclerView
        rvUserList!!.layoutManager = layoutManager
        //==2、Instantiate adapter
        //=2.1、Initialize adapter
        mUserAdapter = UserAdapter(llEmpty, rvUserList)
        //=2.3、Set up the adapter for recyclerView
        rvUserList!!.adapter = mUserAdapter
        loadData()
    }

    /**
     * load data
     */
    private fun loadData() {
        val content = etQuery!!.text.toString() //Retrieve search content
        mUsers = if ("" == content) {
            LitePal.where("account != ?", "admin").find(
                User::class.java
            ) //search all
        } else {
            LitePal.where("account like ?", "%$content%").find(
                User::class.java
            ) //Fuzzy query of users through account
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
