package com.example.movies.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter(
    "imageUrl",
    "themoviedbUrl",
    "placeholder",
    "error",
    "allowHardware",
    requireAll = false
)
fun ImageView.loadImage(
    imageUrl: String?,
    themoviedb: Boolean?,
    placeholder: Drawable?,
    error: Drawable?,
    allowHardware: Boolean?,
) {
    val url = when {
        themoviedb != false && imageUrl != null -> "${Constants.MOVIE_IMAGE_BASE_URL}${imageUrl}"
        else -> imageUrl
    }
    load(url) {
        if (placeholder != null) placeholder(placeholder)
        if (error != null) error(error)
        allowHardware(allowHardware ?: true)
    }
}

@BindingAdapter("yearFromDate")
fun TextView.yearFromDate(date: String?) {
    text = if (date.isNullOrEmpty()) {
        "Unknown"
    } else {
        date.split('-').first()
    }
}
