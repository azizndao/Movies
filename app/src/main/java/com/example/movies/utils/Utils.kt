package com.example.movies.utils

import android.content.Context
import android.util.DisplayMetrics

fun Float.toPx(context: Context): Float {
    return this * (context.resources
        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Int.toPx(context: Context): Float = toFloat().toPx(context)

fun Float.toDp(context: Context): Float {
    return this / (context.resources
        .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Int.toDp(context: Context): Float = toFloat().toDp(context)