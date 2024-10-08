package com.Weekly.android.service

import kotlin.math.max

object TextFormatter {

    fun lazyColumnItemName(itemName: String): String{
        val maxSize = 14
        if(itemName.length <= maxSize){
            return itemName
        }
        var formatted = itemName.substring(0, maxSize)
        formatted+="..."
        return formatted
    }
}