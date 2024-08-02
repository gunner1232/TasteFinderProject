package com.example.food.bean

import org.litepal.crud.LitePalSupport

/**
 * browse history
 */
class Browse(//username
    var account: String, //name of the food
    var title: String
) : LitePalSupport()
