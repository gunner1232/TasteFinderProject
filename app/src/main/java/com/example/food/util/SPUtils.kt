package com.example.food.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Data persistence tool class
 */
object SPUtils {
    const val IF_FIRST: String = "is_first" //first time login or not
    const val IS_ADMIN: String = "is_admin" //admin or not
    const val ACCOUNT: String = "account" //account

    /**
     * The file name saved on the phone
     */
    private const val FILE_NAME = "share_data"

    /**
     * To save data, we need to obtain the specific type of data to be saved,
     * and then call different saving methods based on the type
     *
     * @param context content
     * @param key     key
     * @param `object`  value
     */
    fun put(context: Activity?, key: String?, `object`: Any) {
        val sp = context?.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp?.edit()

        if (`object` is String) {
            editor?.putString(key, `object`)
        } else if (`object` is Int) {
            editor?.putInt(key, `object`)
        } else if (`object` is Boolean) {
            editor?.putBoolean(key, `object`)
        } else if (`object` is Float) {
            editor?.putFloat(key, `object`)
        } else if (`object` is Long) {
            editor?.putLong(key, `object`)
        } else {
            editor?.putString(key, `object`.toString())
        }

        editor?.let { SharedPreferencesCompat.apply(it) }
    }

    /**
     * Get the method to save data. We get the specific type of saved data according to the default value,
     * and then call the corresponding method to get the value
     *
     * @param context       content
     * @param key           key
     * @param defaultObject default value
     * @return value
     */
    fun get(context: Context?, key: String?, defaultObject: Any?): Any? {
        val sp = context!!.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )

        if (defaultObject is String) {
            return sp.getString(key, defaultObject as String?)
        } else if (defaultObject is Int) {
            return sp.getInt(key, (defaultObject as Int?)!!)
        } else if (defaultObject is Boolean) {
            return sp.getBoolean(key, (defaultObject as Boolean?)!!)
        } else if (defaultObject is Float) {
            return sp.getFloat(key, (defaultObject as Float?)!!)
        } else if (defaultObject is Long) {
            return sp.getLong(key, (defaultObject as Long?)!!)
        }

        return null
    }

    /**
     * Remove the value that already corresponds to a certain key value
     *
     * @param context content
     * @param key     key
     */
    fun remove(context: Context?, key: String?) {
        val sp = context!!.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * clear all data
     *
     * @param context content
     */
    fun clear(context: Context) {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * Check if a key already exists
     *
     * @param context content
     * @param key     key
     * @return exist or not
     */
    fun contains(context: Context, key: String?): Boolean {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.contains(key)
    }

    /**
     * return all the key values
     *
     * @param context content
     * @return all the key values
     */
    fun getAll(context: Context): Map<String, *> {
        val sp = context.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.all
    }

    /**
     * Create a compatibility class that solves the SharedReferenceCompat.apply method
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod = findApplyMethod()

        /**
         * Reflection based method for finding 'apply'
         *
         * @return Method
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }

            return null
        }

        /**
         * If found, use apply to execute; otherwise, use commit
         *
         * @param editor SharedPreferences.Edito
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            editor.commit()
        }
    }
}