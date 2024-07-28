package com.example.food.bean

import org.litepal.crud.LitePalSupport

/**
 * 浏览记录
 */
class Browse(//账号
    var account: String, //菜品标题
    var title: String
) : LitePalSupport()
