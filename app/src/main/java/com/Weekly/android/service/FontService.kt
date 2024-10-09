package com.Weekly.android.service

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.Weekly.android.R

object FontService {

    private val fontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs


    )
    private fun getFont(fontName: String): FontFamily {
        val fontFamily = FontFamily(
            Font(
            googleFont = GoogleFont(fontName),
            fontProvider = fontProvider
        )
        )
        return fontFamily
    }

    val montserrat = getFont("Montserrat")

    val oswald = getFont("Oswald")
}