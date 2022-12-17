package com.example.movies.ui.moviedetails

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
enum class DataType : Parcelable {
    MOVIE, TV_SHOW
}