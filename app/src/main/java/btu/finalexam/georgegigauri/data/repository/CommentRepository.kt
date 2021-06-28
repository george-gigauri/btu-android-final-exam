package btu.finalexam.georgegigauri.data.repository

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
    private val firebaseUser: FirebaseUser
) {

    fun getComments(id: String) = flow {
        emit(UIState.Loading())
        emit(UIState.Success(retrieve(id)))
    }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }.flowOn(Dispatchers.IO)

    fun addComment(carId: String, commentStr: String) =
        flow {
            emit(UIState.Loading())

            val commentPath = fireStore.collection("cars").document(carId)
                .collection("comments")
            val uid = commentPath.id

            val comment = Comment()
            comment.id = uid
            comment.author = firebaseUser.displayName
            comment.authorImage = firebaseUser.photoUrl.toString()
            comment.comment = commentStr

            commentPath.document(uid).set(comment).await()

            val comments = retrieve(carId)
            emit(UIState.Success(comments))
        }.catch { emit(UIState.Error(it.message ?: "Unknown Error")) }.flowOn(Dispatchers.IO)

    private suspend fun retrieve(carId: String): List<Comment> {
        val result = fireStore.collection("cars").document(carId).collection("comments")
            .get().await()
        return result.toObjects(Comment::class.java)
    }
}