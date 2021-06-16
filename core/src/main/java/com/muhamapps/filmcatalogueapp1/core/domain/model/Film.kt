package com.muhamapps.filmcatalogueapp1.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    val movieId: String,
    val title: String,
    val rating: String,
    val description: String,
    val poster: String,
    val isFavorite: Boolean
) : Parcelable