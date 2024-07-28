package com.example.food.bean

import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 * 菜品
 */
class Fruit(//类型 0科技，1娱乐，2体育，3军事,4汽车,5健康
    var typeId: Int, //标题
    var title: String, //图片
    var img: String, //内容
    var content: String, //发布人
    var issuer: String, //时间
    var date: String
) : LitePalSupport(), Serializable
