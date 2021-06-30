package btu.finalexam.georgegigauri.data.repository

import android.net.Uri
import btu.finalexam.georgegigauri.data.model.Car
import btu.finalexam.georgegigauri.util.UIState
import btu.finalexam.georgegigauri.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val profileRepository: ProfileRepository
) {

    fun getAll(sortBy: HomeViewModel.SortBy) = flow {
        emit(UIState.Loading())

        val result = fireStore.collection("cars")
        var list = if (sortBy != HomeViewModel.SortBy.DEFAULT) {
            result.orderBy(sortBy.toString(), Query.Direction.ASCENDING)
                .get().await().toObjects(Car::class.java)
        } else {
            result.get().await().toObjects(Car::class.java)
        }

        list = list.map {
            val user = profileRepository.getUserInfo(it.userId!!)
            Car(
                it.id,
                it.brand,
                it.model,
                it.description,
                it.image,
                it.userId,
                user?.name,
                user?.image
            )
        }

        emit(UIState.Success(list))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }.flowOn(Dispatchers.IO)

    fun get(carId: String) = flow {
        emit(UIState.Loading())

        val result = fireStore.collection("cars").document(carId).get().await()
        val car = result.toObject(Car::class.java)!!
        val user = profileRepository.getUserInfo(car.userId!!)
        car.authorName = user?.name
        car.authorImage = user?.image

        emit(UIState.Success(car))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }.flowOn(Dispatchers.IO)

    fun addCar(brand: String, model: String, description: String, image: Uri) = flow {
        emit(UIState.Loading())

        val user = auth.currentUser

        val imageUrl = addImageToStorage(image)

        val carId = fireStore.collection("cars").document().id
        val documentReference = fireStore.collection("cars").document(carId)

        val car = Car(
            carId,
            brand,
            model,
            description,
            imageUrl,
            user?.uid
        )

        documentReference.set(car).await()
        emit(UIState.Success(car))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error Message")) }.flowOn(Dispatchers.IO)

    private suspend fun addImageToStorage(uri: Uri): String {
        val storageReference = storage.reference.child("images/" + UUID.randomUUID().toString())

        val result = storageReference.putFile(uri).await()
        return result.metadata?.reference?.downloadUrl?.await().toString()
    }
}