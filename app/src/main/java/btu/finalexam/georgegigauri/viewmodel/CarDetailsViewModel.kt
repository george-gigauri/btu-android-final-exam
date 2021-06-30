package btu.finalexam.georgegigauri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.data.model.Comment
import btu.finalexam.georgegigauri.data.repository.CarRepository
import btu.finalexam.georgegigauri.data.repository.CommentRepository
import btu.finalexam.georgegigauri.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarDetailsViewModel @Inject constructor(
    private val carRepository: CarRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Car>>(UIState.Empty())
    val uiState = _uiState.asStateFlow()

    private val _commentsUiState = MutableStateFlow<UIState<List<Comment>>>(UIState.Empty())
    val commentsUiState = _commentsUiState.asStateFlow()

    fun load(id: String) = viewModelScope.launch(Dispatchers.IO) {
        carRepository.get(id).collect {
            _uiState.value = it
        }
    }

    fun loadComments(id: String) = viewModelScope.launch(Dispatchers.IO) {
        commentRepository.getComments(id).collect {
            _commentsUiState.value = it
        }
    }

    fun addComment(carId: String, comment: String) = viewModelScope.launch(Dispatchers.IO) {
        commentRepository.addComment(carId, comment).collect {
            _commentsUiState.value = it
        }
    }
}