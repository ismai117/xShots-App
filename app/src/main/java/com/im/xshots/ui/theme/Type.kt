package com.im.xshots.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.im.xshots.R

// Set of Material typography styles to start with
val Typography = Typography(

    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )


    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val Fonts = FontFamily(
//    Font(R.font.lato_black),
//    Font(R.font.lato_blackitalic),
    Font(R.font.lato_bold, weight = FontWeight.Bold),
//    Font(R.font.lato_bolditalic, weight = FontWeight.Bold, style = FontStyle.Italic),
//    Font(R.font.lato_italic, style = FontStyle.Italic),
//    Font(R.font.lato_light, weight = FontWeight.Light),
//    Font(R.font.lato_lightitalic, weight = FontWeight.Light, style = FontStyle.Italic),
//    Font(R.font.lato_regular, weight = FontWeight.Normal),
//    Font(R.font.lato_thin, weight = FontWeight.Thin),
//    Font(R.font.lato_thinitalic, weight = FontWeight.Thin, style = FontStyle.Italic)

)