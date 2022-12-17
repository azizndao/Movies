package com.example.movies.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.movies.R

fun ImageView.loadImage(
    imageUrl: String?,
    themoviedb: Boolean = true,
    @DrawableRes placeholder: Int = R.drawable.loading,
    @DrawableRes error: Int? = null,
    allowHardware: Boolean = true,
    imageWidth: Int? = null,
    onSuccess: (request: ImageRequest, result: SuccessResult) -> Unit = { _, _ -> }
) {
    val url = when {
        themoviedb && imageUrl != null -> imageWidth?.let {
            ImageHelper.getImage(it, imageUrl)
        } ?: ImageHelper.getImage(imageUrl)

        else -> imageUrl
    }
    load(url) {
        placeholder(placeholder)
        if (error != null) error(error)
        allowHardware(allowHardware)
        listener(onSuccess = onSuccess)
    }
}

fun TextView.yearFromDate(date: String?) {
    text = if (date.isNullOrEmpty()) {
        "Unknown"
    } else {
        date.split('-').first()
    }
}
