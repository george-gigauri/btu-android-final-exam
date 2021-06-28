package btu.finalexam.georgegigauri.viewmodel

import android.net.Uri
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
class AddCarViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _uiState : MutableStateFlow<UIState<Car>> =
        MutableStateFlow(UIState.Empty())

    val uiState : StateFlow<UIState<Car>> = _uiState.asStateFlow()

    fun addCar(brand : String, model : String, description : String, image : Uri) =
        viewModelScope.launch(Dispatchers.IO) {
            carRepository.addCar(brand,model,description,image).collect {
                _uiState.value = it
            }
        }
}