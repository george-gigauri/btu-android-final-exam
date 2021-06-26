package btu.finalexam.georgegigauri.data.repository

import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    fun login(email: String, password: String) = flow {
        emit(UIState.Loading())

        val result = auth.signInWithEmailAndPassword(email, password).await()
        emit(UIState.Success(result.user!!))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }
        .flowOn(Dispatchers.IO)

    fun register(email: String, password: String) = flow<UIState<FirebaseUser>> {
        emit(UIState.Loading())
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }
        .flowOn(Dispatchers.IO)
}