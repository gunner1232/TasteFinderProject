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
 * order
 */
class OrderFragment : Fragment() {
    private var myActivity: Activity? = null
    private var llEmpty: LinearLayout? = null
    private var rvOrderList: RecyclerView? = null
    var mOrderAdapter: OrderAdapter? = null
    private var mIsAdmin: Boolean? = null
    private var etQuery: EditText? = null //Search content
    private var ivSearch: ImageView? = null //Search icon
    private var mOrder: List<Orders?>? = ArrayList()
    private var account: String? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        rvOrderList = view.findViewById(R.id.rv_order_list)
        llEmpty = view.findViewById(R.id.ll_empty)
        etQuery = view.findViewById(R.id.et_query)
        ivSearch = view.findViewById(R.id.iv_search)
        //Get Control
        initView()
        //Soft keyboard search
        ivSearch?.setOnClickListener(View.OnClickListener {
            loadData() //load data
        })
        //click the search in the soft keyboard
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
        mIsAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
        account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
        val layoutManager = LinearLayoutManager(myActivity)
        //=1.2. Set to vertical arrangement using the setOrientation method (default is vertical layout)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3. Set up the layout manager for recyclerView
        rvOrderList!!.layoutManager = layoutManager
        //==2. Instantiate adapter
        //=2.1 Initialize adapter
        mOrderAdapter = OrderAdapter(llEmpty, rvOrderList)
        //=2.3. Setting up the RecyclerView adapter
        rvOrderList!!.adapter = mOrderAdapter
        loadData() //load data
    }

    /**
     * load data
     */
    private fun loadData() {
        val content = etQuery!!.text.toString() //Retrieve search content
        mOrder = if ("" == content && !mIsAdmin!!) {
            LitePal.where("account = ? ", account).find(Orders::class.java) //Search all
        } else {
            LitePal.where("number like ? and account != ?", "%$content%", "admin").find(
                Orders::class.java
            ) //Fuzzy query of comments through title
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
