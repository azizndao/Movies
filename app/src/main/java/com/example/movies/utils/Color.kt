package com.example.movies.utils

import android.graphics.Color
import com.example.movies.R
import kotlin.math.sqrt

fun isBrightColor(color: Int): Boolean {
    if (R.color.transparent == color) return true
    var rtnValue = false
    val rgb = intArrayOf(Color.red(color), Color.green(color), Color.blue(color))
    val brightness = sqrt(
        rgb[0] * rgb[0] * .241 + (rgb[1] * rgb[1] * .691) + rgb[2] * rgb[2] * .068
    ).toInt()

    // color is light
    if (brightness >= 200) {
        rtnValue = true
    }
    return rtnValue
}