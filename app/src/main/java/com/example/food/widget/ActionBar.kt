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
 * customizable ActionBar
 */
class ActionBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    private var llActionbarRoot: LinearLayout? = null //Customize ActionBar root node
    private var vStatusBar: View? = null //Status bar position
    private var ivLeft: ImageView? = null //Left icon
    private var tvLeft: TextView? = null //Left
    private var tvTitle: TextView? = null //Middle title
    private var ivRight: ImageView? = null //Right icon
    private var btnRight: Button? = null //Right button
    private var tvRight: TextView? = null //right-hand text

    init {
        init(context)
    }

    /**
     * Setting the title
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
     * Setting the left side text
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
     * Setting the left icon
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
     * Setting the right side text
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
     * Set the right button
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
     * Setting the right icon
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
        orientation = HORIZONTAL //Setting the horizontal layout
        val contentView = inflate(getContext(), R.layout.widget_actionbar, this)
        //Getting Controls
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
     * Setting transparency
     *
     * @param transAlpha{Integer} between 0-255
     */
    fun setTranslucent(transAlpha: Int) {
        //Setting transparency
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
     * Setup Data
     *
     * @param strTitle   Title
     * @param resIdLeft  Left Icon Resources
     * @param resIdRight Right Icon Resources
     * @param intColor   Content color 0 is white 1 is black
     * @param backgroundColor   background color
     * @param listener   Click event listener
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
            llActionbarRoot!!.setBackgroundColor(backgroundColor) //Setting the background color of the title bar
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
            llActionbarRoot!!.setBackgroundColor(backgroundColor) //Setting the background color of the title bar
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
            llActionbarRoot!!.setBackgroundColor(backgroundColor) //Setting the background color of the title bar
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
        //Left click
        fun onLeftClick()

        //Right click
        fun onRightClick()
    }
}
