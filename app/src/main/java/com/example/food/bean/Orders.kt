package com.example.food.bean

import org.litepal.crud.LitePalSupport
import java.io.Serializable

class Orders(
    var account: String, //account
    var title: String, // title
    var number: String, // number
    var amount: String, // amount
    var date: String     //data
) : LitePalSupport(), Serializable
