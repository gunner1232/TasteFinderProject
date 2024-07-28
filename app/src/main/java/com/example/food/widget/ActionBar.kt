package com.example.food.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.example.food.R


/**
 * 自定义ActionBar
 */
class ActionBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    private var llActionbarRoot: LinearLayout? = null //自定义ActionBar根节点
    private var vStatusBar: View? = null //状态栏位置
    private var ivLeft: ImageView? = null //左边图标
    private var tvLeft: TextView? = null //左边
    private var tvTitle: TextView? = null //中间标题
    private var ivRight: ImageView? = null //右边图标
    private var btnRight: Button? = null //右边按钮
    private var tvRight: TextView? = null //右边文字

    init {
        init(context)
    }

    /**
     * 设置标题
     * @param title
     */
    fun setTitle(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle!!.text = title
        } else {
            tvTitle!!.visibility = GONE
        }
    }

    /**
     * 设置左侧文本
     * @param text
     */
    fun setLeftText(text: String?) {
        if (!TextUtils.isEmpty(text)) {
            tvLeft!!.text = text
            tvLeft!!.visibility = VISIBLE
        } else {
            tvLeft!!.visibility = GONE
        }
    }

    /**
     * 设置左侧图标
     * @param ico
     */
    fun setLeftIco(ico: Int) {
        if (ico != 0) {
            ivLeft!!.setImageResource(ico)
            ivLeft!!.visibility = VISIBLE
        } else {
            ivLeft!!.visibility = GONE
        }
    }


    /**
     * 设置右侧文本
     * @param text
     */
    fun setRightText(text: String?) {
        if (!TextUtils.isEmpty(text)) {
            tvRight!!.text = text
            tvRight!!.visibility = VISIBLE
        } else {
            tvRight!!.visibility = GONE
        }
    }

    /**
     * 设置右侧btn
     * @param text
     */
    fun setRightBtn(text: String?) {
        if (!TextUtils.isEmpty(text)) {
            btnRight!!.text = text
            btnRight!!.visibility = VISIBLE
        } else {
            btnRight!!.visibility = GONE
        }
    }

    /**
     * 设置右侧图标
     * @param ico
     */
    fun setRightIco(ico: Int) {
        if (ico != 0) {
            ivRight!!.setImageResource(ico)
            ivRight!!.visibility = VISIBLE
        } else {
            ivRight!!.visibility = GONE
        }
    }

    private fun init(context: Context) {
        orientation = HORIZONTAL //设置横向布局
        val contentView = inflate(getContext(), R.layout.widget_actionbar, this)
        //获取控件
        llActionbarRoot = contentView.findViewById<View>(R.id.ll_actionbar_root) as LinearLayout
        vStatusBar = contentView.findViewById(R.id.v_statusbar)
        ivLeft = contentView.findViewById<View>(R.id.iv_actionbar_left) as ImageView
        tvLeft = contentView.findViewById<View>(R.id.tv_actionbar_left) as TextView
        tvTitle = contentView.findViewById<View>(R.id.tv_actionbar_title) as TextView
        ivRight = contentView.findViewById<View>(R.id.iv_actionbar_right) as ImageView
        btnRight = contentView.findViewById<View>(R.id.btn_actionbar_right) as Button
        tvRight = contentView.findViewById<View>(R.id.tv_actionbar_right) as TextView
    }

    /**
     * 设置透明度
     *
     * @param transAlpha{Integer} 0-255 之间
     */
    fun setTranslucent(transAlpha: Int) {
        //设置透明度
        llActionbarRoot!!.setBackgroundColor(
            ColorUtils.setAlphaComponent(
                resources.getColor(R.color.colorPrimary),
                transAlpha
            )
        )
        tvTitle!!.alpha = transAlpha.toFloat()
        ivLeft!!.setAlpha(transAlpha)
        ivRight!!.setAlpha(transAlpha)
    }

    /**
     * 设置数据
     *
     * @param strTitle   标题
     * @param resIdLeft  左边图标资源
     * @param resIdRight 右边图标资源
     * @param intColor   内容颜色 0为白色 1为黑色
     * @param backgroundColor   背景颜色
     * @param listener   点击事件监听
     */
    fun setData(
        strTitle: String?,
        resIdLeft: Int,
        resIdRight: Int,
        intColor: Int,
        backgroundColor: Int,
        listener: ActionBarClickListener?
    ) {
        val textColor = if (intColor == 0) "#FFFFFF" else "#000000"
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle!!.text = strTitle
            tvTitle!!.setTextColor(Color.parseColor(textColor))
        } else {
            tvTitle!!.visibility = GONE
        }
        if (resIdLeft == 0) {
            ivLeft!!.visibility = GONE
        } else {
            ivLeft!!.setBackgroundResource(resIdLeft)
            ivLeft!!.visibility = VISIBLE
        }
        if (resIdRight == 0) {
            ivRight!!.visibility = GONE
        } else {
            ivRight!!.setBackgroundResource(resIdRight)
            ivRight!!.visibility = VISIBLE
        }

        if (backgroundColor == 0) {
            llActionbarRoot!!.setBackgroundResource(0)
        } else {
            llActionbarRoot!!.setBackgroundColor(backgroundColor) //设置标题栏背景颜色
        }
        if (listener != null) {
            ivLeft!!.setOnClickListener { listener.onLeftClick() }
            ivRight!!.setOnClickListener { listener.onRightClick() }
        }
    }

    fun setData(
        context: Activity?,
        strTitle: String?,
        resIdLeft: Int,
        resIdRight: Int,
        intColor: Int,
        backgroundColor: Int,
        listener: ActionBarClickListener?
    ) {
        val textColor = if (intColor == 0) "#FFFFFF" else "#000000"
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle!!.text = strTitle
            tvTitle!!.setTextColor(Color.parseColor(textColor))
        } else {
            tvTitle!!.visibility = GONE
        }
        if (resIdLeft == 0) {
            ivLeft!!.visibility = GONE
        } else {
            ivLeft!!.setBackgroundResource(resIdLeft)
            ivLeft!!.visibility = VISIBLE
        }
        if (resIdRight == 0) {
            ivRight!!.visibility = GONE
        } else {
            ivRight!!.setBackgroundResource(resIdRight)
            ivRight!!.visibility = VISIBLE
        }

        if (backgroundColor == 0) {
            llActionbarRoot!!.setBackgroundResource(0)
        } else {
            llActionbarRoot!!.setBackgroundColor(backgroundColor) //设置标题栏背景颜色
        }
        if (listener != null) {
            ivLeft!!.setOnClickListener { listener.onLeftClick() }
            ivRight!!.setOnClickListener { listener.onRightClick() }
        }
    }

    fun setData(
        strTitle: String?,
        resIdLeft: Int,
        strRight: String?,
        intColor: Int,
        backgroundColor: Int,
        listener: ActionBarClickListener?
    ) {
        val textColor = if (intColor == 0) "#FFFFFF" else "#000000"
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle!!.text = strTitle
            tvTitle!!.setTextColor(Color.parseColor(textColor))
        } else {
            tvTitle!!.visibility = GONE
        }
        if (resIdLeft == 0) {
            ivLeft!!.visibility = GONE
        } else {
            ivLeft!!.setBackgroundResource(resIdLeft)
            ivLeft!!.visibility = VISIBLE
        }
        if (!TextUtils.isEmpty(strRight)) {
            btnRight!!.text = strRight
            btnRight!!.visibility = VISIBLE
        } else {
            btnRight!!.visibility = GONE
        }
        if (backgroundColor == 0) {
            llActionbarRoot!!.setBackgroundResource(0)
        } else {
            llActionbarRoot!!.setBackgroundColor(backgroundColor) //设置标题栏背景颜色
        }
        if (listener != null) {
            if (ivLeft!!.visibility == VISIBLE) {
                ivLeft!!.setOnClickListener { listener.onLeftClick() }
            } else if (tvLeft!!.visibility == VISIBLE) {
                tvLeft!!.setOnClickListener { listener.onLeftClick() }
            }


            if (btnRight!!.visibility == VISIBLE) {
                btnRight!!.setOnClickListener { listener.onRightClick() }
            } else if (tvRight!!.visibility == VISIBLE) {
                tvRight!!.setOnClickListener { listener.onRightClick() }
            }
        }
    }

    interface ActionBarClickListener {
        //左边点击
        fun onLeftClick()

        //右边点击
        fun onRightClick()
    }
}
