package btu.finalexam.georgegigauri.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import btu.finalexam.georgegigauri.data.repository.AuthRepository
import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UIState<FirebaseUser>> =
        MutableStateFlow(UIState.Empty())
    val uiState: StateFlow<UIState<FirebaseUser>> = _uiState.asStateFlow()

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