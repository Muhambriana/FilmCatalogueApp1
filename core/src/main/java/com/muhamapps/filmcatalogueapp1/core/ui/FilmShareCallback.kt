package com.muhamapps.filmcatalogueapp1.core.ui

import com.muhamapps.filmcatalogueapp1.core.domain.model.Film

interface FilmShareCallback {
    fun onShareClick(data: Film)
}
