package btu.finalexam.georgegigauri.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import btu.finalexam.georgegigauri.data.model.User
import btu.finalexam.georgegigauri.data.repository.ProfileRepository
import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState : MutableStateFlow<UIState<User>> =
        MutableStateFlow(UIState.Empty())

    val uiState : StateFlow<UIState<User>> = _uiState.asStateFlow()


    fun addUser(uri: Uri) =
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.addUser(uri).collect {
                _uiState.value = it
            }
        }

    fun setNewPassword(password : String) =
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.setNewPassword(password).collect {
                _uiState.value = it
            }
        }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            profileRepository.logOut().collect {
                _uiState.value = it
            }
        }
    }





}