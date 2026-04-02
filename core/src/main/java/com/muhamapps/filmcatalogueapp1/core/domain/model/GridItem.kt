package com.muhamapps.filmcatalogueapp1.core.domain.model

sealed class GridItem {
    data class Content(val film: Film): GridItem()
    object Ad: GridItem()
}