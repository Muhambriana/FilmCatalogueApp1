package com.muhamapps.filmcatalogueapp1.di

import com.muhamapps.filmcatalogueapp1.core.domain.usecase.FilmInteractor
import com.muhamapps.filmcatalogueapp1.core.domain.usecase.FilmUseCase
import com.muhamapps.filmcatalogueapp1.detail.DetailFilmViewModel
import com.muhamapps.filmcatalogueapp1.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<FilmUseCase> { FilmInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailFilmViewModel(get()) }
}