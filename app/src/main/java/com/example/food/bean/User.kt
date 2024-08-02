package com.example.food.bean

import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * user
 */
class User(
    var account: String, //account
    var password: String, //password
    var nickName: String, //nickName
    var age: Int, //age
    var email: String //email
) : LitePalSupport(), Serializable
