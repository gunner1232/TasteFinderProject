package com.example.food.bean

import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * Food items
 */
class Fruit(//Type 0: Technology, 1: Entertainment, 2: Sports, 3: Military, 4: Automotive, 5: Health
    var typeId: Int, //Title
    var title: String, //Pic
    var img: String, //Content
    var content: String, //publisher
    var issuer: String, //Time
    var date: String
) : LitePalSupport(), Serializable
