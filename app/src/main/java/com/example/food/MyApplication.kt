package com.example.food

import android.app.Activity
import android.app.Application
//import org.litepal.LitePal
import org.litepal.LitePal


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Instance = this
        LitePal.initialize(this) //Initialize LitePal database
    }

    var mainActivity: Activity? = null

    companion object {
        var Instance: MyApplication? = null
    }
}