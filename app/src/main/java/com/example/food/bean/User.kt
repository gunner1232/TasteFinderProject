package com.example.food.bean

import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * 用户
 */
class User(//账号
    var account: String, //密码
    var password: String, //昵称
    var nickName: String, //年龄
    var age: Int, //邮箱
    var email: String
) : LitePalSupport(), Serializable
