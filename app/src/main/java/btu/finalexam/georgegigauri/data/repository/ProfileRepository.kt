package btu.finalexam.georgegigauri.data.repository

import android.net.Uri
import btu.finalexam.georgegigauri.data.model.User
import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    fun getUser() = flow {
        emit(UIState.Loading())

        val user = User()
        val result = fireStore.collection("users").document(auth.uid!!).get().await()
        val userResult = result.toObject(User::class.java)
        userResult?.let {
            user.uid = it.uid
            user.email = it.email
            user.name = it.name
            user.image = it.image
        }

        emit(UIState.Success(user))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }.flowOn(Dispatchers.IO)

    fun addUser(uri: Uri) = flow {
        emit(UIState.Loading())

        val currentUser = auth.currentUser

        val imageUrl = addProfileImageToStorage(uri)

        val documentRef = fireStore.collection("users").document(currentUser!!.uid)


        val user = User(currentUser.uid, currentUser.email!!, imageUrl, currentUser.displayName!!)
        documentRef.set(user).await()
        emit(UIState.Success(user))

    }.catch { emit(UIState.Error(it.message ?: "Unknown Error Occurred")) }.flowOn(Dispatchers.IO)

    private suspend fun addProfileImageToStorage(uri: Uri): String {
        val storageReference = storage.reference.child("profiles/" + UUID.randomUUID().toString())

        val result = storageReference.putFile(uri).await()
        return result.metadata?.reference?.downloadUrl?.await().toString()
    }

    fun setNewPassword(password: String) = flow {
        emit(UIState.Loading())

        auth.currentUser?.updatePassword(password)?.await()

        val user = User()
        val result = fireStore.collection("users").document(auth.uid!!).get().await()
        val userResult = result.toObject(User::class.java)
        userResult?.let {
            user.uid = it.uid
            user.email = it.email
            user.name = it.name
            user.image = it.image
        }

        emit(UIState.Success(user))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error Occurred")) }.flowOn(Dispatchers.IO)


    fun logOut() = flow<UIState<User>> {
        emit(UIState.Loading())

        auth.signOut()

        emit(UIState.Error("SIGN_OUT"))
    }
}