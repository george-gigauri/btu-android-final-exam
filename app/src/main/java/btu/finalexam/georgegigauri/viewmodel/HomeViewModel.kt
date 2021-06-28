package btu.finalexam.georgegigauri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.data.repository.CarRepository
import btu.finalexam.georgegigauri.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _carsUiState: MutableStateFlow<UIState<List<Car>>> =
        MutableStateFlow(UIState.Empty())
    val carsUiState: StateFlow<UIState<List<Car>>> = _carsUiState.asStateFlow()

    init {
        getCars()
    }

    fun refresh(sortBy: SortBy = SortBy.DEFAULT) = getCars(sortBy)

    private fun getCars(sortBy: SortBy = SortBy.DEFAULT) = viewModelScope.launch(Dispatchers.IO) {
        carRepository.getAll(sortBy).collect {
            _carsUiState.value = it
        }
    }

    enum class SortBy {
        DEFAULT {
            override fun toName(): String = "არაფერი"
        },
        BRAND {
            override fun toName(): String = "ბრენდი"
            override fun toString(): String = "brand"
        },
        AUTHOR {
            override fun toName(): String = "ავტორი"
            override fun toString(): String = "authorName"
        };

        abstract fun toName(): String
    }
}