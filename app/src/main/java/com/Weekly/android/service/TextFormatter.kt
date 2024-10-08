package com.Weekly.android.service

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.Weekly.android.R
import kotlin.math.max
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font

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