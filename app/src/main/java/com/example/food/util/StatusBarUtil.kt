package com.example.food.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.example.food.R

/**
 * Full screen display status bar tool class
 */
object StatusBarUtil {
    /**
     * Modify the display mode of the current activity, hideStatsBrBackground: true full screen mode, false shading mode
     *
     * @param activity
     * @param hideStatusBarBackground
     */
    fun setStatusBar(activity: Activity?, hideStatusBarBackground: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val window = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (hideStatusBarBackground) {
                window?.decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }

            val mContentView = window?.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
            val mChildView = mContentView.getChildAt(0)
            if (mChildView != null) {
                if (hideStatusBarBackground) {
                    mChildView.setPadding(
                        mChildView.paddingLeft,
                        0,
                        mChildView.paddingRight,
                        mChildView.paddingBottom
                    )
                } else {
                    val statusHeight = getStatusBarHeight(activity)
                    mChildView.setPadding(
                        mChildView.paddingLeft,
                        statusHeight,
                        mChildView.paddingRight,
                        mChildView.paddingBottom
                    )
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (hideStatusBarBackground) {
                window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window?.statusBarColor = Color.TRANSPARENT
                window?.decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else {
                activity?.resources?.getColor(R.color.colorPrimary)
                    .also {
                        if (it != null) {
                            window?.statusBarColor = it
                        }
                    }
                window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }

    /**
     * Get the height of the phone status bar
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * Set the color of the status bar
     */

    @TargetApi(19)
    fun transparentBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
    }

    /**
     * Set the status bar text color to true black and false white (XML and layout configuration Android: fitsSystemWindows="true",
     * otherwise the layout will move up)
     */
    fun setStatusBarLightMode(activity: Activity?, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (dark) {  //
                if (MIUISetStatusBarLightMode(activity, dark)) {
                } else if (FlymeSetStatusBarLightMode(activity?.window, dark)) {
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //其他
                    activity?.window?.decorView?.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            } else {
                if (MIUISetStatusBarLightMode(activity, dark)) {
                } else if (FlymeSetStatusBarLightMode(activity?.window, dark)) {
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }
        }
    }

    /**
     * Set the status bar icon to dark color and Meizu specific text style
     * Can be used to determine if you are a Flyme user
     *
     * @param window Window that needs to be set
     * @param dark   Should the status bar text and icon colors be set to dark
     * @return boolean Successful execution returns true
     */
    fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                    .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * Need MIUIV6 or above
     *
     * @param activity
     * @param dark     Should the status bar text and icon colors be set to dark
     * @return boolean Successful execution returns true
     */
    fun MIUISetStatusBarLightMode(activity: Activity?, dark: Boolean): Boolean {
        var result = false
        val window = activity?.window
        if (window != null) {
            val clazz: Class<*> = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag) //The status bar is transparent and in black font
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag) //clear the black font
                }
                result = true

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (dark) {
                        activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }
            } catch (e: Exception) {
            }
        }
        return result
    }
}