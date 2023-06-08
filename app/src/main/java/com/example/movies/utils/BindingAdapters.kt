package com.example.movies.utils

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.movies.R

fun ImageView.loadImage(
    imageUrl: String?,
    themoviedb: Boolean? = true,
    placeholder: Int = R.drawable.movie_placeholder,
    error: Int = R.drawable.movie_placeholder,
    imageWidth: Int? = null,
) {
    val url = when {
        themoviedb != false && imageUrl != null -> imageWidth?.let {
            ImageHelper.getImage(it, imageUrl)
        } ?: ImageHelper.getImage(imageUrl)

        else -> imageUrl
    }
    Glide.with(this).load(url)
        .placeholder(placeholder)
        .error(error)
        .into(this)
}

fun TextView.yearFromDate(date: String?) {
    text = if (date.isNullOrEmpty()) {
        "Unknown"
    } else {
        date.split('-').first()
    }
}
