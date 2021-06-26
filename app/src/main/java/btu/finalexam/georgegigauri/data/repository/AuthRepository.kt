package btu.finalexam.georgegigauri.data.repository

import btu.finalexam.georgegigauri.data.model.User
import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun login(email: String, password: String) = flow<UIState<User>> {
        emit(UIState.Loading())
    }.flowOn(Dispatchers.IO)

    fun register(email: String, password: String) = flow<UIState<User>> {
        emit(UIState.Loading())
    }.flowOn(Dispatchers.IO)
}