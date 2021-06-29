package btu.finalexam.georgegigauri.data.repository

import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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

    fun register(name: String, email: String, password: String) = flow {
        emit(UIState.Loading())

        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(
            UserProfileChangeRequest.Builder().apply {
                displayName = name
            }.build()
        )

        emit(UIState.Success(result.user!!))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }
        .flowOn(Dispatchers.IO)

    fun resetPassword(email: String) = flow<UIState<FirebaseUser>> {
        emit(UIState.Loading())

        auth.sendPasswordResetEmail(email).await()

        emit(UIState.Error("პაროლის აღსადგენი ბმული გამოგზავნილია ელ. ფოსტაზე."))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }
        .flowOn(Dispatchers.IO)


}