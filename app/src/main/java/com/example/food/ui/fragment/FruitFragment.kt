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
 * fruit
 */
class FruitFragment : Fragment() {
    private var myActivity: Activity? = null //context
    private var tabTitle: TabLayout? = null
    private var rvfruitList: RecyclerView? = null
    private var mfruitAdapter: FruitAdapter? = null
    private var llEmpty: LinearLayout? = null
    private var mIsAdmin: Boolean? = null
    private var etQuery: EditText? = null //search content
    private var ivSearch: ImageView? = null //search icon
    private var btnAdd: FloatingActionButton? = null
    private val state = arrayOf("0", "1", "2", "3", "4", "5")
    private val title = arrayOf("Pizza", "Burger", "Sandwich", "Sushi", "Drink")
    private var typeId = "0"
    private var mfruit: List<Fruit>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }
    
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




    //Get Control
    initView()
    //Soft keyboard search
    ivSearch?.setOnClickListener {
        loadData() //load data
    }
    //click the search in the soft keyboard
    etQuery?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            loadData() //load data
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
     * Initialize page
     */
    private fun initView() {
        mIsAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
//        btnAdd!!.visibility = if (mIsAdmin!!) View.VISIBLE else View.GONE

        btnAdd!!.visibility =  View.GONE
        tabTitle!!.tabMode = TabLayout.MODE_SCROLLABLE

        //Add tab names to TabLayout
        for (i in title.indices) {
            tabTitle!!.addTab(tabTitle!!.newTab().setText(title[i]))
        }
        val layoutManager = LinearLayoutManager(myActivity)
        //=1.2. Set to vertical arrangement using the setOrientation method (default is vertical layout)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3. Set up the layout manager for recyclerView
        rvfruitList!!.layoutManager = layoutManager
        //==2. Instantiate adapter
        //=2.1 Initialize adapter
        mfruitAdapter = FruitAdapter(llEmpty, rvfruitList)
        //=2.3. Setting up the RecyclerView adapter
        rvfruitList!!.adapter = mfruitAdapter
        loadData()



        mfruitAdapter!!.setItemListener(object : FruitAdapter.ItemListener {
            override fun ItemClick(fruit: Fruit?) { val isAdmin = SPUtils.get(myActivity, SPUtils.IS_ADMIN, false) as Boolean
                val account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
                if (account.isEmpty()) { // Not logged in, redirected to login page
//                    MyApplication.Instance.getMainActivity().finish()
                    MyApplication.Instance?.mainActivity?.finish()
                    startActivity(Intent(myActivity, LoginActivity::class.java))
                } else { // already logged
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
        val content = etQuery!!.text.toString() //Retrieve search content
        mfruit = if ("" == content) {
            LitePal.where("typeId = ?", typeId).find(Fruit::class.java)
        } else {
            LitePal.where("typeId = ? and title like ?", typeId, "%$content%").find(
                Fruit::class.java
            ) //Fuzzy query of comments through title
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
            loadData() //load data
        }
    }
}
