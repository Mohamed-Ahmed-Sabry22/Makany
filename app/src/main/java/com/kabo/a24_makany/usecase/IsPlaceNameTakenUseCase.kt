    package com.kabo.a24_makany.usecase

    import com.kabo.a24_makany.data.repository.PlacesRepository
    import kotlinx.coroutines.flow.first

    class IsPlaceNameTakenUseCase(private val repo : PlacesRepository) {
            suspend operator fun invoke(name : String): Boolean{
                val places = repo.getAllPlaces().first()
                return places.any { it.name == name }
            }
    }