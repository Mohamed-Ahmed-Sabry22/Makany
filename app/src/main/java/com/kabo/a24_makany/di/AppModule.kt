package com.kabo.a24_makany.di

import com.kabo.a24_makany.data.local.PlacesDatabase
import com.kabo.a24_makany.data.repository.PlacesRepository
import com.kabo.a24_makany.data.location.GeocoderHandler
import com.kabo.a24_makany.data.location.LocationHandler
import com.kabo.a24_makany.ui.screens.home.MapHomeViewModel
import com.kabo.a24_makany.ui.screens.places.PlacesViewModel
import com.kabo.a24_makany.usecase.IsPlaceNameTakenUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { PlacesDatabase.getInstance(androidContext()) }
    single { get<PlacesDatabase>().dao }
    single { PlacesRepository(get()) }
    single { LocationHandler(androidContext()) }
    single { GeocoderHandler(androidContext()) }
    single { IsPlaceNameTakenUseCase(get()) }

    viewModel { PlacesViewModel(get() , get()) }
    viewModel { MapHomeViewModel(get() , get()) }
}