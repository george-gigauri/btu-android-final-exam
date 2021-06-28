package btu.finalexam.georgegigauri.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.net.URI
import java.util.*
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val auth : FirebaseAuth,
    private val fireStore : FirebaseFirestore,
    private val storage : FirebaseStorage
){
    fun addCar(brand : String, model : String, description : String, image : Uri) = flow<UIState<Car>> {
        emit(UIState.Loading())

        var user = auth.currentUser


        val imageUrl = addImageToStorage(image)

        val carId = fireStore.collection("cars").document().id
        val documentReference = fireStore.collection("cars").document(carId)


        val car = Car(carId,brand,model,description,imageUrl,user?.displayName, user?.photoUrl?.toString())

        documentReference.set(car).await()

        emit(UIState.Success(car))

    }.catch { emit(UIState.Error(it.message ?: "Unknown Error Message")) }.flowOn(Dispatchers.IO)



    private suspend fun addImageToStorage (uri : Uri) : String {

        val storageReference = storage.reference.child("images/" + UUID.randomUUID().toString())

        val result = storageReference.putFile(uri).await()
        val url = result.metadata?.reference?.downloadUrl.toString()
        return url
    }
}