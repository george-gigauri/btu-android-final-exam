package btu.finalexam.georgegigauri.data.repository

import android.util.Log
import btu.finalexam.georgegigauri.data.model.Comment
import btu.finalexam.georgegigauri.util.UIState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseUser: FirebaseUser?,
    private val profileRepository: ProfileRepository
) {

    fun getComments(id: String) = flow {
        emit(UIState.Loading())

        val result = retrieve(id).map {
            val user = profileRepository.getUserInfo(it.authorId)
            Comment(
                it.id,
                it.comment,
                it.authorId,
                user?.name,
                user?.image,
                it.timestamp
            )
        }

        Log.d("RESULT_FS", result.toString())

        emit(UIState.Success(result))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }.flowOn(Dispatchers.IO)

    fun addComment(carId: String, commentStr: String) =
        flow {
            emit(UIState.Loading())

            val commentPath = fireStore.collection("cars").document(carId)
                .collection("comments")
            val uid = commentPath.document().id

            val comment = Comment()
            comment.id = uid
            comment.authorId = firebaseUser?.uid!!
            comment.comment = commentStr

            commentPath.document(uid).set(comment).await()

            var comments = retrieve(carId)
            comments = comments.map {
                val user = profileRepository.getUserInfo(it.authorId)
                Comment(
                    it.id,
                    it.comment,
                    it.authorId,
                    user?.name,
                    user?.image,
                    it.timestamp
                )
            }

            emit(UIState.Success(comments))
        }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }.flowOn(Dispatchers.IO)

    private suspend fun retrieve(carId: String): List<Comment> {
        val result = fireStore.collection("cars").document(carId).collection("comments")
            .get().await()
        return result.toObjects(Comment::class.java).map {
            val user = profileRepository.getUserInfo(it.authorId)
            Comment(
                it.id,
                it.comment,
                it.authorId,
                user?.image,
                user?.name,
                it.timestamp
            )
        }
    }
}