package com.example.food.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food.R
import com.example.food.adapter.BrowseAdapter
import com.example.food.bean.Browse
import com.example.food.bean.Fruit
import com.example.food.util.SPUtils
import com.example.food.widget.ActionBar
import com.example.food.widget.ActionBar.ActionBarClickListener
import org.litepal.LitePal

//import org.litepal.crud.DataSupport

/**
 * Browsing History
 */
class BrowseActivity : AppCompatActivity() {
    private var myActivity: Activity? = null
    private var mTitleBar: ActionBar? = null //Title bar
    private var llEmpty: LinearLayout? = null
    private var rvBrowseList: RecyclerView? = null
    private var mBrowseAdapter: BrowseAdapter? = null
    private var mBrowses: List<Browse>? = null
    private var account: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)
        myActivity = this
        rvBrowseList = findViewById(R.id.rv_collect_list)
        llEmpty = findViewById(R.id.ll_empty)
        mTitleBar = findViewById<View>(R.id.myActionBar) as ActionBar
        mTitleBar!!.setData(
            myActivity,
            "Browsing History",
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
        initView()
    }

    private fun initView() {
        account = SPUtils.get(myActivity, SPUtils.ACCOUNT, "") as String
        val layoutManager = LinearLayoutManager(myActivity)
        //=1.2、Set to vertical arrangement，Set with setOrientation method (default is vertical layout)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        //=1.3、Set up a layout manager for recyclerView
        rvBrowseList!!.layoutManager = layoutManager
        //==2、Instantiate adapter
        //=2.1、Initialize adapter
        mBrowseAdapter = BrowseAdapter(llEmpty, rvBrowseList!!)
        //=2.3、Set up the adapter for recyclerView
        rvBrowseList!!.adapter = mBrowseAdapter
        loadData() //load data

        mBrowseAdapter?.setItemListener(object : BrowseAdapter.ItemListener {

            override fun ItemClick(collect: Browse?) {
                val intent = Intent(myActivity, FruitDetailActivity::class.java)
                val fruit = LitePal.where("title = ?", collect?.title).findFirst(Fruit::class.java)
                intent.putExtra("fruit", fruit)
                startActivityForResult(intent, 100)
            }
        })
    }

    /**
     * load data
     */
    private fun loadData() {
        mBrowses = LitePal.where("account = ?", account).find(Browse::class.java) //Search browsing history
        if (mBrowses != null && mBrowses!!.size > 0) {
            rvBrowseList!!.visibility = View.VISIBLE
            llEmpty!!.visibility = View.GONE
            mBrowseAdapter!!.addItem(mBrowses)
        } else {
            rvBrowseList!!.visibility = View.GONE
            llEmpty!!.visibility = View.VISIBLE
        }
    }

    public override fun onResume() {
        super.onResume()
        loadData()
    }
}
