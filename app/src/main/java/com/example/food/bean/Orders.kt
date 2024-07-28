package com.example.food.bean

import org.litepal.crud.DataSupport
import java.io.Serializable

class Orders(//账号
    var account: String, //标题
    var title: String, //编号
    var number: String, //数量
    var amount: String, //时间
    var date: String
) : DataSupport(), Serializable
