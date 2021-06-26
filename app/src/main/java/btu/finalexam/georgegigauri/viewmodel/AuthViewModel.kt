package btu.finalexam.georgegigauri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import btu.finalexam.georgegigauri.data.model.User
import btu.finalexam.georgegigauri.data.repository.AuthRepository
import btu.finalexam.georgegigauri.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UIState<User>> = MutableStateFlow(UIState.Empty())
    val uiState: StateFlow<UIState<User>> = _uiState.asStateFlow()

    fun login(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        authRepository.login(email, password).collect {
            _uiState.value = it
        }
    }

    fun register(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        authRepository.register(email, password).collect {
            _uiState.value = it
        }
    }
}