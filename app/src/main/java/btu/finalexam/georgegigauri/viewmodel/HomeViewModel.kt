package btu.finalexam.georgegigauri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fireStore: FirebaseFirestore
) : ViewModel() {

    val _sort: MutableStateFlow<SortBy> = MutableStateFlow(SortBy.DEFAULT)

    private val _carsUiState: MutableStateFlow<UIState<List<Car>>> =
        MutableStateFlow(UIState.Empty())
    val carsUiState: StateFlow<UIState<List<Car>>> = _carsUiState.asStateFlow()

    init {
        getCars()
    }

    fun refresh() = getCars()

    private fun getCars(sortBy: SortBy = SortBy.DEFAULT) = viewModelScope.launch(Dispatchers.IO) {
        _carsUiState.value = UIState.Loading()
        val result = fireStore.collection("cars").get().await()
        val list = result.toObjects(Car::class.java)
        _carsUiState.value = UIState.Success(list)
    }

    enum class SortBy {
        DEFAULT,
        ASCENDING,
        DESCENDING,
        COMMENT,
        BRAND,
        AUTHOR
    }
}